package com.services.group4.permission.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.services.group4.permission.dto.LintRulesDto;
import com.services.group4.permission.dto.UpdateRulesRequestDto;
import com.services.group4.permission.model.LintConfig;
import com.services.group4.permission.repository.LintConfigRepository;
import com.services.group4.permission.service.async.LintEventProducer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

class LintingServiceTest {

  @Mock
  private LintConfigRepository lintConfigRepository;

  @Mock
  private LintEventProducer lintEventProducer;

  @InjectMocks
  private LintingService lintingService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void getConfig_shouldReturnDefaultRulesWhenConfigNotPresent() {
    String userId = "user123";
    when(lintConfigRepository.findLintConfigByUserId(userId)).thenReturn(Optional.empty());

    LintRulesDto defaultRules = lintingService.getConfig(userId);

    assertEquals("camelCase", defaultRules.getWritingConventionName());
    assertTrue(defaultRules.isPrintLnAcceptsExpressions());
    assertTrue(defaultRules.isReadInputAcceptsExpressions());
    verify(lintConfigRepository).save(any(LintConfig.class));
  }

  @Test
  void getConfig_shouldReturnExistingRulesWhenConfigPresent() {
    String userId = "user123";
    LintConfig existingConfig = new LintConfig(userId, "snake_case", false, true);
    when(lintConfigRepository.findLintConfigByUserId(userId)).thenReturn(Optional.of(existingConfig));

    LintRulesDto rules = lintingService.getConfig(userId);

    assertEquals("snake_case", rules.getWritingConventionName());
    assertFalse(rules.isPrintLnAcceptsExpressions());
    assertTrue(rules.isReadInputAcceptsExpressions());
    verify(lintConfigRepository, never()).save(any(LintConfig.class));
  }

  @Test
  void updateRules_shouldUpdateExistingConfig() {
    String userId = "user123";
    LintRulesDto newRules = new LintRulesDto("PascalCase", false, false);
    UpdateRulesRequestDto<LintRulesDto> request = new UpdateRulesRequestDto<>(newRules);
    LintConfig existingConfig = new LintConfig(userId, "camelCase", true, true);

    when(lintConfigRepository.findLintConfigByUserId(userId)).thenReturn(Optional.of(existingConfig));
    when(lintConfigRepository.save(any(LintConfig.class))).thenAnswer(invocation -> invocation.getArgument(0));

    LintConfig updatedConfig = lintingService.updateRules(userId, request);

    assertEquals("PascalCase", updatedConfig.getWritingConventionName());
    assertFalse(updatedConfig.isPrintLnAcceptsExpressions());
    assertFalse(updatedConfig.isReadInputAcceptsExpressions());
    verify(lintConfigRepository).save(existingConfig);
  }

  @Test
  void updateRules_shouldCreateNewConfigWhenNotPresent() {
    String userId = "user123";
    LintRulesDto newRules = new LintRulesDto("PascalCase", false, true);
    UpdateRulesRequestDto<LintRulesDto> request = new UpdateRulesRequestDto<>(newRules);

    when(lintConfigRepository.findLintConfigByUserId(userId)).thenReturn(Optional.empty());
    when(lintConfigRepository.save(any(LintConfig.class))).thenAnswer(invocation -> invocation.getArgument(0));

    LintConfig newConfig = lintingService.updateRules(userId, request);

    assertEquals("PascalCase", newConfig.getWritingConventionName());
    assertFalse(newConfig.isPrintLnAcceptsExpressions());
    assertTrue(newConfig.isReadInputAcceptsExpressions());
    verify(lintConfigRepository).save(any(LintConfig.class));
  }

  @Test
  void asyncLint_shouldPublishEventsAndReturnCount() {
    List<Long> snippetIds = List.of(1L, 2L, 3L);
    LintRulesDto rules = new LintRulesDto("camelCase", true, true);

    Optional<Integer> result = lintingService.asyncLint(snippetIds, rules);

    assertTrue(result.isPresent());
    assertEquals(3, result.get());
    verify(lintEventProducer, times(3)).publishEvent(anyLong(), eq(rules));
  }
}

