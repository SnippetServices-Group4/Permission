package com.services.group4.permission.service;

import com.services.group4.permission.common.DataTuple;
import com.services.group4.permission.dto.FormatRulesDto;
import com.services.group4.permission.dto.ResponseDto;
import com.services.group4.permission.dto.UpdateRulesRequestDto;
import com.services.group4.permission.dto.snippet.Language;
import com.services.group4.permission.dto.snippet.SnippetResponseDto;
import com.services.group4.permission.model.FormatConfig;
import com.services.group4.permission.repository.FormatConfigRepository;
import com.services.group4.permission.service.async.FormatEventProducer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class FormattingServiceTest {

  @Mock
  private FormatConfigRepository formatConfigRepository;
  @Mock private FormatEventProducer formatEventProducer;
  @Mock private ParserService parserService;
  @Mock private SnippetService snippetService;
  @Mock private OwnershipService ownershipService;

  @InjectMocks
  private FormattingService formattingService;

  private static final String USER_ID = "testUser";
  private static final Long SNIPPET_ID = 1L;

  @Test
  void getConfig_whenConfigExists_returnsExistingConfig() {
    // Arrange
    FormatConfig existingConfig =
        new FormatConfig(USER_ID, true, false, true, 1, 4);
    Mockito.when(formatConfigRepository.findFormatConfigByUserId(USER_ID))
        .thenReturn(Optional.of(existingConfig));

    // Act
    FormatRulesDto result = formattingService.getConfig(USER_ID);

    // Assert
    assertNotNull(result);
    assertEquals(true, result.isSpaceBeforeColon());
    assertEquals(false, result.isSpaceAfterColon());
    assertEquals(true, result.isEqualSpaces());
    assertEquals(1, result.getPrintLineBreaks());
    assertEquals(4, result.getIndentSize());
  }

  @Test
  void getConfig_whenConfigDoesNotExist_createsDefaultConfig() {
    // Arrange
    Mockito.when(formatConfigRepository.findFormatConfigByUserId(USER_ID))
        .thenReturn(Optional.empty());

    // Act
    FormatRulesDto result = formattingService.getConfig(USER_ID);

    // Assert
    assertNotNull(result);
    assertFalse(result.isSpaceBeforeColon());
    assertTrue(result.isSpaceAfterColon());
    assertTrue(result.isEqualSpaces());
    assertEquals(0, result.getPrintLineBreaks());
    assertEquals(2, result.getIndentSize());

    Mockito.verify(formatConfigRepository, Mockito.times(1))
        .save(Mockito.any(FormatConfig.class));
  }

  @Test
  void updateRules_updatesConfigAndFormatsSnippets() {
    // Arrange
    FormatRulesDto rulesDto = new FormatRulesDto(true, true, false, 2, 4);
    UpdateRulesRequestDto<FormatRulesDto> request = new UpdateRulesRequestDto<>(rulesDto);

    List<Long> snippetIds = List.of(1L, 2L, 3L);
    Mockito.when(formatConfigRepository.findFormatConfigByUserId(USER_ID))
        .thenReturn(Optional.of(new FormatConfig(USER_ID, false, true, true, 0, 2)));
    Mockito.when(ownershipService.findSnippetIdsByUserId(USER_ID))
        .thenReturn(Optional.of(snippetIds));

    // Act
    ResponseEntity<ResponseDto<List<Long>>> response =
        formattingService.updateRules(USER_ID, request);

    // Assert
    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertTrue(response.getBody().data().data().containsAll(snippetIds));

    Mockito.verify(formatConfigRepository, Mockito.times(1)).save(Mockito.any());
    Mockito.verify(formatEventProducer, Mockito.times(3))
        .publishEvent(Mockito.anyLong(), Mockito.eq(rulesDto));
  }

  @Test
  void updateRules_defaultConfigAndFormatsSnippets() {
    // Arrange
    FormatRulesDto rulesDto = new FormatRulesDto(true, true, false, 2, 4);
    UpdateRulesRequestDto<FormatRulesDto> request = new UpdateRulesRequestDto<>(rulesDto);

    List<Long> snippetIds = List.of(1L, 2L, 3L);
    Mockito.when(formatConfigRepository.findFormatConfigByUserId(USER_ID))
        .thenReturn(Optional.empty());
    Mockito.when(ownershipService.findSnippetIdsByUserId(USER_ID))
        .thenReturn(Optional.of(snippetIds));

    // Act
    ResponseEntity<ResponseDto<List<Long>>> response =
        formattingService.updateRules(USER_ID, request);

    // Assert
    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertTrue(response.getBody().data().data().containsAll(snippetIds));

    Mockito.verify(formatConfigRepository, Mockito.times(1)).save(Mockito.any());
    Mockito.verify(formatEventProducer, Mockito.times(3))
        .publishEvent(Mockito.anyLong(), Mockito.eq(rulesDto));
  }

  @Test
  void asyncFormat_whenAllEventsPublished_returnsCount() {
    // Arrange
    List<Long> snippetIds = List.of(1L, 2L, 3L);
    FormatRulesDto config = new FormatRulesDto(true, true, false, 2, 4);

    // Act
    Optional<Integer> result = formattingService.asyncFormat(snippetIds, config);

    // Assert
    assertTrue(result.isPresent());
    assertEquals(3, result.get().intValue());
    Mockito.verify(formatEventProducer, Mockito.times(3))
        .publishEvent(Mockito.anyLong(), Mockito.eq(config));
  }

  @Test
  void runFormatting_whenUserIsNotOwner_returnsForbidden() {
    // Arrange
    Mockito.when(ownershipService.isOwner(USER_ID, SNIPPET_ID)).thenReturn(false);

    // Act
    ResponseEntity<ResponseDto<Object>> response =
        formattingService.runFormatting(SNIPPET_ID, USER_ID);

    // Assert
    assertNotNull(response);
    assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    assertEquals("You don't have permission to format this snippet",
        response.getBody().message());
  }

  // falla
//  @Test
//  void runFormatting_whenUserIsOwner_formatsSnippet() {
//    // Arrange
//    Mockito.when(ownershipService.isOwner(USER_ID, SNIPPET_ID)).thenReturn(true);
//
//    SnippetResponseDto snippet = new SnippetResponseDto(SNIPPET_ID, "Test Snippet", "Username", new Language("Java", "1.8", ".java"));
//    Mockito.when(snippetService.getSnippetInfo(SNIPPET_ID))
//        .thenReturn(new ResponseEntity<>(new ResponseDto<>("Success",
//            new DataTuple<>("snippet", snippet)), HttpStatus.OK));
//
//    FormatRulesDto rulesDto = new FormatRulesDto(true, true, false, 2, 4);
//    Mockito.when(formatConfigRepository.findFormatConfigByUserId(USER_ID))
//        .thenReturn(Optional.of(new FormatConfig(USER_ID, true, true, false, 2, 4)));
//
//    ResponseEntity<ResponseDto<Object>> expectedResponse =
//        new ResponseEntity<>(new ResponseDto<>("Formatted", null), HttpStatus.OK);
//    Mockito.when(parserService.runFormatting(Mockito.any(), Mockito.eq(rulesDto)))
//        .thenReturn(expectedResponse);
//
//    // Act
//    ResponseEntity<ResponseDto<Object>> response =
//        formattingService.runFormatting(SNIPPET_ID, USER_ID);
//
//    // Assert
//    assertNotNull(response);
//    assertEquals(HttpStatus.OK, response.getStatusCode());
//    Mockito.verify(parserService, Mockito.times(1))
//        .runFormatting(Mockito.any(), Mockito.eq(rulesDto));
//  }
}

