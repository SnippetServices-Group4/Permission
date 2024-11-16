package com.services.group4.permission.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
public class LintConfig {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, unique = true)
  private String userId;

  @Column(nullable = false)
  private String writingConventionName;

  @Column(nullable = false)
  private boolean printLnAcceptsExpressions;

  @Column(nullable = false)
  private boolean readInputAcceptsExpressions;

  public LintConfig() {}

  public LintConfig(
      String userId,
      String writingConventionName,
      boolean printLnAcceptsExpressions,
      boolean readInputAcceptsExpressions) {
    this.userId = userId;
    this.writingConventionName = writingConventionName;
    this.printLnAcceptsExpressions = printLnAcceptsExpressions;
    this.readInputAcceptsExpressions = readInputAcceptsExpressions;
  }
}
