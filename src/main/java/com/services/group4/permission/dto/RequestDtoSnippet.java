package com.services.group4.permission.dto;

public record RequestDtoSnippet(String userId, Long snippetId) {
  public RequestDtoSnippet {
    if (userId == null || snippetId == null) {
      throw new IllegalArgumentException("userId and snippetId must not be null");
    }
  }
}
