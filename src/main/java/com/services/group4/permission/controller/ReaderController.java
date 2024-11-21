package com.services.group4.permission.controller;

import com.services.group4.permission.common.FullResponse;
import com.services.group4.permission.dto.RequestDtoShareSnippet;
import com.services.group4.permission.dto.ResponseDto;
import com.services.group4.permission.service.ReaderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
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
    log.info("Sharing snippet with id: {}", requestData.snippetId());
    Long snippetId = requestData.snippetId();
    String targetUserId = requestData.targetUserId();

    return readerService.shareSnippet(ownerId, snippetId, targetUserId);
  }

  // reader/getPermission funciona por postman
  @GetMapping("/permission/{userId}/for/{snippetId}")
  public ResponseEntity<ResponseDto<Boolean>> hasReaderPermission(
      @PathVariable String userId, @PathVariable Long snippetId) {
    log.info("Checking reader permission for user with id: {}", userId);
    try {
      return readerService.getReaderPermission(userId, snippetId);
    } catch (Exception e) {
      log.error("Error checking reader permission: {}", e.getMessage());
      return FullResponse.create(
          e.getMessage(), "readerPermission", false, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
}
