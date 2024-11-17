package com.services.group4.permission.service;

import org.springframework.stereotype.Service;

@Service
public class ValidationService {

  // TODO: Check if the user is valid on AUTH0
  public boolean isUserIdValid(String userId) {
    return true;
  }
}
