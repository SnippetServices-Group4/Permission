package com.services.group4.permission.service;

import com.services.group4.permission.dto.ResponseDto;
import com.services.group4.permission.model.Ownership;
import com.services.group4.permission.repository.OwnershipRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OwnershipServiceTest {

  @Mock
  private OwnershipRepository ownershipRepository;

  @InjectMocks
  private OwnershipService ownershipService;

  @Test
  void createOwnership_shouldReturnCreatedResponse() {
    // Arrange
    String userId = "user123";
    Long snippetId = 1L;
    Ownership ownership = new Ownership(userId, snippetId);

    when(ownershipRepository.save(any(Ownership.class))).thenReturn(ownership);

    // Act
    ResponseEntity<ResponseDto<Long>> response = ownershipService.createOwnership(userId, snippetId);

    // Assert
    assertNotNull(response);
    assertEquals(HttpStatus.CREATED, response.getStatusCode());
    assertEquals(snippetId, response.getBody().data().data());
    assertEquals("Ownership created", response.getBody().message());
  }

  @Test
  void isOwner_shouldReturnTrueWhenOwnershipExists() {
    // Arrange
    String userId = "user123";
    Long snippetId = 1L;

    when(ownershipRepository.findOwnershipByUserIdAndSnippetId(userId, snippetId))
        .thenReturn(Optional.of(new Ownership(userId, snippetId)));

    // Act
    boolean isOwner = ownershipService.isOwner(userId, snippetId);

    // Assert
    assertTrue(isOwner);
  }

  @Test
  void isOwner_shouldReturnFalseWhenOwnershipDoesNotExist() {
    // Arrange
    String userId = "user123";
    Long snippetId = 1L;

    when(ownershipRepository.findOwnershipByUserIdAndSnippetId(userId, snippetId))
        .thenReturn(Optional.empty());

    // Act
    boolean isOwner = ownershipService.isOwner(userId, snippetId);

    // Assert
    assertFalse(isOwner);
    verify(ownershipRepository).findOwnershipByUserIdAndSnippetId(userId, snippetId);
  }

  @Test
  void hasOwnerPermission_shouldReturnTrueWhenUserIsOwner() {
    // Arrange
    String userId = "user123";
    Long snippetId = 1L;

    when(ownershipRepository.findOwnershipByUserIdAndSnippetId(userId, snippetId))
        .thenReturn(Optional.of(new Ownership(userId, snippetId)));

    // Act
    ResponseEntity<ResponseDto<Boolean>> response = ownershipService.hasOwnerPermission(userId, snippetId);

    // Assert
    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertTrue(response.getBody().data().data());
    assertEquals("User is the owner of the snippet", response.getBody().message());
    verify(ownershipRepository).findOwnershipByUserIdAndSnippetId(userId, snippetId);
  }

  @Test
  void hasOwnerPermission_shouldReturnFalseWhenUserIsNotOwner() {
    // Arrange
    String userId = "user123";
    Long snippetId = 1L;

    when(ownershipRepository.findOwnershipByUserIdAndSnippetId(userId, snippetId))
        .thenReturn(Optional.empty());

    // Act
    ResponseEntity<ResponseDto<Boolean>> response = ownershipService.hasOwnerPermission(userId, snippetId);

    // Assert
    assertNotNull(response);
    assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    assertFalse(response.getBody().data().data());
    assertEquals("User is not the owner of the snippet", response.getBody().message());
    verify(ownershipRepository).findOwnershipByUserIdAndSnippetId(userId, snippetId);
  }

  @Test
  void deleteOwnership_shouldReturnOkResponseWhenOwnershipExists() {
    // Arrange
    String userId = "user123";
    Long snippetId = 1L;
    Ownership ownership = new Ownership(userId, snippetId);

    when(ownershipRepository.findOwnershipByUserIdAndSnippetId(userId, snippetId))
        .thenReturn(Optional.of(ownership));

    // Act
    ResponseEntity<ResponseDto<Long>> response = ownershipService.deleteOwnership(userId, snippetId);

    // Assert
    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(snippetId, response.getBody().data().data());
    assertEquals("Ownership deleted", response.getBody().message());

    // Verificar que se llamó dos veces al repositorio
    verify(ownershipRepository, times(2)).findOwnershipByUserIdAndSnippetId(userId, snippetId);

    // Verificar que el ownership fue eliminado
    verify(ownershipRepository).delete(ownership);
  }


  @Test
  void deleteOwnership_shouldReturnForbiddenResponseWhenOwnershipDoesNotExist() {
    // Arrange
    String userId = "user123";
    Long snippetId = 1L;

    when(ownershipRepository.findOwnershipByUserIdAndSnippetId(userId, snippetId))
        .thenReturn(Optional.empty());

    // Act
    ResponseEntity<ResponseDto<Long>> response = ownershipService.deleteOwnership(userId, snippetId);

    // Assert
    assertNotNull(response);
    assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    assertEquals(snippetId, response.getBody().data().data());
    assertEquals("User is not the owner of the snippet", response.getBody().message());
    verify(ownershipRepository).findOwnershipByUserIdAndSnippetId(userId, snippetId);
    verify(ownershipRepository, never()).delete(any(Ownership.class));
  }
}

