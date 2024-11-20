package com.services.group4.permission.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class RequestDtoShareSnippetTests {
  @Test
  void testRequestDtoShareSnippet_ConstructorValidation() {
    assertThrows(IllegalArgumentException.class, () -> new RequestDtoShareSnippet(null, "targetUser"));
    assertDoesNotThrow(() -> new RequestDtoShareSnippet(1L, "targetUser"));
  }

}
