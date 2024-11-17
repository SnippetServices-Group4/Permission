package com.services.group4.permission.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LintRulesDto {
  private String writingConventionName;
  private boolean printLnAcceptsExpressions;
  private boolean readInputAcceptsExpressions;

  public LintRulesDto() {}
}
