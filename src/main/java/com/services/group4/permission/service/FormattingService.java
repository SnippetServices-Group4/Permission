package com.services.group4.permission.service;

import com.services.group4.permission.common.DataTuple;
import com.services.group4.permission.common.FullResponse;
import com.services.group4.permission.dto.FormatRulesDto;
import com.services.group4.permission.dto.ResponseDto;
import com.services.group4.permission.dto.UpdateRulesRequestDto;
import com.services.group4.permission.dto.snippet.SnippetResponseDto;
import com.services.group4.permission.model.FormatConfig;
import com.services.group4.permission.repository.FormatConfigRepository;
import com.services.group4.permission.service.async.FormatEventProducer;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class FormattingService {
  private final FormatConfigRepository formatConfigRepository;
  private final FormatEventProducer formatEventProducer;
  private final ParserService parserService;
  private final SnippetService snippetService;
  private final OwnershipService ownershipService;

  @Autowired
  public FormattingService(
      FormatConfigRepository formatConfigRepository,
      FormatEventProducer formatEventProducer,
      ParserService parserService,
      SnippetService snippetService,
      OwnershipService ownershipService) {
    this.formatConfigRepository = formatConfigRepository;
    this.formatEventProducer = formatEventProducer;
    this.parserService = parserService;
    this.snippetService = snippetService;
    this.ownershipService = ownershipService;
  }

  public FormatRulesDto getConfig(String userId) {
    log.info("Getting formatting rules for user with id{}", userId);
    Optional<FormatConfig> config = formatConfigRepository.findFormatConfigByUserId(userId);

    if (config.isEmpty()) {
      log.info("No formatting rules found for user with id{}", userId);
      log.info("Using default rules to format");
      return setDefaultRules(userId);
    } else {
      log.info("Formatting rules found for user with id{}", userId);
      FormatConfig rules = config.get();
      return toFormatRulesDto(rules);
    }
  }

  private FormatRulesDto setDefaultRules(String userId) {
    FormatConfig defaultConfig = new FormatConfig(userId, false, true, true, 0, 2);
    formatConfigRepository.save(defaultConfig);
    return toFormatRulesDto(defaultConfig);
  }

  private @NotNull FormatRulesDto toFormatRulesDto(FormatConfig rules) {
    return new FormatRulesDto(
        rules.isSpaceBeforeColon(),
        rules.isSpaceAfterColon(),
        rules.isEqualSpaces(),
        rules.getPrintLineBreaks(),
        rules.getIndentSize());
  }

  public ResponseEntity<ResponseDto<List<Long>>> updateRules(
      String userId, UpdateRulesRequestDto<FormatRulesDto> req) {
    FormatRulesDto rules = req.rules();

    log.info("Fetching current formatting rules");
    Optional<FormatConfig> config = formatConfigRepository.findFormatConfigByUserId(userId);
    FormatConfig newConfig = getNewConfig(userId, config, rules);

    log.info("Updating formatting rules");
    formatConfigRepository.save(newConfig);

    Optional<List<Long>> snippetsId = ownershipService.findSnippetIdsByUserId(userId);

    Optional<Integer> snippetsInQueue = Optional.empty();

    if (snippetsId.isPresent()) {
      snippetsInQueue = asyncFormat(snippetsId.get(), req.rules());
    }

    String message =
        snippetsInQueue
            .map(i -> "Formatting of " + i + " snippets in progress.")
            .orElse("No snippets to format");

    log.info(message);
    List<Long> snippetsIds = snippetsId.orElse(List.of());
    return new ResponseEntity<>(
        new ResponseDto<>(message, new DataTuple<>("snippetsIds", snippetsIds)), HttpStatus.OK);
  }

  private static @NotNull FormatConfig getNewConfig(
      String userId, Optional<FormatConfig> config, FormatRulesDto rules) {
    FormatConfig newConfig;
    if (config.isPresent()) {
      newConfig = config.get();

      newConfig.setSpaceBeforeColon(rules.isSpaceBeforeColon());
      newConfig.setSpaceAfterColon(rules.isSpaceAfterColon());
      newConfig.setEqualSpaces(rules.isEqualSpaces());
      newConfig.setPrintLineBreaks(rules.getPrintLineBreaks());
      newConfig.setIndentSize(rules.getIndentSize());
    } else {
      newConfig =
          new FormatConfig(
              userId,
              rules.isSpaceBeforeColon(),
              rules.isSpaceAfterColon(),
              rules.isEqualSpaces(),
              rules.getPrintLineBreaks(),
              rules.getIndentSize());
    }
    return newConfig;
  }

  public Optional<Integer> asyncFormat(List<Long> snippetsId, FormatRulesDto config) {
    int i = 0;

    try {
      for (Long snippetId : snippetsId) {
        formatEventProducer.publishEvent(snippetId, config);
        i++;
      }
    } catch (Exception e) {
      return Optional.empty();
    }

    return Optional.of(i);
  }

  public ResponseEntity<ResponseDto<Object>> runFormatting(Long snippetId, String userId) {
    boolean canFormat = ownershipService.isOwner(userId, snippetId);
    if (!canFormat) {
      log.error("User with id {} doesn't have permission to format snippet with id {}", userId, snippetId);
      return FullResponse.create(
          "You don't have permission to format this snippet",
          "formattingResponse",
          null,
          HttpStatus.FORBIDDEN);
    }

    log.info("Fetching snippet with id {}", snippetId);

    ResponseEntity<ResponseDto<SnippetResponseDto>> snippetResponse =
        snippetService.getSnippetInfo(snippetId);
    if (snippetResponse.getStatusCode().equals(HttpStatus.OK)
        && Objects.requireNonNull(snippetResponse.getBody()).data() != null) {
      SnippetResponseDto snippet = snippetResponse.getBody().data().data();
      FormatRulesDto formatRules = getConfig(userId);
      return parserService.runFormatting(snippet, formatRules);
    }
    return new ResponseEntity<>(
        new ResponseDto<>(Objects.requireNonNull(snippetResponse.getBody()).message(), null),
        snippetResponse.getStatusCode());
  }
}
