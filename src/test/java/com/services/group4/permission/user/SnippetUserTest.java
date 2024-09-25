package com.services.group4.permission.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.services.group4.permission.model.SnippetUser;
import com.services.group4.permission.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.Order;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SnippetUserTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private static UserRepository userRepository;

    @BeforeAll
    public static void cleanDatabaseBeforeAll(@Autowired UserRepository userRepository) {
      SnippetUserTest.userRepository = userRepository;
      userRepository.deleteAll();
      SnippetUser snippetUser = new SnippetUser("testUser", "testPassword", "test@example.com");
      userRepository.save(snippetUser);
    }



    @Test
    @Order(1)
    public void testAddUserSuccess() throws Exception {
      System.out.println("User saved: " + userRepository.findByUsername("testUser").get().toJson());

        SnippetUser snippetUser2 = new SnippetUser("testUser2", "testPassword2", "test2@example.com");

        mockMvc.perform(post("/user/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(snippetUser2)))
                .andExpect(status().isCreated())
                .andExpect(content().string("User added"));
    }

    @Test
    @Order(2)
    public void testLoginUserSuccess() throws Exception {
      System.out.println("User saved: " + userRepository.findByUsername("testUser").get().toJson());

        SnippetUser snippetUser2 = new SnippetUser("testUser", "testPassword", "test@example.com");

        mockMvc.perform(post("/user/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(snippetUser2)))
                .andExpect(status().isOk())
                .andExpect(content().string("Login successful"));

        System.out.println("User logged in: " + userRepository.findByUsername("testUser").get().toJson());
    }

    @Test
    @Order(3)
    public void testGetUserByIdSuccess() throws Exception {
      System.out.println("User saved: " + userRepository.findByUsername("testUser").get().toJson());

        mockMvc.perform(get("/user/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("testUser"));

        System.out.println("User by ID: " + userRepository.findById(1L).get().toJson());
    }

    @Test
    @Order(4)
    public void testGetAllUsersSuccess() throws Exception {
      System.out.println("User saved: " + userRepository.findByUsername("testUser").get().toJson());

      mockMvc.perform(get("/user"))
                .andExpect(status().isOk());
    }

    @Test
    @Order(5)
    public void testUpdateUserSuccess() throws Exception {
      System.out.println("User saved: " + userRepository.findByUsername("testUser").get().toJson());

        SnippetUser snippetUser2 = userRepository.findByUsername("testUser").get();

        snippetUser2.setUsername("updatedUser");
        mockMvc.perform(put("/user/update/{id}", snippetUser2.getUserID())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(snippetUser2)))
                .andExpect(status().isOk())
                .andExpect(content().string("User updated"));

        System.out.println("User updated: " + userRepository.findByUsername("updatedUser").get().toJson());
    }

    @Test
    @Order(6)
    public void testDeleteUserSuccess() throws Exception {
      System.out.println("User saved: " + userRepository.findByUsername("updatedUser").get().toJson());

        mockMvc.perform(delete("/user/delete/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().string("User deleted"));

        System.out.println("User deleted: " + userRepository.findByUsername("updatedUser").orElse(null));
    }
}