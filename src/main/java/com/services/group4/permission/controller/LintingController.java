package com.services.group4.permission.controller;

import com.services.group4.permission.dto.UpdateRulesRequestDto;
import com.services.group4.permission.model.LintConfig;
import com.services.group4.permission.service.LintingService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/linting")
public class LintingController {
  private final LintingService lintingService;

  public LintingController(LintingService lintingService) {
    this.lintingService = lintingService;
  }

  @PostMapping("/update/rules")
  public void updateRulesAndLint(@RequestBody UpdateRulesRequestDto req) {
    LintConfig config = lintingService.updateRules(req);

    String message
  }
}
