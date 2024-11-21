package com.services.group4.permission.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.services.group4.permission.DotenvConfig;
import com.services.group4.permission.clients.SnippetClient;
import com.services.group4.permission.common.DataTuple;
import com.services.group4.permission.dto.ResponseDto;
import com.services.group4.permission.dto.snippet.Language;
import com.services.group4.permission.dto.snippet.SnippetResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

class SnippetServiceTest {

  @Mock
  private SnippetClient snippetClient;

  @InjectMocks
  private SnippetService snippetService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    DotenvConfig.loadEnv();
  }

  @Test
  void getSnippetInfo_shouldReturnSnippetInfoWhenClientRespondsSuccessfully() {
    Long snippetId = 1L;
    SnippetResponseDto snippetResponseDto = new SnippetResponseDto(1L,"snippet", "USER", new Language("java", "1.8", "Java"));
    ResponseDto<SnippetResponseDto> responseDto = new ResponseDto<>("Success", new DataTuple<>("snippet", snippetResponseDto));
    ResponseEntity<ResponseDto<SnippetResponseDto>> clientResponse = new ResponseEntity<>(responseDto, HttpStatus.OK);

    when(snippetClient.getSnippetInfo(snippetId)).thenReturn(clientResponse);

    ResponseEntity<ResponseDto<SnippetResponseDto>> result = snippetService.getSnippetInfo(snippetId);

    assertNotNull(result);
    assertEquals(HttpStatus.OK, result.getStatusCode());
    assertEquals("Success", result.getBody().message());
    assertEquals(snippetResponseDto, result.getBody().data().data());
    verify(snippetClient).getSnippetInfo(snippetId);
  }

  @Test
  void getSnippetInfo_shouldReturnErrorWhenClientFails() {
    Long snippetId = 2L;
    ResponseDto<SnippetResponseDto> errorResponse = new ResponseDto<>("Snippet not found", null);
    ResponseEntity<ResponseDto<SnippetResponseDto>> clientResponse = new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);

    when(snippetClient.getSnippetInfo(snippetId)).thenReturn(clientResponse);

    ResponseEntity<ResponseDto<SnippetResponseDto>> result = snippetService.getSnippetInfo(snippetId);

    assertNotNull(result);
    assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
    assertEquals("Snippet not found", result.getBody().message());
    assertNull(result.getBody().data());
    verify(snippetClient).getSnippetInfo(snippetId);
  }
}

