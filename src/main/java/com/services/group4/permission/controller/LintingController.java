package com.services.group4.permission.controller;

import com.services.group4.permission.dto.UpdateRulesRequestDto;
import com.services.group4.permission.model.LintConfig;
import com.services.group4.permission.service.LintingService;
import com.services.group4.permission.service.OwnershipService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/linting")
public class LintingController {
  private final LintingService lintingService;
  private final OwnershipService ownershipService;

  public LintingController(LintingService lintingService, OwnershipService ownershipService) {
    this.lintingService = lintingService;
    this.ownershipService = ownershipService;
  }

  @PostMapping("/update/rules")
  public ResponseEntity<String> updateRulesAndLint(@RequestBody UpdateRulesRequestDto req) {
    try {
      System.out.println("Updating rules");
      LintConfig config = lintingService.updateRules(req);

      System.out.println("Getting snippets");
      List<Long> snippetsId = ownershipService.findSnippetIdsByUserId(config.getUserId());

      System.out.println("Linting snippets");
      Optional<Integer> snippetsInQueue = lintingService.asyncLint(snippetsId, config);

      return new ResponseEntity<>("Linting " + snippetsInQueue + " snippets", HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
}
