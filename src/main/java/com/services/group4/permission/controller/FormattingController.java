package com.services.group4.permission.controller;

import com.services.group4.permission.common.FullResponse;
import com.services.group4.permission.dto.FormatRulesDto;
import com.services.group4.permission.dto.ResponseDto;
import com.services.group4.permission.dto.UpdateRulesRequestDto;
import com.services.group4.permission.service.FormattingService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/formatting")
public class FormattingController {
  private final FormattingService formattingService;

  public FormattingController(FormattingService formattingService) {
    this.formattingService = formattingService;
  }

  @GetMapping("/rules")
  public ResponseEntity<ResponseDto<FormatRulesDto>> getConfig(
      @RequestHeader("userId") String userId) {
    FormatRulesDto config = formattingService.getConfig(userId);
    return FullResponse.create(
        "Config of user " + userId + " found.", "config", config, HttpStatus.OK);
  }

  @PostMapping("/update/rules")
  public ResponseEntity<ResponseDto<List<Long>>> updateRulesAndFormat(
      @RequestBody UpdateRulesRequestDto<FormatRulesDto> req,
      @RequestHeader("userId") String userId) {
    try {
      return formattingService.updateRules(userId, req);
    } catch (Exception e) {
      return FullResponse.create(
          "Error updating rules and formatting",
          "snippetIds",
          null,
          HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @PostMapping("/run/{snippetId}")
  public ResponseEntity<ResponseDto<Object>> runFormatting(
      @RequestHeader("userId") String userId, @PathVariable Long snippetId) {
    return formattingService.runFormatting(snippetId, userId);
  }
}
