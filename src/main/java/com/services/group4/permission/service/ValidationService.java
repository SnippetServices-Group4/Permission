package com.services.group4.permission.service;

import com.services.group4.permission.dto.ResponseDto;
import com.services.group4.permission.dto.UserDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ValidationService {

  final Auth0Users auth0Users;

  public ValidationService(com.services.group4.permission.service.Auth0Users auth0Users) {
    this.auth0Users = auth0Users;
  }

  public boolean isUserIdInvalid(String ownerId, String targetUserId) {
    ResponseDto<List<UserDto>> listOfUsers= auth0Users.getUsers(ownerId);
    List<UserDto> users = listOfUsers.data().data();

    for (UserDto user : users) {
      if (user.userId().equals(targetUserId)) {
        return false;
      }
    }
    return true;
  }
}
