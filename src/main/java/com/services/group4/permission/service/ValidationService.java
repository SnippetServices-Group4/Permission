package com.services.group4.permission.service;

import com.services.group4.permission.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ValidationService {

  // FUTURE: Check if the user is valid on AUTH0
  @Autowired private UserRepository userRepository;

  public boolean isUserIdValid(String userId) {
    return userRepository.existsById(userId);
  }
}
