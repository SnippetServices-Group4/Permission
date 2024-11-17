package com.services.group4.permission.service;

import com.services.group4.permission.dto.FormatRulesDto;
import com.services.group4.permission.dto.UpdateRulesRequestDto;
import com.services.group4.permission.model.FormatConfig;
import com.services.group4.permission.repository.FormatConfigRepository;
import com.services.group4.permission.service.async.FormatEventProducer;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class FormattingService {
  private final FormatConfigRepository formatConfigRepository;
  private final FormatEventProducer formatEventProducer;

  public FormattingService(
      FormatConfigRepository formatConfigRepository, FormatEventProducer formatEventProducer) {
    this.formatConfigRepository = formatConfigRepository;
    this.formatEventProducer = formatEventProducer;
  }

  public FormatConfig updateRules(String userId, UpdateRulesRequestDto<FormatRulesDto> req) {
    FormatRulesDto rules = req.rules();

    System.out.println("Updating rules for user: " + userId);
    System.out.println("Rules: " + rules);

    Optional<FormatConfig> config = formatConfigRepository.findFormatConfigByUserId(userId);

    if (config.isPresent()) {
      FormatConfig updatedConfig = config.get();

      updatedConfig.setSpaceBeforeColon(rules.isSpaceBeforeColon());
      updatedConfig.setSpaceAfterColon(rules.isSpaceAfterColon());
      updatedConfig.setEqualSpaces(rules.isEqualSpaces());
      updatedConfig.setPrintLineBreaks(rules.getPrintLineBreaks());
      updatedConfig.setIndentSize(rules.getIndentSize());

      return formatConfigRepository.save(updatedConfig);
    } else {
      FormatConfig newConfig =
          new FormatConfig(
              userId,
              rules.isSpaceBeforeColon(),
              rules.isSpaceAfterColon(),
              rules.isEqualSpaces(),
              rules.getPrintLineBreaks(),
              rules.getIndentSize());
      return formatConfigRepository.save(newConfig);
    }
  }

  public Optional<Integer> asyncFormat(List<Long> snippetsId, FormatConfig config) {
    int i = 0;

    try {
      for (Long snippetId : snippetsId) {
        System.out.println("Producing formatting event for snippet: " + snippetId);

        formatEventProducer.publishEvent(snippetId, config);

        i++;
      }
    } catch (Exception e) {
      return Optional.empty();
    }

    return Optional.of(i);
  }
}
