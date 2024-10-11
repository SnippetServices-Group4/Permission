package com.services.group4.permission.model;

import jakarta.persistence.*;

@Entity
public class Reader {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long readerID;

  @Column(nullable = false)
  private Long userId;

  @Column(nullable = false, unique = true)
  private Long snippetId;

  // Added for testing purposes
  public Reader(Long userId, Long snippetId) {
    this.userId = userId;
    this.snippetId = snippetId;
  }

  public Reader() {
  }

  public Long getUserId() {
    return userId;
  }

  public Long getSnippetId() {
    return snippetId;
  }

  public Long getReaderID() {
    return readerID;
  }
}