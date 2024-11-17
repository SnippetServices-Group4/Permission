package com.services.group4.permission.user;

import static org.junit.jupiter.api.Assertions.*;

// @SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
// @ExtendWith(SpringExtension.class)
// @ActiveProfiles(value = "test")
// @AutoConfigureWebTestClient
public class OwnershipRequestE2ETests {
  //  @BeforeAll
  //  public static void setupEnv() {
  //    DotenvConfig.loadEnv();
  //  }
  //
  //  @Autowired private WebTestClient client;
  //
  //  @Autowired private OwnershipRepository ownershipRepository;
  //
  //  private final String BASE = "/ownership";
  //
  //  @BeforeEach
  //  public void setup() {
  //    ownershipRepository.saveAll(OwnershipFixtures.all());
  //  }
  //
  //  @Test
  //  public void canGetAllOwnership() {
  //    client
  //        .get()
  //        .uri(BASE)
  //        .exchange()
  //        .expectStatus()
  //        .isOk()
  //        .expectBodyList(Ownership.class)
  //        .hasSize(3);
  //  }
  //
  //  @Test
  //  public void canGetOwnershipByUserId() {
  //    client
  //        .get()
  //        .uri(BASE + "/user/1")
  //        .exchange()
  //        .expectStatus()
  //        .isOk()
  //        .expectBody(Ownership.class)
  //        .value(ownership -> assertEquals(1L, ownership.getUserId()));
  //  }
  //
  //  @Test
  //  public void canGetOwnershipBySnippetId() {
  //    client
  //        .get()
  //        .uri(BASE + "/snippet/1")
  //        .exchange()
  //        .expectStatus()
  //        .isOk()
  //        .expectBody(Ownership.class)
  //        .value(ownership -> assertEquals(1L, ownership.getSnippetId()));
  //  }
  //
  //  @Test
  //  public void canCreateOwnership() {
  //    Ownership newOwnerships = new Ownership("4L", 4L);
  //    client
  //        .post()
  //        .uri(BASE + "/create")
  //        .bodyValue(newOwnerships)
  //        .exchange()
  //        .expectStatus()
  //        .isCreated();
  //
  //    List<Ownership> ownerships = ownershipRepository.findAll();
  //    assertEquals(4, ownerships.size());
  //  }
  //
  //  @Test
  //  public void canDeleteOwnership() {
  //    client.delete().uri(BASE + "/delete/1").exchange().expectStatus().isOk();
  //
  //    Ownership ownerships = ownershipRepository.findById(1L).orElse(null);
  //    assertNull(ownerships);
  //  }
  //
  //  // Add a test for the case where the ownership does not exist
  //
  //  @Test
  //  public void testGetOwnershipByUserId_NotFound() {
  //    client.get().uri(BASE + "/user/999").exchange().expectStatus().isNotFound();
  //  }
  //
  //  @Test
  //  public void testGetOwnershipBySnippetId_NotFound() {
  //    client.get().uri(BASE + "/snippet/999").exchange().expectStatus().isNotFound();
  //  }
  //
  //  @AfterEach
  //  public void tearDown() {
  //    ownershipRepository.deleteAll();
  //  }
}
