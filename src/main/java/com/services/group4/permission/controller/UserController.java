package com.services.group4.permission.controller;

import com.services.group4.permission.model.SnippetUser;
import com.services.group4.permission.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
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
            return new ResponseEntity<>("Something went wrong creating the user",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
