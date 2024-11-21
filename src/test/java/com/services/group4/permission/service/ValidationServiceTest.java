package com.services.group4.permission.service;

import com.services.group4.permission.common.DataTuple;
import com.services.group4.permission.dto.ResponseDto;
import com.services.group4.permission.dto.UserDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ValidationServiceTest {

  @Mock
  private Auth0Users auth0Users;

  @InjectMocks
  private ValidationService validationService;

  @Test
  void isUserIdInvalid_shouldReturnFalseWhenTargetUserExists() {
    // Arrange
    String ownerId = "owner123";
    String targetUserId = "target456";

    List<UserDto> users = List.of(
        new UserDto("target456", "Target User"),
        new UserDto("another789", "Another User")
    );
    ResponseDto<List<UserDto>> response = new ResponseDto<>("users", new DataTuple("users", users));

    when(auth0Users.getUsers(ownerId)).thenReturn(response);

    // Act
    boolean result = validationService.isUserIdInvalid(ownerId, targetUserId);

    // Assert
    assertFalse(result);
    verify(auth0Users).getUsers(ownerId);
  }

  @Test
  void isUserIdInvalid_shouldReturnTrueWhenTargetUserDoesNotExist() {
    // Arrange
    String ownerId = "owner123";
    String targetUserId = "nonexistentUser";

    List<UserDto> users = List.of(
        new UserDto("target456", "Target User"),
        new UserDto("another789", "Another User")
    );
    ResponseDto<List<UserDto>> response = new ResponseDto<>("users", new DataTuple("users", users));

    when(auth0Users.getUsers(ownerId)).thenReturn(response);

    // Act
    boolean result = validationService.isUserIdInvalid(ownerId, targetUserId);

    // Assert
    assertTrue(result);
    verify(auth0Users).getUsers(ownerId);
  }


  @Test
  void isUserIdInvalid_shouldReturnTrueWhenUsersListIsEmpty() {
    // Arrange
    String ownerId = "owner123";
    String targetUserId = "target456";

    List<UserDto> emptyUsers = List.of();
    ResponseDto<List<UserDto>> response = new ResponseDto<>("users", new DataTuple("users", emptyUsers));
    when(auth0Users.getUsers(ownerId)).thenReturn(response);

    // Act
    boolean result = validationService.isUserIdInvalid(ownerId, targetUserId);

    // Assert
    assertTrue(result);
    verify(auth0Users).getUsers(ownerId);
  }
}

