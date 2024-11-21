package com.services.group4.permission.dto;

import com.services.group4.permission.dto.snippet.Language;
import com.services.group4.permission.dto.snippet.SnippetResponseDto;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SnippetResponseDtoTests {
  @Test
  void testSnippetResponseDto_ConstructorValidation() {
    assertDoesNotThrow(() -> new SnippetResponseDto(1L, "user123", "user123", new Language("java", "1.8", "java")));
  }
}
