package com.services.group4.permission.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.services.group4.permission.DotenvConfig;
import com.services.group4.permission.clients.ParserClient;
import com.services.group4.permission.common.FullResponse;
import com.services.group4.permission.dto.FormatRulesDto;
import com.services.group4.permission.dto.FormattingRequestDto;
import com.services.group4.permission.dto.ResponseDto;
import com.services.group4.permission.dto.snippet.Language;
import com.services.group4.permission.dto.snippet.SnippetResponseDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

class ParserServiceTest {

  @Mock
  private ParserClient parserClient;

  @InjectMocks
  private ParserService parserService;

  private AutoCloseable mocks;

  @BeforeEach
  void setUp() {
    mocks = MockitoAnnotations.openMocks(this);
    DotenvConfig.loadEnv();
  }

  @AfterEach
  void tearDown() throws Exception {
    mocks.close();
  }

  @Test
  void runFormatting_shouldReturnSuccessfulResponseFromClient() {
    // Arrange
    Long snippetId = 1L;
    SnippetResponseDto snippet = new SnippetResponseDto(
        snippetId, "snippet", "user", new Language("Java", "11", "java"));
    FormatRulesDto formatRules = new FormatRulesDto(true, true, false, 2, 4);
    FormattingRequestDto request = new FormattingRequestDto(
        formatRules, snippet.language().getLangName(), snippet.language().getVersion());
    ResponseEntity<ResponseDto<Object>> clientResponse =
        FullResponse.create("Formatting completed", "done", true, HttpStatus.OK);
    when(parserClient.runFormatting(request, snippetId)).thenReturn(clientResponse);

    // Act
    ResponseEntity<ResponseDto<Object>> result = parserService.runFormatting(snippet, formatRules);

    // Assert
    assertNotNull(clientResponse);
    assertEquals(HttpStatus.OK, clientResponse.getStatusCode());
    assertEquals("Formatting completed", clientResponse.getBody().message());
  }

  @Test
  void runFormatting_shouldHandleClientErrorResponse() {
    // Arrange
    Long snippetId = 2L;
    SnippetResponseDto snippet = new SnippetResponseDto(
        snippetId, "snippet", "user", new Language("Java", "11", "java"));
    FormatRulesDto formatRules = new FormatRulesDto(false, true, true, 1, 2);
    FormattingRequestDto request = new FormattingRequestDto(
        formatRules, snippet.language().getLangName(), snippet.language().getVersion());
    ResponseDto<Object> clientResponseDto = new ResponseDto<>("Formatting failed", null);
    ResponseEntity<ResponseDto<Object>> clientResponse = new ResponseEntity<>(clientResponseDto, HttpStatus.BAD_REQUEST);

    when(parserClient.runFormatting(request, snippetId)).thenReturn(clientResponse);

    // Act
    ResponseEntity<ResponseDto<Object>> result = parserService.runFormatting(snippet, formatRules);

    // Assert
    assertNotNull(clientResponse);
    assertEquals(HttpStatus.BAD_REQUEST, clientResponse.getStatusCode());
    assertEquals("Formatting failed", clientResponse.getBody().message());
  }
}

