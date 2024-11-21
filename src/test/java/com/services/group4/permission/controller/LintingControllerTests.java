package com.services.group4.permission.controller;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.services.group4.permission.DotenvConfig;
import com.services.group4.permission.common.FullResponse;
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
import org.springframework.http.HttpStatus;
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


  @Autowired private MockMvc mockMvc;

  private final ObjectMapper objectMapper = new ObjectMapper();

  @Test
  void testGetConfig_UserFound() throws Exception {
    String userId = "user123";
    LintRulesDto rulesDto = new LintRulesDto(); // Crear un objeto de ejemplo para LintRulesDto
    when(lintingService.getConfig(userId)).thenReturn(rulesDto);

    mockMvc
        .perform(get("/linting/rules").header("userId", userId))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.message").value("Config of user user123 found."))
        .andExpect(jsonPath("$.data.config").exists());
  }

  @Test
  void testGetConfig_ExceptionThrown() throws Exception {
    String userId = "user123";
    when(lintingService.getConfig(userId)).thenThrow(new RuntimeException("Error getting config"));

    mockMvc
        .perform(get("/linting/rules").header("userId", userId))
        .andExpect(status().isInternalServerError())
        .andExpect(jsonPath("$.message").value("Error getting linting rules"));
  }

  @Test
  void testUpdateRulesAndLint_NoSnippetsToLint() throws Exception {
    String userId = "user123";
    LintRulesDto rulesDto = new LintRulesDto(); // Crear un objeto de ejemplo para LintRulesDto
    UpdateRulesRequestDto<LintRulesDto> requestDto = new UpdateRulesRequestDto<>(rulesDto);
    LintConfig lintConfig = new LintConfig();

    when(lintingService.updateAndLint(eq(userId), any(UpdateRulesRequestDto.class)))
        .thenReturn(FullResponse.create("No snippets to lint.", "snippetsIds", lintConfig, HttpStatus.INTERNAL_SERVER_ERROR));
    mockMvc
        .perform(
            post("/linting/update/rules")
                .header("userId", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(requestDto)))
        .andExpect(status().isInternalServerError())
        .andExpect(jsonPath("$.message").value("No snippets to lint."));
  }

}
