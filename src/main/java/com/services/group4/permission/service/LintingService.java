package com.services.group4.permission.service;

import com.services.group4.permission.dto.LintRulesDto;
import com.services.group4.permission.dto.UpdateRulesRequestDto;
import com.services.group4.permission.model.LintConfig;
import com.services.group4.permission.repository.LintConfigRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LintingService {
  private final LintConfigRepository lintConfigRepository;

  public LintingService(LintConfigRepository lintConfigRepository) {
    this.lintConfigRepository = lintConfigRepository;
  }

  public LintConfig updateRules(UpdateRulesRequestDto req) {
    Long userId = req.userId();
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
      LintConfig newConfig = new LintConfig(userId, rules.getWritingConventionName(), rules.isPrintLnAcceptsExpressions(), rules.isReadInputAcceptsExpressions());
      return lintConfigRepository.save(newConfig);
    }
  }

  public String
}
