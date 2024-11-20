package com.services.group4.permission.controller;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.services.group4.permission.DotenvConfig;
import com.services.group4.permission.dto.FormatRulesDto;
import com.services.group4.permission.dto.UpdateRulesRequestDto;
import com.services.group4.permission.service.FormattingService;
import com.services.group4.permission.service.OwnershipService;
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
@WebMvcTest(FormattingController.class)
public class FormattingControllerTests {

  @BeforeAll
  public static void setupEnv() {
    DotenvConfig.loadEnv();
  }

  @MockBean private FormattingService formattingService;
  @MockBean private OwnershipService ownershipService;

  @Autowired private MockMvc mockMvc;
  private final ObjectMapper objectMapper = new ObjectMapper();

  @Test
  void testGetConfig_UserExists() throws Exception {
    String userId = "user123";
    FormatRulesDto mockConfig =
        new FormatRulesDto(); // Crear un objeto de ejemplo para FormatRulesDto
    when(formattingService.getConfig(userId)).thenReturn(Optional.of(mockConfig));

    mockMvc
        .perform(get("/formatting/rules").header("userId", userId))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.message").value("Config of user " + userId + " found."))
        .andExpect(jsonPath("$.data.config").isNotEmpty());
  }

  @Test
  void testGetConfig_UserNotFound() throws Exception {
    String userId = "user123";
    when(formattingService.getConfig(userId)).thenReturn(Optional.empty());

    mockMvc
        .perform(get("/formatting/rules").header("userId", userId))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.message").value("User doesn't exist."));
  }

  @Test
  void testUpdateRulesAndFormat_NoSnippetsToFormat() throws Exception {
    String userId = "user123";
    FormatRulesDto rulesDto =
        new FormatRulesDto(); // Crear un objeto de ejemplo para FormatRulesDto
    UpdateRulesRequestDto<FormatRulesDto> requestDto = new UpdateRulesRequestDto<>(rulesDto);

    when(ownershipService.findSnippetIdsByUserId(userId)).thenReturn(Optional.empty());
    when(formattingService.asyncFormat(any(), any())).thenReturn(Optional.empty());

    mockMvc
        .perform(
            post("/formatting/update/rules")
                .header("userId", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.message").value("No snippets to format"))
        .andExpect(jsonPath("$.data.snippetsIds").isArray())
        .andExpect(jsonPath("$.data.snippetsIds.length()").value(0));
  }

  @Test
  void testUpdateRulesAndFormat_InternalServerError() throws Exception {
    String userId = "user123";
    FormatRulesDto rulesDto =
        new FormatRulesDto(); // Crear un objeto de ejemplo para FormatRulesDto
    UpdateRulesRequestDto<FormatRulesDto> requestDto = new UpdateRulesRequestDto<>(rulesDto);

    when(formattingService.updateRules(any(), any()))
        .thenThrow(new RuntimeException("Internal error"));

    mockMvc
        .perform(
            post("/formatting/update/rules")
                .header("userId", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
        .andExpect(status().isInternalServerError());
  }
}
