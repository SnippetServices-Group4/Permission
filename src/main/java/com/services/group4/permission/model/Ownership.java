package com.services.group4.permission.model;

import jakarta.persistence.*;

@Entity
public class Ownership {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long ownershipID;

  @Column(nullable = false)
  private Long userId;

  @Column(nullable = false, unique = true)
  private Long snippetId;

  // Added for testing purposes
  public Ownership(Long userId, Long snippetId) {
    this.userId = userId;
    this.snippetId = snippetId;
  }

  public Ownership() {}

  public Long getUserId() {
    return userId;
  }

  public Long getSnippetId() {
    return snippetId;
  }

  public Long getOwnershipID() {
    return ownershipID;
  }
}
