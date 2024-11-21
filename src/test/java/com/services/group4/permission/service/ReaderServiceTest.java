package com.services.group4.permission.service;

import com.services.group4.permission.dto.ResponseDto;
import com.services.group4.permission.model.Reader;
import com.services.group4.permission.repository.ReaderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReaderServiceTest {

  @Mock
  private ReaderRepository readerRepository;

  @Mock
  private OwnershipService ownershipService;

  @Mock
  private ValidationService validationService;

  @InjectMocks
  private ReaderService readerService;

  @Test
  void shareSnippet_shouldReturnBadRequestIfUserIsInvalid() {
    // Arrange
    String ownerId = "user123";
    Long snippetId = 1L;
    String targetUserId = "user456";
    when(validationService.isUserIdInvalid(ownerId, targetUserId)).thenReturn(true);

    // Act
    ResponseEntity<ResponseDto<String>> response = readerService.shareSnippet(ownerId, snippetId, targetUserId);

    // Assert
    assertNotNull(response);
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertEquals("User isn't valid, it doesn't exists", response.getBody().message());
  }

  @Test
  void shareSnippet_shouldReturnForbiddenIfUserIsNotOwner() {
    // Arrange
    String ownerId = "user123";
    Long snippetId = 1L;
    String targetUserId = "user456";
    when(validationService.isUserIdInvalid(ownerId, targetUserId)).thenReturn(false);
    when(ownershipService.isOwner(ownerId, snippetId)).thenReturn(false);

    // Act
    ResponseEntity<ResponseDto<String>> response = readerService.shareSnippet(ownerId, snippetId, targetUserId);

    // Assert
    assertNotNull(response);
    assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    assertEquals("User is not the owner of the snippet", response.getBody().message());
  }

  @Test
  void shareSnippet_shouldReturnSuccessWhenSnippetIsShared() {
    // Arrange
    String ownerId = "user123";
    Long snippetId = 1L;
    String targetUserId = "user456";
    when(validationService.isUserIdInvalid(ownerId, targetUserId)).thenReturn(false);
    when(ownershipService.isOwner(ownerId, snippetId)).thenReturn(true);

    // Act
    ResponseEntity<ResponseDto<String>> response = readerService.shareSnippet(ownerId, snippetId, targetUserId);

    // Assert
    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals("Snippet shared successfully", response.getBody().message());
  }

  @Test
  void isReader_shouldReturnTrueIfUserIsReader() {
    // Arrange
    String userId = "user456";
    Long snippetId = 1L;
    when(readerRepository.findReaderByUserIdAndSnippetId(userId, snippetId))
        .thenReturn(Optional.of(new Reader(userId, snippetId)));

    // Act
    boolean result = readerService.isReader(userId, snippetId);

    // Assert
    assertTrue(result);
  }

  @Test
  void isReader_shouldReturnFalseIfUserIsNotReader() {
    // Arrange
    String userId = "user456";
    Long snippetId = 1L;
    when(readerRepository.findReaderByUserIdAndSnippetId(userId, snippetId))
        .thenReturn(Optional.empty());

    // Act
    boolean result = readerService.isReader(userId, snippetId);

    // Assert
    assertFalse(result);
  }

  @Test
  void deleteReaders_shouldReturnOkIfReadersExist() {
    // Arrange
    Long snippetId = 1L;
    List<Reader> readers = List.of(new Reader("user456", snippetId));
    when(readerRepository.findReadersBySnippetId(snippetId)).thenReturn(Optional.of(readers));

    // Act
    ResponseEntity<ResponseDto<Long>> response = readerService.deleteReaders(snippetId);

    // Assert
    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals("Readers deleted", response.getBody().message());
  }

  @Test
  void deleteReaders_shouldReturnNoContentIfNoReaders() {
    // Arrange
    Long snippetId = 1L;
    when(readerRepository.findReadersBySnippetId(snippetId)).thenReturn(Optional.empty());

    // Act
    ResponseEntity<ResponseDto<Long>> response = readerService.deleteReaders(snippetId);

    // Assert
    assertNotNull(response);
    assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    assertEquals("Snippet wasn't shared with other users", response.getBody().message());
  }
}


