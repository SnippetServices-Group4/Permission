package com.services.group4.permission.model;

import jakarta.persistence.*;

@Entity
public class Ownership {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long OwnershipID;

  @Column(nullable = false)
  private Long userId;

  @Column(nullable = false, unique = true)
  private Long snippetId;

  public Ownership(Long userId, Long snippetId) {
      this.userId = userId;
      this.snippetId = snippetId;
  }

  public Ownership() {
  }

  public Long getId() {
    return OwnershipID;
  }

  public Long getUserId() {
    return userId;
  }

  public Long getSnippetId() {
    return snippetId;
  }
}