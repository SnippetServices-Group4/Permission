package com.services.group4.permission.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class UpdateRulesRequestDtoTests {
  @Test
  void testUpdateRulesRequestDto() {
    FormatRulesDto rulesDto = new FormatRulesDto();
    UpdateRulesRequestDto<FormatRulesDto> requestDto = new UpdateRulesRequestDto<>(rulesDto);

    assertNotNull(requestDto);
    assertEquals(rulesDto, requestDto.rules());
  }

}
