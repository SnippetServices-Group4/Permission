package com.services.group4.permission.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
public class FormatConfig {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, unique = true)
  private String userId;

  @Column(nullable = false)
  private boolean spaceBeforeColon;

  @Column(nullable = false)
  private boolean spaceAfterColon;

  @Column(nullable = false)
  private boolean equalSpaces;

  @Column(nullable = false)
  private Integer printLineBreaks;

  @Column(nullable = false)
  private Integer indentSize;

  public FormatConfig() {}

  public FormatConfig(
      String userId,
      boolean spaceBeforeColon,
      boolean spaceAfterColon,
      boolean equalSpaces,
      Integer printLineBreaks,
      Integer indentSize) {
    this.userId = userId;
    this.spaceBeforeColon = spaceBeforeColon;
    this.spaceAfterColon = spaceAfterColon;
    this.equalSpaces = equalSpaces;
    this.printLineBreaks = printLineBreaks;
    this.indentSize = indentSize;
  }
}
