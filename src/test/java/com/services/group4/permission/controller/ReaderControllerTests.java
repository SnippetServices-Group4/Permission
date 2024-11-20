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
import com.services.group4.permission.dto.RequestDtoShareSnippet;
import com.services.group4.permission.service.ReaderService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

@ExtendWith(SpringExtension.class)
@WebMvcTest(ReaderController.class)
public class ReaderControllerTests {

  @BeforeAll
  public static void setupEnv() {
    DotenvConfig.loadEnv();
  }

  @MockBean private ReaderService readerService;

  @Autowired private MockMvc mockMvc;

  @Test
  void testShareSnippet() throws Exception {
    RequestDtoShareSnippet requestDto = new RequestDtoShareSnippet(1L, "user123");
    when(readerService.shareSnippet(anyString(), anyLong(), anyString()))
        .thenReturn(
            FullResponse.create("Snippet shared successfully", "ownerId", "1", HttpStatus.OK));

    mockMvc
        .perform(
            post("/reader/share")
                .header("userId", "ownerId")
                .contentType("application/json")
                .content(new ObjectMapper().writeValueAsString(requestDto)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.message").value("Snippet shared successfully"));
  }

  @Test
  void testHasReaderPermission() throws Exception {

    when(readerService.getReaderPermission(anyString(), anyLong()))
        .thenThrow(new RuntimeException("Some error"));
    mockMvc
        .perform(get("/reader/permission/1/for/1"))
        .andExpect(status().isInternalServerError())
        .andExpect(jsonPath("$.message").value("Some error"))
        .andExpect(jsonPath("$.data.readerPermission").value(false));
  }

  @Test
  void testHasReaderPermission_InternalServerError() throws Exception {
    when(readerService.getReaderPermission(anyString(), anyLong()))
        .thenReturn(
            FullResponse.create(
                "Database connection failed",
                "readerPermission",
                false,
                HttpStatus.INTERNAL_SERVER_ERROR));

    mockMvc
        .perform(get("/reader/permission/1/for/1"))
        .andExpect(status().isInternalServerError())
        .andExpect(jsonPath("$.message").value("Database connection failed"))
        .andExpect(jsonPath("$.data.readerPermission").value(false));
  }
}
