package com.services.group4.permission.controller;

import com.services.group4.permission.common.FullResponse;
import com.services.group4.permission.dto.RequestDtoShareSnippet;
import com.services.group4.permission.dto.ResponseDto;
import com.services.group4.permission.service.ReaderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reader")
public class ReaderController {

  private final ReaderService readerService;

  public ReaderController(ReaderService readerService) {
    this.readerService = readerService;
  }

  // TODO: new routes for snippet-service

  // reader/share funciona por postman
  @PostMapping("/share")
  public ResponseEntity<ResponseDto<String>> shareSnippet(
      @RequestBody RequestDtoShareSnippet requestData, @RequestHeader("userId") String ownerId) {
    Long snippetId = requestData.snippetId();
    String targetUserId = requestData.targetUserId();

    return readerService.shareSnippet(ownerId, snippetId, targetUserId);
  }

  // reader/getPermission funciona por postman
  @GetMapping("/permission/{userId}/for/{snippetId}")
  public ResponseEntity<ResponseDto<Boolean>> hasReaderPermission(
      @PathVariable String userId, @PathVariable Long snippetId) {
    try {
      return readerService.getReaderPermission(userId, snippetId);
    } catch (Exception e) {
      return FullResponse.create(
          e.getMessage(), "readerPermission", false, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
}
