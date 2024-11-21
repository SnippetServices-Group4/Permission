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
import com.services.group4.permission.dto.RequestDtoSnippet;
import com.services.group4.permission.service.OwnershipService;
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

@ExtendWith(SpringExtension.class)
@WebMvcTest(OwnershipController.class)
class OwnershipControllerTests {

  @BeforeAll
  public static void setupEnv() {
    DotenvConfig.loadEnv();
  }

  @MockBean private OwnershipService ownershipService;

  @Autowired private MockMvc mockMvc;

  private final ObjectMapper objectMapper = new ObjectMapper();

  @Test
  void testCreateOwnership_Success() throws Exception {
    RequestDtoSnippet requestDto = new RequestDtoSnippet("user123", 1L);
    when(ownershipService.createOwnership(anyString(), anyLong()))
        .thenReturn(
            FullResponse.create("Ownership created successfully", "snippetId", 1L, HttpStatus.OK));

    mockMvc
        .perform(
            post("/ownership/createRelation")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(requestDto)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.message").value("Ownership created successfully"))
        .andExpect(jsonPath("$.data.snippetId").value(1L));
  }

  @Test
  void testCreateOwnership_InternalServerError() throws Exception {
    RequestDtoSnippet requestDto = new RequestDtoSnippet("user123", 1L);
    when(ownershipService.createOwnership(anyString(), anyLong()))
        .thenThrow(new RuntimeException("Database error"));

    mockMvc
        .perform(
            post("/ownership/createRelation")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(requestDto)))
        .andExpect(status().isInternalServerError())
        .andExpect(
            jsonPath("$.message")
                .value("Something went wrong creating the ownership for the snippet"));
  }

  @Test
  void testHasOwnerPermission_Success() throws Exception {
    when(ownershipService.hasOwnerPermission(anyString(), anyLong()))
        .thenReturn(
            FullResponse.create(
                "User has ownership permission", "ownerPermission", true, HttpStatus.OK));

    mockMvc
        .perform(get("/ownership/permission/user123/for/1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.message").value("User has ownership permission"))
        .andExpect(jsonPath("$.data.ownerPermission").value(true));
  }

  @Test
  void testHasOwnerPermission_Forbidden() throws Exception {
    when(ownershipService.hasOwnerPermission(anyString(), anyLong()))
        .thenThrow(
            HttpClientErrorException.create(
                HttpStatus.FORBIDDEN, "User does not have permission", null, null, null));
    mockMvc
        .perform(get("/ownership/permission/user123/for/1"))
        .andExpect(status().isForbidden())
        .andExpect(
            jsonPath("$.message").value("User does not have permission to update this snippet"));
  }

  @Test
  void testHasOwnerPermission_InternalServerError() throws Exception {
    when(ownershipService.hasOwnerPermission(anyString(), anyLong()))
        .thenThrow(new RuntimeException("Unexpected error"));

    mockMvc
        .perform(get("/ownership/permission/user123/for/1"))
        .andExpect(status().isInternalServerError())
        .andExpect(
            jsonPath("$.message")
                .value("Something went wrong getting the ownership permission for the snippet"))
        .andExpect(jsonPath("$.data.ownerPermission").value(false));
  }
}
