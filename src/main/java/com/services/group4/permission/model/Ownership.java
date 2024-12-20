package com.services.group4.permission.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Generated;

@Generated
@Entity
public class Ownership {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long ownershipID;

  @Column(nullable = false)
  private String userId;

  @Column(nullable = false, unique = true)
  private Long snippetId;

  // Added for testing purposes
  public Ownership(String userId, Long snippetId) {
    this.userId = userId;
    this.snippetId = snippetId;
  }

  public Ownership() {}

  public String getUserId() {
    return userId;
  }

  public Long getSnippetId() {
    return snippetId;
  }

  public Long getOwnershipID() {
    return ownershipID;
  }
}
