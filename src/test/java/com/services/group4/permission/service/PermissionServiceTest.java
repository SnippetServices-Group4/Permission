package com.services.group4.permission.service;

import com.services.group4.permission.common.FullResponse;
import com.services.group4.permission.dto.ResponseDto;
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
class PermissionServiceTest {

  @Mock
  private ReaderService readerService;

  @Mock
  private OwnershipService ownershipService;

  @InjectMocks
  private PermissionService permissionService;

  @Test
  void getAllowedSnippets_shouldReturnOkIfUserHasPermissions() {
    // Arrange
    String userId = "user123";
    List<Long> readerSnippets = List.of(1L, 2L);
    List<Long> ownerSnippets = List.of(3L, 4L);

    when(readerService.findSnippetIdsByUserId(userId)).thenReturn(Optional.of(readerSnippets));
    when(ownershipService.findSnippetIdsByUserId(userId)).thenReturn(Optional.of(ownerSnippets));

    // Act
    ResponseEntity<ResponseDto<List<Long>>> response = permissionService.getAllowedSnippets(userId);

    // Assert
    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertTrue(response.getBody().data().data().containsAll(readerSnippets));
    assertTrue(response.getBody().data().data().containsAll(ownerSnippets));
  }

  @Test
  void getAllowedSnippets_shouldReturnEmptyIfNoPermissions() {
    // Arrange
    String userId = "user123";
    when(readerService.findSnippetIdsByUserId(userId)).thenReturn(Optional.empty());
    when(ownershipService.findSnippetIdsByUserId(userId)).thenReturn(Optional.empty());

    // Act
    ResponseEntity<ResponseDto<List<Long>>> response = permissionService.getAllowedSnippets(userId);

    // Assert
    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertTrue(response.getBody().data().data().isEmpty());
  }

  @Test
  void deletePermissionsOfSnippet_shouldReturnOkIfPermissionsDeletedSuccessfully() {
    // Arrange
    String userId = "user123";
    Long snippetId = 1L;
    ResponseEntity<ResponseDto<Long>> responseOwnership = FullResponse.create("Ownership deleted", "snippetId", snippetId, HttpStatus.OK);
    ResponseEntity<ResponseDto<Long>> responseReader = FullResponse.create("Readers deleted", "snippetId", snippetId, HttpStatus.OK);

    when(ownershipService.deleteOwnership(userId, snippetId)).thenReturn(responseOwnership);
    when(readerService.deleteReaders(snippetId)).thenReturn(responseReader);

    // Act
    ResponseEntity<ResponseDto<Long>> response = permissionService.deletePermissionsOfSnippet(userId, snippetId);

    // Assert
    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals("Permissions and shared relations deleted successfully", response.getBody().message());
  }

  @Test
  void deletePermissionsOfSnippet_shouldReturnOnlyOwnershipDeleted() {
    // Arrange
    String userId = "user123";
    Long snippetId = 1L;
    ResponseEntity<ResponseDto<Long>> responseOwnership = FullResponse.create("Ownership deleted", "snippetId", snippetId, HttpStatus.OK);
    ResponseEntity<ResponseDto<Long>> responseReader = FullResponse.create("Forbidden", "snippetId", snippetId, HttpStatus.FORBIDDEN);

    when(ownershipService.deleteOwnership(userId, snippetId)).thenReturn(responseOwnership);
    when(readerService.deleteReaders(snippetId)).thenReturn(responseReader);

    // Act
    ResponseEntity<ResponseDto<Long>> response = permissionService.deletePermissionsOfSnippet(userId, snippetId);

    // Assert
    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals("Permissions deleted successfully", response.getBody().message());
  }

  @Test
  void deletePermissionsOfSnippet_shouldReturnOwnershipErrorIfFailed() {
    // Arrange
    String userId = "user123";
    Long snippetId = 1L;
    ResponseEntity<ResponseDto<Long>> responseOwnership = FullResponse.create("Ownership deletion failed", "snippetId", snippetId, HttpStatus.INTERNAL_SERVER_ERROR);

    when(ownershipService.deleteOwnership(userId, snippetId)).thenReturn(responseOwnership);

    // Act
    ResponseEntity<ResponseDto<Long>> response = permissionService.deletePermissionsOfSnippet(userId, snippetId);

    // Assert
    assertNotNull(response);
    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    assertEquals("Ownership deletion failed", response.getBody().message());
  }


  @Test
  void hasPermissionOnSnippet_shouldReturnTrueIfUserHasPermission() {
    // Arrange
    String userId = "user123";
    Long snippetId = 1L;

    ResponseEntity<ResponseDto<Boolean>> readerPermission = FullResponse.create("User has reader permission", "permission", true, HttpStatus.OK);
    ResponseEntity<ResponseDto<Boolean>> ownerPermission = FullResponse.create("User has owner permission", "permission", true, HttpStatus.OK);

    when(readerService.getReaderPermission(userId, snippetId)).thenReturn(readerPermission);
    when(ownershipService.hasOwnerPermission(userId, snippetId)).thenReturn(ownerPermission);

    // Act
    ResponseEntity<ResponseDto<Boolean>> response = permissionService.hasPermissionOnSnippet(userId, snippetId);

    // Assert
    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertTrue(response.getBody().data().data());
  }

  @Test
  void hasPermissionOnSnippet_shouldReturnFalseIfUserHasNoPermission() {
    // Arrange
    String userId = "user123";
    Long snippetId = 1L;

    ResponseEntity<ResponseDto<Boolean>> readerPermission = FullResponse.create("User has no reader permission", "permission", false, HttpStatus.FORBIDDEN);
    ResponseEntity<ResponseDto<Boolean>> ownerPermission = FullResponse.create("User has no owner permission", "permission", false, HttpStatus.FORBIDDEN);

    when(readerService.getReaderPermission(userId, snippetId)).thenReturn(readerPermission);
    when(ownershipService.hasOwnerPermission(userId, snippetId)).thenReturn(ownerPermission);

    // Act
    ResponseEntity<ResponseDto<Boolean>> response = permissionService.hasPermissionOnSnippet(userId, snippetId);

    // Assert
    assertNotNull(response);
    assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    assertFalse(response.getBody().data().data());
  }

}

