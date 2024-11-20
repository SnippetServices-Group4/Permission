package com.services.group4.permission.controller;

import com.services.group4.permission.dto.ResponseDto;
import com.services.group4.permission.dto.UserDto;
import com.services.group4.permission.model.SnippetUser;
import com.services.group4.permission.repository.UserRepository;
import com.services.group4.permission.service.Auth0Users;
import com.services.group4.permission.service.TokenService;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {
  private final UserRepository userRepository;
  private final Auth0Users auth0Users;

  public UserController(UserRepository userRepository, RestTemplate restTemplate) {
    this.userRepository = userRepository;
    this.auth0Users = new Auth0Users(new TokenService(restTemplate), restTemplate);
  }

  @PostMapping("/register")
  public ResponseEntity<String> addUser(@RequestBody SnippetUser snippetUser) {
    try {
      userRepository.save(snippetUser);
      return new ResponseEntity<>("User added", HttpStatus.CREATED);
    } catch (Exception e) {
      System.out.println(e.getMessage());
      return new ResponseEntity<>(
          "Something went wrong creating the user", HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @PostMapping("/login")
  public ResponseEntity<String> loginUser(@RequestBody SnippetUser loginUser) {
    Optional<SnippetUser> user = userRepository.findByUsername(loginUser.getUsername());
    if (user.isPresent() && user.get().getPassword().equals(loginUser.getPassword())) {
      return new ResponseEntity<>("Login successful", HttpStatus.OK);
    } else {
      return new ResponseEntity<>("Invalid username or password", HttpStatus.UNAUTHORIZED);
    }
  }

  @GetMapping("/{id}")
  public ResponseEntity<SnippetUser> getUserById(@PathVariable String id) {
    Optional<SnippetUser> user = userRepository.findById(id);
    return user.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
        .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
  }

  @GetMapping
  public ResponseEntity<List<SnippetUser>> getAllUsers() {
    List<SnippetUser> users = userRepository.findAll();
    return new ResponseEntity<>(users, HttpStatus.OK);
  }

  @PutMapping("/update/{id}")
  public ResponseEntity<String> updateUser(
      @PathVariable String id, @RequestBody SnippetUser updatedUser) {
    Optional<SnippetUser> user = userRepository.findById(id);
    if (user.isPresent()) {
      SnippetUser existingUser = user.get();
      existingUser.setUsername(updatedUser.getUsername());
      existingUser.setPassword(updatedUser.getPassword());
      existingUser.setEmail(updatedUser.getEmail());
      userRepository.save(existingUser);
      return new ResponseEntity<>("User updated", HttpStatus.OK);
    } else {
      return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
    }
  }

  @DeleteMapping("/delete/{id}")
  public ResponseEntity<String> deleteUser(@PathVariable String id) {
    try {
      userRepository.deleteById(id);
      return new ResponseEntity<>("User deleted", HttpStatus.OK);
    } catch (Exception e) {
      System.out.println(e.getMessage());
      return new ResponseEntity<>(
          "Something went wrong deleting the user", HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @GetMapping("/search")
  public ResponseEntity<List<SnippetUser>> searchUsers(@RequestParam String username) {
    List<SnippetUser> users = userRepository.findByUsernameContaining(username);
    return new ResponseEntity<>(users, HttpStatus.OK);
  }

  @GetMapping("/getAll")
  public ResponseDto<List<UserDto>> getUsers(@RequestHeader("userId") String userId) {
    return auth0Users.getUsers(userId);
  }
}

