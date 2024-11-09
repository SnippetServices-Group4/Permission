package com.services.group4.permission.service;

import com.services.group4.permission.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ValidationService {

  @Autowired private UserRepository userRepository;

  public boolean isUserIdValid(Long userId) {
    return userRepository.existsById(userId);
  }
}
