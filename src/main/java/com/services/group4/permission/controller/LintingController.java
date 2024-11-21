package com.services.group4.permission.controller;

import com.services.group4.permission.common.DataTuple;
import com.services.group4.permission.dto.LintRulesDto;
import com.services.group4.permission.dto.ResponseDto;
import com.services.group4.permission.dto.UpdateRulesRequestDto;
import com.services.group4.permission.model.LintConfig;
import com.services.group4.permission.service.LintingService;
import com.services.group4.permission.service.OwnershipService;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/linting")
public class LintingController {

  private final LintingService lintingService;
  private final OwnershipService ownershipService;

  public LintingController(LintingService lintingService, OwnershipService ownershipService) {
    this.lintingService = lintingService;
    this.ownershipService = ownershipService;
  }

  @GetMapping("/rules")
  public ResponseEntity<ResponseDto<LintRulesDto>> getConfig(
      @RequestHeader("userId") String userId) {
    log.info("Getting linting rules for user with id{}", userId);
    Optional<LintRulesDto> config = lintingService.getConfig(userId);

    log.info("Returning response for user with id{}", userId);
    return config
        .map(
            c ->
                new ResponseEntity<>(
                    new ResponseDto<>(
                        "Config of user " + userId + " found.", new DataTuple<>("config", c)),
                    HttpStatus.OK))
        .orElseGet(
            () ->
                new ResponseEntity<>(
                    new ResponseDto<>("User doesn't exist.", null), HttpStatus.NOT_FOUND));
  }

  @PostMapping("/update/rules")
  public ResponseEntity<ResponseDto<LintConfig>> updateRulesAndLint(
      @RequestBody UpdateRulesRequestDto<LintRulesDto> req,
      @RequestHeader("userId") String userId) {
    try {
      log.info("Updating linting rules for user with id{}", userId);
      LintConfig rules = lintingService.updateRules(userId, req);

      log.info("Getting snippets id for user with id{}", userId);
      Optional<List<Long>> snippetsId = ownershipService.findSnippetIdsByUserId(userId);

      DataTuple<LintConfig> lintRules = new DataTuple<>("lintRules", rules);

      if (snippetsId.isEmpty()) {
        log.info("No snippets to lint found for user with id {}", userId);
        return new ResponseEntity<>(
            new ResponseDto<>("Updated lint rules, no snippets to lint", lintRules), HttpStatus.OK);
      }

      log.info("Asynchronously linting snippets for user with id{}", userId);
      Optional<Integer> totalToLintSnippets =
          lintingService.asyncLint(snippetsId.get(), req.rules());

      return totalToLintSnippets
          .map(
              integer ->
                  new ResponseEntity<>(
                      new ResponseDto<>(
                          "Updated lint rules, " + integer + " snippets in queue", lintRules),
                      HttpStatus.OK))
          .orElseGet(
              () ->
                  new ResponseEntity<>(
                      new ResponseDto<>(
                          "Updated lint rules, but something occurred during asynchronous linting",
                          lintRules),
                      HttpStatus.INTERNAL_SERVER_ERROR));
    } catch (Exception e) {
      log.error("Could not update linting rules for user with id{}", userId);
      return new ResponseEntity<>(
          new ResponseDto<>("Could not update linting rules.", null),
          HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
}
