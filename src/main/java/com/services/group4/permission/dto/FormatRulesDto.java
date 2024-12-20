package com.services.group4.permission.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class FormatRulesDto {
  private boolean spaceBeforeColon;
  private boolean spaceAfterColon;
  private boolean equalSpaces;
  private Integer printLineBreaks;
  private Integer indentSize;

  public FormatRulesDto() {}
}
