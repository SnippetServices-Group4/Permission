package com.services.group4.permission.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.services.group4.permission.DotenvConfig;
import com.services.group4.permission.common.FullResponse;
import com.services.group4.permission.dto.RequestDtoSnippet;
import com.services.group4.permission.service.OwnershipService;
import com.services.group4.permission.service.PermissionService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(PermissionController.class)
class PermissionControllerTests {

  @BeforeAll
  public static void setupEnv() {
    DotenvConfig.loadEnv();
  }

  @MockBean private PermissionService permissionService;

  @Autowired private MockMvc mockMvc;

  private final ObjectMapper objectMapper = new ObjectMapper();

  @Test
  void testGetAllowedSnippets_Success() throws Exception {
    List<Long> snippets = List.of(1L, 2L, 3L);
    when(permissionService.getAllowedSnippets(anyString()))
        .thenReturn(
            FullResponse.create(
                "Allowed snippets retrieved successfully", "snippets", snippets, HttpStatus.OK));

    mockMvc
        .perform(get("/permissions/allowedSnippets/user123"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.message").value("Allowed snippets retrieved successfully"))
        .andExpect(jsonPath("$.data.snippets[0]").value(1L))
        .andExpect(jsonPath("$.data.snippets[1]").value(2L))
        .andExpect(jsonPath("$.data.snippets[2]").value(3L));
  }

  @Test
  void testGetAllowedSnippets_NotFound() throws Exception {
    when(permissionService.getAllowedSnippets(anyString()))
        .thenThrow(new RuntimeException("User doesn't have access to any snippets"));

    mockMvc
        .perform(get("/permissions/allowedSnippets/user123"))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.message").value("User doesn't have permission to view any snippet"))
        .andExpect(jsonPath("$.data.snippet").isEmpty());
  }

  @Test
  void testHasPermission_Success() throws Exception {
    when(permissionService.hasPermissionOnSnippet(anyString(), anyLong()))
        .thenReturn(
            FullResponse.create(
                "User has permission on the snippet", "hasPermission", true, HttpStatus.OK));

    mockMvc
        .perform(get("/permissions/user123/for/1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.message").value("User has permission on the snippet"))
        .andExpect(jsonPath("$.data.hasPermission").value(true));
  }

  @Test
  void testHasPermission_Forbidden() throws Exception {
    when(permissionService.hasPermissionOnSnippet(anyString(), anyLong()))
        .thenReturn(
            FullResponse.create(
                "User doesn't have permission on the snippet",
                "hasPermission",
                false,
                HttpStatus.FORBIDDEN));

    mockMvc
        .perform(get("/permissions/user123/for/1"))
        .andExpect(status().isForbidden())
        .andExpect(jsonPath("$.message").value("User doesn't have permission on the snippet"))
        .andExpect(jsonPath("$.data.hasPermission").value(false));
  }

  @Test
  void testDeleteOwnership_Success() throws Exception {
    RequestDtoSnippet requestDto = new RequestDtoSnippet("user123", 1L);
    when(permissionService.deletePermissionsOfSnippet(anyString(), anyLong()))
        .thenReturn(
            FullResponse.create(
                "Ownership deleted successfully", "snippetId", 1L, HttpStatus.OK));

    mockMvc
        .perform(
            delete("/permissions/deleteRelation")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(requestDto)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.message").value("Ownership deleted successfully"))
        .andExpect(jsonPath("$.data.snippetId").value(1L));
  }

  @Test
  void testDeleteOwnership_InternalServerError() throws Exception {
    RequestDtoSnippet requestDto = new RequestDtoSnippet("user123", 1L);
    when(permissionService.deletePermissionsOfSnippet(anyString(), anyLong()))
        .thenThrow(new RuntimeException("Database error while deleting ownership"));

    mockMvc
        .perform(
            delete("/permissions/deleteRelation")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(requestDto)))
        .andExpect(status().isInternalServerError())
        .andExpect(
            jsonPath("$.message")
                .value("Something went wrong deleting the ownership of the snippet"))
        .andExpect(jsonPath("$.data.ownership").isEmpty());
  }
}
