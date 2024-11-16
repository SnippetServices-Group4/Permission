package com.services.group4.permission.service;

import com.services.group4.permission.dto.LintRulesDto;
import com.services.group4.permission.dto.UpdateRulesRequestDto;
import com.services.group4.permission.model.LintConfig;
import com.services.group4.permission.repository.LintConfigRepository;
import com.services.group4.permission.service.async.LintEventProducer;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LintingService {
  private final LintConfigRepository lintConfigRepository;
  private final LintEventProducer lintEventProducer;

  public LintingService(LintConfigRepository lintConfigRepository, LintEventProducer lintEventProducer) {
    this.lintConfigRepository = lintConfigRepository;
    this.lintEventProducer = lintEventProducer;
  }

  public LintConfig updateRules(UpdateRulesRequestDto req) {
    String userId = req.userId();
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

  public Optional<Integer> asyncLint(List<Long> snippetsId, LintConfig config) {
    int i = 0;

    try {
      for (Long snippetId : snippetsId) {
        System.out.println("Producing linting event for snippet: " + snippetId);

        lintEventProducer.publishEvent(snippetId, config);

        i++;
      }
    } catch (Exception e) {
      return Optional.empty();
    }

    return Optional.of(i);
  }
}
