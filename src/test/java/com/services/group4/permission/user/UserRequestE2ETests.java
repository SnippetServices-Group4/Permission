package com.services.group4.permission.user;

import static org.junit.jupiter.api.Assertions.*;

// @SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
// @ExtendWith(SpringExtension.class)
// @ActiveProfiles(value = "test")
// @AutoConfigureWebTestClient
public class UserRequestE2ETests {
  //  @BeforeAll
  //  public static void setupEnv() {
  //    DotenvConfig.loadEnv();
  //  }
  //
  //  @Autowired private WebTestClient client;
  //
  //  @Autowired private UserRepository userRepository;
  //
  //  private final String BASE = "/user";
  //
  //  @BeforeEach
  //  public void setup() {
  //    userRepository.saveAll(UserFixtures.all());
  //  }
  //
  //  @Test
  //  public void canGetAllUsers() {
  //    client
  //        .get()
  //        .uri(BASE)
  //        .exchange()
  //        .expectStatus()
  //        .isOk()
  //        .expectBodyList(SnippetUser.class)
  //        .hasSize(3);
  //  }
  //
  //  @Test
  //  public void canGetUserById() {
  //    String userId = userRepository.findByUsername("John Doe").orElseThrow().getUserID();
  //    System.out.println(userId);
  //    client
  //        .get()
  //        .uri(BASE + "/{userId}", userId)
  //        .exchange()
  //        .expectStatus()
  //        .isOk()
  //        .expectBody(SnippetUser.class)
  //        .value(user -> assertEquals("John Doe", user.getUsername()));
  //  }
  //
  //  @Test
  //  public void canCreateUser() {
  //    SnippetUser newUser = new SnippetUser("new_user", "new_password", "new.user@example.com");
  //    client.post().uri(BASE +
  // "/register").bodyValue(newUser).exchange().expectStatus().isCreated();
  //
  //    List<SnippetUser> users = userRepository.findAll();
  //    assertEquals(4, users.size());
  //  }
  //
  //  @Test
  //  public void canLoginUser_Success() {
  //    SnippetUser loginUser = new SnippetUser("1L", "John Doe", "user1", "john.doe@example.com");
  //
  //    client
  //        .post()
  //        .uri(BASE + "/login")
  //        .bodyValue(loginUser)
  //        .exchange()
  //        .expectStatus()
  //        .isOk()
  //        .expectBody(String.class)
  //        .isEqualTo("Login successful");
  //  }
  //
  //  @Test
  //  public void canUpdateUser() {
  //    String userId = userRepository.findByUsername("John Doe").orElseThrow().getUserID();
  //    System.out.println(userId);
  //
  //    SnippetUser updatedUser =
  //        new SnippetUser("updated_user", "updated_password", "updated.user@example.com");
  //    client
  //        .put()
  //        .uri(BASE + "/update/{userId}", userId)
  //        .bodyValue(updatedUser)
  //        .exchange()
  //        .expectStatus()
  //        .isOk();
  //
  //    SnippetUser user = userRepository.findById(userId).orElse(null);
  //    System.out.println("user updated: " + user);
  //    assertNotNull(user);
  //    assertEquals("updated_user", user.getUsername());
  //  }
  //
  //  @Test
  //  public void canDeleteUser() {
  //    client.delete().uri(BASE + "/delete/1").exchange().expectStatus().isOk();
  //
  //    SnippetUser user = userRepository.findById("1L").orElse(null);
  //    assertNull(user);
  //  }
  //
  //  // Add this test to test the case when the user is not found
  //  @Test
  //  public void testGetReaderByUserId_NotFound() {
  //    client.get().uri(BASE + "/user/999").exchange().expectStatus().isNotFound();
  //  }
  //
  //  @Test
  //  public void canLoginUser_Failure() {
  //    SnippetUser loginUser = new SnippetUser("John Doe", "wrong_password", null);
  //    client
  //        .post()
  //        .uri(BASE + "/login")
  //        .bodyValue(loginUser)
  //        .exchange()
  //        .expectStatus()
  //        .isUnauthorized()
  //        .expectBody(String.class)
  //        .isEqualTo("Invalid username or password");
  //  }
  //
  //  @AfterEach
  //  public void tearDown() {
  //    userRepository.deleteAll();
  //  }
}
