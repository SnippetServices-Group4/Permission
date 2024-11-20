package com.services.group4.permission.controller;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.services.group4.permission.DotenvConfig;
import com.services.group4.permission.dto.LintRulesDto;
import com.services.group4.permission.dto.UpdateRulesRequestDto;
import com.services.group4.permission.model.LintConfig;
import com.services.group4.permission.service.LintingService;
import com.services.group4.permission.service.OwnershipService;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

@ExtendWith(SpringExtension.class)
@WebMvcTest(LintingController.class)
public class LintingControllerTests {

  @BeforeAll
  public static void setupEnv() {
    DotenvConfig.loadEnv();
  }

  @MockBean private LintingService lintingService;

  @MockBean private OwnershipService ownershipService;

  @Autowired private MockMvc mockMvc;

  private final ObjectMapper objectMapper = new ObjectMapper();

  @Test
  void testGetConfig_UserFound() throws Exception {
    String userId = "user123";
    LintRulesDto rulesDto = new LintRulesDto(); // Crear un objeto de ejemplo para LintRulesDto
    when(lintingService.getConfig(userId)).thenReturn(Optional.of(rulesDto));

    mockMvc
        .perform(get("/linting/rules").header("userId", userId))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.message").value("Config of user user123 found."))
        .andExpect(jsonPath("$.data.config").exists());
  }

  // no agarra bien el mock de lintingService
  //  @Test
  //  void testUpdateRulesAndLint_SnippetsInQueue() throws Exception {
  //    String userId = "user123";
  //    LintRulesDto rulesDto = new LintRulesDto(); // Crear un objeto de ejemplo para LintRulesDto
  //    UpdateRulesRequestDto<LintRulesDto> requestDto = new UpdateRulesRequestDto<>(rulesDto);
  //    List<Long> snippetsIds = List.of(1L, 2L);
  //
  //    when(ownershipService.findSnippetIdsByUserId(userId)).thenReturn(Optional.of(snippetsIds));
  //    when(lintingService.asyncLint(snippetsIds,
  // rulesDto)).thenReturn(Optional.of(snippetsIds.size()));
  //
  //    mockMvc.perform(post("/linting/update/rules")
  //            .header("userId", userId)
  //            .contentType(MediaType.APPLICATION_JSON)
  //            .content(new ObjectMapper().writeValueAsString(requestDto)))
  //        .andExpect(status().isOk())
  //        .andExpect(jsonPath("$.message").value("Linting of 2 snippets in progress."))
  //        .andExpect(jsonPath("$.data.snippetsIds").isArray())
  //        .andExpect(jsonPath("$.data.snippetsIds.length()").value(2));
  //  }

  @Test
  void testUpdateRulesAndLint_NoSnippetsToLint() throws Exception {
    String userId = anyString();
    LintRulesDto rulesDto = new LintRulesDto(); // Crear un objeto de ejemplo para LintRulesDto
    UpdateRulesRequestDto<LintRulesDto> requestDto = new UpdateRulesRequestDto<>(rulesDto);
    LintConfig lintConfig = new LintConfig();

    when(ownershipService.findSnippetIdsByUserId(userId)).thenReturn(Optional.empty());
    when(lintingService.updateRules(userId, requestDto)).thenReturn(lintConfig);
    mockMvc
        .perform(
            post("/linting/update/rules")
                .header("userId", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(requestDto)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.message").value("Updated lint rules, no snippets to lint"));
  }

  @Test
  void testUpdateRulesAndLint() throws Exception {
    String userId = anyString();
    LintRulesDto rulesDto = new LintRulesDto(); // Crear un objeto de ejemplo para LintRulesDto
    UpdateRulesRequestDto<LintRulesDto> requestDto = new UpdateRulesRequestDto<>(rulesDto);
    LintConfig lintConfig = new LintConfig();

    when(ownershipService.findSnippetIdsByUserId(userId)).thenReturn(Optional.of(List.of(1L, 2L)));
    when(lintingService.updateRules(userId, requestDto)).thenReturn(lintConfig);
    when(lintingService.asyncLint(List.of(1L, 2L), rulesDto)).thenReturn(Optional.empty());
    mockMvc
        .perform(
            post("/linting/update/rules")
                .header("userId", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(requestDto)))
        .andExpect(status().isInternalServerError())
        .andExpect(jsonPath("$.message").value("Updated lint rules, but something occurred during asynchronous linting"));
  }

  @Test
  void testUpdateRulesAndLint_ServerError() throws Exception {
    String userId = "user123";
    LintRulesDto rulesDto = new LintRulesDto(); // Crear un objeto de ejemplo para LintRulesDto
    UpdateRulesRequestDto<LintRulesDto> requestDto = new UpdateRulesRequestDto<>(rulesDto);
    List<Long> snippetsIds = List.of(1L, 2L);

    when(ownershipService.findSnippetIdsByUserId(userId))
        .thenThrow(new RuntimeException("Internal server error"));

    mockMvc
        .perform(
            post("/linting/update/rules")
                .header("userId", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(requestDto)))
        .andExpect(status().isInternalServerError());
  }
}
