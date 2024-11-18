package com.services.group4.permission.dto;

public record RequestDtoShareSnippet(Long snippetId, String targetUserId) {
  public RequestDtoShareSnippet {
    if (snippetId == null) {
      throw new IllegalArgumentException("userId and snippetId must not be null");
    }
  }
}
