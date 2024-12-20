package com.services.group4.permission.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Generated;

@Generated
@Entity
public class Reader {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long readerID;

  @Column(nullable = false)
  private String userId;

  @Column(nullable = false)
  private Long snippetId;

  // Added for testing purposes
  public Reader(String userId, Long snippetId) {
    this.userId = userId;
    this.snippetId = snippetId;
  }

  public Reader() {}

  public String getUserId() {
    return userId;
  }

  public Long getSnippetId() {
    return snippetId;
  }

  public Long getReaderID() {
    return readerID;
  }
}
