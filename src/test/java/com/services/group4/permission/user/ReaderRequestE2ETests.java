package com.services.group4.permission.user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.services.group4.permission.mock.ReaderFixtures;
import com.services.group4.permission.model.Reader;
import com.services.group4.permission.repository.ReaderRepository;
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
public class ReaderRequestE2ETests {

  @Autowired private WebTestClient client;

  @Autowired private ReaderRepository readerRepository;

  private final String BASE = "/reader";

  @BeforeEach
  public void setup() {
    readerRepository.saveAll(ReaderFixtures.all());
  }

  @Test
  public void canGetAllReader() {
    client.get().uri(BASE).exchange().expectStatus().isOk().expectBodyList(Reader.class).hasSize(3);
  }

  @Test
  public void canGetReaderByUserId() {
    client
        .get()
        .uri(BASE + "/user/1")
        .exchange()
        .expectStatus()
        .isOk()
        .expectBody(Reader.class)
        .value(reader -> assertEquals(1L, reader.getUserId()));
  }

  @Test
  public void canGetReaderBySnippetId() {
    client
        .get()
        .uri(BASE + "/snippet/1")
        .exchange()
        .expectStatus()
        .isOk()
        .expectBody(Reader.class)
        .value(reader -> assertEquals(1L, reader.getSnippetId()));
  }

  @Test
  public void canCreateReader() {
    Reader reader = new Reader(4L, 4L);
    client.post().uri(BASE + "/create").bodyValue(reader).exchange().expectStatus().isCreated();

    List<Reader> readers = readerRepository.findAll();
    assertEquals(4, readers.size());
  }

  @Test
  public void canDeleteReader() {
    client.delete().uri(BASE + "/delete/1").exchange().expectStatus().isOk();

    Reader reader = readerRepository.findById(1L).orElse(null);
    assertNull(reader);
  }

  @AfterEach
  public void tearDown() {
    readerRepository.deleteAll();
  }
}
