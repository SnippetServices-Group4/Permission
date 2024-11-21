package com.services.group4.permission.service;

import com.services.group4.permission.common.DataTuple;
import com.services.group4.permission.common.FullResponse;
import com.services.group4.permission.dto.LintRulesDto;
import com.services.group4.permission.dto.ResponseDto;
import com.services.group4.permission.dto.UpdateRulesRequestDto;
import com.services.group4.permission.model.LintConfig;
import com.services.group4.permission.repository.LintConfigRepository;
import com.services.group4.permission.service.async.LintEventProducer;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class LintingService {
  private final LintConfigRepository lintConfigRepository;
  private final LintEventProducer lintEventProducer;
  private final OwnershipService ownershipService;

  public LintingService(
          LintConfigRepository lintConfigRepository, LintEventProducer lintEventProducer, OwnershipService ownershipService) {
    this.lintConfigRepository = lintConfigRepository;
    this.lintEventProducer = lintEventProducer;
      this.ownershipService = ownershipService;
  }

  public LintRulesDto getConfig(String userId) {
    Optional<LintConfig> config = lintConfigRepository.findLintConfigByUserId(userId);

    if (config.isEmpty()) {
      return setDefaultRules(userId);
    } else {
      LintConfig rules = config.get();
      return toLintRulesDto(rules);
    }
  }

  private LintRulesDto setDefaultRules(String userId) {
    LintConfig defaultConfig = new LintConfig(userId, "camelCase", true, true);
    lintConfigRepository.save(defaultConfig);
    return toLintRulesDto(defaultConfig);
  }

  private LintRulesDto toLintRulesDto(LintConfig rules) {
    return new LintRulesDto(
        rules.getWritingConventionName(),
        rules.isPrintLnAcceptsExpressions(),
        rules.isReadInputAcceptsExpressions());
  }

  public ResponseEntity<ResponseDto<LintConfig>> updateAndLint(String userId, UpdateRulesRequestDto<LintRulesDto> req) {
    try {
      System.out.println("Updating rules");
      LintConfig rules = updateRules(userId, req);

      System.out.println("Getting snippets");
      Optional<List<Long>> snippetsId = ownershipService.findSnippetIdsByUserId(userId);

      if (snippetsId.isEmpty()) {
        return FullResponse.create(
                "Updated lint rules, no snippets to lint",
                "lintRules",
                null,
                HttpStatus.OK);
      }

      Optional<Integer> totalToLintSnippets = asyncLint(snippetsId.get(), req.rules());
        return totalToLintSnippets.map(value -> FullResponse.create(
                "Updated lint rules, " + value + " snippets in queue",
                "lintRules",
                rules,
                HttpStatus.OK)).orElseGet(() -> FullResponse.create(
                "Updated lint rules, but something occurred during asynchronous linting",
                "lintRules",
                rules,
                HttpStatus.INTERNAL_SERVER_ERROR));
    } catch (Exception e) {
      return FullResponse.create(
              "Error updating rules and linting",
              "lintRules",
              null,
              HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  public LintConfig updateRules(String userId, UpdateRulesRequestDto<LintRulesDto> req) {
    LintRulesDto rules = req.rules();

    System.out.println("Updating rules for user: " + userId);
    System.out.println("Rules: " + rules);

    Optional<LintConfig> config = lintConfigRepository.findLintConfigByUserId(userId);

    if (config.isPresent()) {
      LintConfig updatedConfig = config.get();

      updatedConfig.setWritingConventionName(rules.getWritingConventionName());
      updatedConfig.setPrintLnAcceptsExpressions(rules.isPrintLnAcceptsExpressions());
      updatedConfig.setReadInputAcceptsExpressions(rules.isReadInputAcceptsExpressions());

      return lintConfigRepository.save(updatedConfig);
    } else {
      LintConfig newConfig =
          new LintConfig(
              userId,
              rules.getWritingConventionName(),
              rules.isPrintLnAcceptsExpressions(),
              rules.isReadInputAcceptsExpressions());
      return lintConfigRepository.save(newConfig);
    }
  }

  public Optional<Integer> asyncLint(List<Long> snippetsId, LintRulesDto config) {
    int i = 0;

    try {
      for (Long snippetId : snippetsId) {
        System.out.println("Producing linting event for snippet: " + snippetId);
        lintEventProducer.publishEvent(snippetId, config);

        i++;
      }
      return Optional.of(i);
    } catch (Exception e) {
      throw new RuntimeException();
    }
  }
}
