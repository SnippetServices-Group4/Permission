package com.services.group4.permission.user;

import static org.junit.jupiter.api.Assertions.*;

import com.services.group4.permission.mock.OwnershipFixtures;
import com.services.group4.permission.model.Ownership;
import com.services.group4.permission.repository.OwnershipRepository;
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
public class OwnershipRequestE2ETests {

  @Autowired private WebTestClient client;

  @Autowired private OwnershipRepository ownershipRepository;

  private final String BASE = "/ownership";

  @BeforeEach
  public void setup() {
    ownershipRepository.saveAll(OwnershipFixtures.all());
  }

  @Test
  public void canGetAllOwnership() {
    client
        .get()
        .uri(BASE)
        .exchange()
        .expectStatus()
        .isOk()
        .expectBodyList(Ownership.class)
        .hasSize(3);
  }

  @Test
  public void canGetOwnershipByUserId() {
    client
        .get()
        .uri(BASE + "/user/1")
        .exchange()
        .expectStatus()
        .isOk()
        .expectBody(Ownership.class)
        .value(ownership -> assertEquals(1L, ownership.getUserId()));
  }

  @Test
  public void canGetOwnershipBySnippetId() {
    client
        .get()
        .uri(BASE + "/snippet/1")
        .exchange()
        .expectStatus()
        .isOk()
        .expectBody(Ownership.class)
        .value(ownership -> assertEquals(1L, ownership.getSnippetId()));
  }

  @Test
  public void canCreateOwnership() {
    Ownership newOwnerships = new Ownership(4L, 4L);
    client
        .post()
        .uri(BASE + "/create")
        .bodyValue(newOwnerships)
        .exchange()
        .expectStatus()
        .isCreated();

    List<Ownership> ownerships = ownershipRepository.findAll();
    assertEquals(4, ownerships.size());
  }

  @Test
  public void canDeleteOwnership() {
    client.delete().uri(BASE + "/delete/1").exchange().expectStatus().isOk();

    Ownership ownerships = ownershipRepository.findById(1L).orElse(null);
    assertNull(ownerships);
  }

  @AfterEach
  public void tearDown() {
    ownershipRepository.deleteAll();
  }
}
