package com.services.group4.permission.mock;

import com.services.group4.permission.model.SnippetUser;
import java.util.Arrays;
import java.util.List;

public class UserFixtures {
  public static List<SnippetUser> all() {
    return Arrays.asList(
        new SnippetUser("1L", "John Doe", "user1", "john.doe@example.com"),
        new SnippetUser("2L", "Jane Doe", "user2", "jane.doe@example.com"),
        new SnippetUser("3L", "Jim Beam", "user3", "jim.beam@example.com"));
  }
}
