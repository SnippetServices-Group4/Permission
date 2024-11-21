package com.services.group4.permission.controller;

import com.services.group4.permission.dto.ResponseDto;
import com.services.group4.permission.dto.UserDto;
import com.services.group4.permission.service.Auth0Users;
import com.services.group4.permission.service.TokenService;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {
  private final Auth0Users auth0Users;

  public UserController(RestTemplate restTemplate) {
    this.auth0Users = new Auth0Users(new TokenService(restTemplate), restTemplate);
  }

  @GetMapping("/getAll")
  public ResponseDto<List<UserDto>> getUsers(@RequestHeader("userId") String userId) {
    log.info("Getting all users");
    return auth0Users.getUsers(userId);
  }
}

