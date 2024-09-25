package com.services.group4.permission.config;

import com.services.group4.permission.model.SnippetUser;
import com.services.group4.permission.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

@Component
public class DatabaseInitializer {

    private final UserRepository snippetUserRepository;

    @Autowired
    public DatabaseInitializer(UserRepository snippetUserRepository) {
      this.snippetUserRepository = snippetUserRepository;
    }

    @PostConstruct
    public void init() {
        snippetUserRepository.deleteAll();

        for (int i = 1; i <= 10; i++) {
            SnippetUser user = new SnippetUser();
            user.setId((long) i);
            user.setUsername("User" + i);
            user.setPassword("password" + i);
            user.setEmail("user" + i + "@mail.com");
            snippetUserRepository.save(user);
        }
      SnippetUser user = new SnippetUser("Jane Doe", "password","user@mail.com");
      snippetUserRepository.save(user);
    }
}