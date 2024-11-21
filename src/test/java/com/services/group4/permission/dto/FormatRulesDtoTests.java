package com.services.group4.permission.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class FormatRulesDtoTests {
  @Test
  void testFormatRulesDto() {
    FormatRulesDto formatRulesDto = new FormatRulesDto(true, true, true, 1, 4);

    assertNotNull(formatRulesDto);
    assertTrue(formatRulesDto.isSpaceBeforeColon());
    assertTrue(formatRulesDto.isSpaceAfterColon());
    assertTrue(formatRulesDto.isEqualSpaces());
    assertEquals(Integer.valueOf(1), formatRulesDto.getPrintLineBreaks());
    assertEquals(Integer.valueOf(4), formatRulesDto.getIndentSize());
  }

}
