package com.services.group4.permission.controller;

import com.services.group4.permission.common.DataTuple;
import com.services.group4.permission.dto.LintRulesDto;
import com.services.group4.permission.dto.ResponseDto;
import com.services.group4.permission.dto.UpdateRulesRequestDto;
import com.services.group4.permission.service.LintingService;
import com.services.group4.permission.service.OwnershipService;
import java.util.List;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
  public ResponseEntity<ResponseDto<List<Long>>> updateRulesAndLint(
      @RequestBody UpdateRulesRequestDto<LintRulesDto> req,
      @RequestHeader("userId") String userId) {
    try {
      System.out.println("Updating rules");
      lintingService.updateRules(userId, req);

      System.out.println("Getting snippets");
      Optional<List<Long>> snippetsId = ownershipService.findSnippetIdsByUserId(userId);

      Optional<Integer> snippetsInQueue = Optional.empty();

      if (snippetsId.isPresent()) {
        System.out.println("Linting snippets");
        snippetsInQueue = lintingService.asyncLint(snippetsId.get(), req.rules());
      }

      String message =
          snippetsInQueue
              .map(i -> "Linting of " + i + " snippets in progress.")
              .orElse("No snippets to lint");

      List<Long> snippetsIds = snippetsId.orElse(List.of());

      return new ResponseEntity<>(
          new ResponseDto<>(message, new DataTuple<>("snippetsIds", snippetsIds)), HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
}
