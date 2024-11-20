package com.services.group4.permission.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class RequestDtoSnippetTests {
  @Test
  void testRequestDtoSnippet_ConstructorValidation() {
    assertThrows(IllegalArgumentException.class, () -> new RequestDtoSnippet(null, 1L));
    assertThrows(IllegalArgumentException.class, () -> new RequestDtoSnippet("user123", null));
    assertDoesNotThrow(() -> new RequestDtoSnippet("user123", 1L));
  }

}
