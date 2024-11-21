package com.services.group4.permission.controller;

import com.services.group4.permission.common.FullResponse;
import com.services.group4.permission.dto.LintRulesDto;
import com.services.group4.permission.dto.ResponseDto;
import com.services.group4.permission.dto.UpdateRulesRequestDto;
import com.services.group4.permission.model.LintConfig;
import com.services.group4.permission.service.LintingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/linting")
public class LintingController {
  private final LintingService lintingService;

  public LintingController(LintingService lintingService) {
    this.lintingService = lintingService;
  }

  @GetMapping("/rules")
  public ResponseEntity<ResponseDto<LintRulesDto>> getConfig(
      @RequestHeader("userId") String userId) {
    try {
      LintRulesDto config = lintingService.getConfig(userId);
      return FullResponse.create(
              "Config of user " + userId + " found.", "config", config, HttpStatus.OK);
    } catch (Exception e) {
      return FullResponse.create(
              "Error getting linting rules", "config", null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @PostMapping("/update/rules")
  public ResponseEntity<ResponseDto<LintConfig>> updateRulesAndLint(
      @RequestBody UpdateRulesRequestDto<LintRulesDto> req,
      @RequestHeader("userId") String userId) {
    return lintingService.updateAndLint(userId, req);
  }
}
