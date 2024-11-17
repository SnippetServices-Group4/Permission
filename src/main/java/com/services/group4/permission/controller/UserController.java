package com.services.group4.permission.controller;

import com.services.group4.permission.model.SnippetUser;
import com.services.group4.permission.repository.UserRepository;

import java.net.http.HttpResponse;
import java.util.List;
import java.util.Optional;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/user")
public class UserController {
  private final UserRepository userRepository;

  public UserController(UserRepository userRepository) {
    this.userRepository = userRepository;
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
  public ResponseEntity<String> getAll() {
    String url = "https://dev-ybvfkgr1bd82iozp.us.auth0.com/oauth/token";
    String body = "grant_type=client_credentials&client_id=G5JC0DlwnrIv7YlY03lqCdBcKqY0RLI4&client_secret=%7ByourClientSecret%7D&audience=https%3A%2F%2Fdev-ybvfkgr1bd82iozp.us.auth0.com%2Fapi%2Fv2%2F";

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

    HttpEntity<String> request = new HttpEntity<>(body, headers);

    ResponseEntity<String> response = new RestTemplate().postForEntity(url, request, String.class);
    response.getBody();

    return new ResponseEntity<>("All users", HttpStatus.OK);
  }
}
