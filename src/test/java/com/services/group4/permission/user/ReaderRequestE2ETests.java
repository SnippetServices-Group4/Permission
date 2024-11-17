package com.services.group4.permission.user;

// @SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
// @ExtendWith(SpringExtension.class)
// @ActiveProfiles(value = "test")
// @AutoConfigureWebTestClient
public class ReaderRequestE2ETests {
  //  @BeforeAll
  //  public static void setupEnv() {
  //    DotenvConfig.loadEnv();
  //  }
  //
  //  @Autowired private WebTestClient client;
  //
  //  @Autowired private ReaderRepository readerRepository;
  //
  //  private final String BASE = "/reader";
  //
  //  @BeforeEach
  //  public void setup() {
  //    readerRepository.saveAll(ReaderFixtures.all());
  //  }
  //
  //  @Test
  //  public void canGetAllReader() {
  //
  // client.get().uri(BASE).exchange().expectStatus().isOk().expectBodyList(Reader.class).hasSize(3);
  //  }
  //
  //  @Test
  //  public void canGetReaderByUserId() {
  //    client
  //        .get()
  //        .uri(BASE + "/user/1")
  //        .exchange()
  //        .expectStatus()
  //        .isOk()
  //        .expectBody(Reader.class)
  //        .value(reader -> assertEquals(1L, reader.getUserId()));
  //  }
  //
  //  @Test
  //  public void canGetReaderBySnippetId() {
  //    client
  //        .get()
  //        .uri(BASE + "/snippet/1")
  //        .exchange()
  //        .expectStatus()
  //        .isOk()
  //        .expectBody(Reader.class)
  //        .value(reader -> assertEquals(1L, reader.getSnippetId()));
  //  }
  //
  //  @Test
  //  public void canCreateReader() {
  //    Reader reader = new Reader("4L", 4L);
  //    client.post().uri(BASE + "/create").bodyValue(reader).exchange().expectStatus().isCreated();
  //
  //    List<Reader> readers = readerRepository.findAll();
  //    assertEquals(4, readers.size());
  //  }
  //
  //  @Test
  //  public void canDeleteReader() {
  //    client.delete().uri(BASE + "/delete/1").exchange().expectStatus().isOk();
  //
  //    Reader reader = readerRepository.findById(1L).orElse(null);
  //    assertNull(reader);
  //  }
  //
  //  // Add this test to test the case when the reader is not found
  //  @Test
  //  public void testGetReaderByUserId_NotFound() {
  //    client.get().uri(BASE + "/user/999").exchange().expectStatus().isNotFound();
  //  }
  //
  //  @Test
  //  public void testGetReaderBySnippetId_NotFound() {
  //    client.get().uri(BASE + "/snippet/999").exchange().expectStatus().isNotFound();
  //  }
  //
  //  @AfterEach
  //  public void tearDown() {
  //    readerRepository.deleteAll();
  //  }
}
