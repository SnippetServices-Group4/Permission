package com.services.group4.permission.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class LintRulesDtoTests {
  @Test
  void testLintRulesDto() {
    LintRulesDto lintRulesDto = new LintRulesDto("Convention1", true, false);

    assertNotNull(lintRulesDto);
    assertEquals("Convention1", lintRulesDto.getWritingConventionName());
    assertTrue(lintRulesDto.isPrintLnAcceptsExpressions());
    assertFalse(lintRulesDto.isReadInputAcceptsExpressions());
  }

}
