package com.services.group4.permission.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.services.group4.permission.model.SnippetUser;
import com.services.group4.permission.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class SnippetUserTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

  @Autowired
  private UserRepository userRepository;

  @BeforeEach
  public void setUp() {
    userRepository.deleteAll(); // Clear the repository to avoid conflicts
    SnippetUser snippetUser = new SnippetUser(1L,"testUser", "testPassword", "test@example.com");
    System.out.println(userRepository.save(snippetUser));
    System.out.println("User saved: " + userRepository.findById(1L).orElse(null));
  }

    @Test
    public void testAddUserSuccess() throws Exception {
      SnippetUser snippetUser = new SnippetUser(2L,"testUser2", "testPassword2", "test2@example.com");

      mockMvc.perform(post("/user/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(snippetUser)))
                .andExpect(status().isCreated())
                .andExpect(content().string("User added"));
    }

    @Test
    public void testLoginUserSuccess() throws Exception {
      SnippetUser snippetUser = new SnippetUser(1L,"testUser", "testPassword", "test@example.com");

      mockMvc.perform(post("/user/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(snippetUser)))
                .andExpect(status().isOk())
                .andExpect(content().string("Login successful"));
    }

    @Test
    public void testGetUserByIdSuccess() throws Exception {
        mockMvc.perform(get("/user/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("testUser"));
    }

    @Test
    public void testGetAllUsersSuccess() throws Exception {
        mockMvc.perform(get("/user"))
                .andExpect(status().isOk());
    }

    @Test
    public void testUpdateUserSuccess() throws Exception {
      SnippetUser snippetUser = new SnippetUser(1L,"testUser", "testPassword", "test@example.com");

      snippetUser.setUsername("updatedUser");
      mockMvc.perform(put("/user/update/{id}", 1L)
              .contentType(MediaType.APPLICATION_JSON)
              .content(objectMapper.writeValueAsString(snippetUser)))
              .andExpect(status().isOk())
              .andExpect(content().string("User updated"));
    }

    @Test
    public void testDeleteUserSuccess() throws Exception {
        mockMvc.perform(delete("/user/delete/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().string("User deleted"));
    }
}