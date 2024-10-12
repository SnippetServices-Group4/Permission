package com.services.group4.permission.user;

import static org.junit.jupiter.api.Assertions.*;

import com.services.group4.permission.mock.UserFixtures;
import com.services.group4.permission.model.SnippetUser;
import com.services.group4.permission.repository.UserRepository;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension.class)
@ActiveProfiles(value = "test")
@AutoConfigureWebTestClient
public class UserRequestE2ETests {


  @Autowired private WebTestClient client;

  @Autowired private UserRepository userRepository;

  private final String BASE = "/user";

  @BeforeEach
  public void setup() {
    userRepository.saveAll(UserFixtures.all());
  }

  @Test
  public void canGetAllUsers() {
    client
        .get()
        .uri(BASE)
        .exchange()
        .expectStatus()
        .isOk()
        .expectBodyList(SnippetUser.class)
        .hasSize(3);
  }

  @Test
  public void canGetUserById() {
    Long userId = userRepository.findByUsername("John Doe").orElseThrow().getUserID();
    System.out.println(userId);
    client
        .get()
        .uri(BASE + "/{userId}", userId)
        .exchange()
        .expectStatus()
        .isOk()
        .expectBody(SnippetUser.class)
        .value(user -> assertEquals("John Doe", user.getUsername()));
  }

  @Test
  public void canCreateUser() {
    SnippetUser newUser = new SnippetUser("new_user", "new_password", "new.user@example.com");
    client.post().uri(BASE + "/register").bodyValue(newUser).exchange().expectStatus().isCreated();

    List<SnippetUser> users = userRepository.findAll();
    assertEquals(4, users.size());
  }

  @Test
  public void canUpdateUser() {
    SnippetUser updatedUser =
        new SnippetUser("updated_user", "updated_password", "updated.user@example.com");
    client.put().uri(BASE + "/update/1").bodyValue(updatedUser).exchange().expectStatus().isOk();

    SnippetUser user = userRepository.findById(1L).orElse(null);
    assertNotNull(user);
    assertEquals("updated_user", user.getUsername());
  }

  @Test
  public void canDeleteUser() {
    client.delete().uri(BASE + "/delete/1").exchange().expectStatus().isOk();

    SnippetUser user = userRepository.findById(1L).orElse(null);
    assertNull(user);
  }

  @AfterEach
  public void tearDown() {
    userRepository.deleteAll();
  }
}
