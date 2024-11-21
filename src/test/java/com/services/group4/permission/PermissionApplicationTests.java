package com.services.group4.permission;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

// TODO: fix this test
@SpringBootTest
@Disabled("Disabled for bd")
class PermissionApplicationTests {
  @BeforeAll
  public static void setupEnv() {
    DotenvConfig.loadEnv();
  }

  @Test
  void contextLoads() {}
}
