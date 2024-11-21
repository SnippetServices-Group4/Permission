package com.services.group4.permission.controller;

import com.services.group4.permission.common.FullResponse;
import com.services.group4.permission.dto.RequestDtoSnippet;
import com.services.group4.permission.dto.ResponseDto;
import com.services.group4.permission.service.OwnershipService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;

@Slf4j
@RestController
@RequestMapping("/ownership")
public class OwnershipController {

  private final OwnershipService ownershipService;

  public OwnershipController(OwnershipService ownershipService) {
    this.ownershipService = ownershipService;
  }

  // TODO: new routes for snippet-service

  // ownership/created funciona por postman
  @PostMapping("/createRelation")
  public ResponseEntity<ResponseDto<Long>> createOwnership(
      @RequestBody RequestDtoSnippet requestData) {
    log.info("Creating ownership for snippet with id: {}", requestData.snippetId());
    try {
      String userId = requestData.userId();
      Long snippetId = requestData.snippetId();
      return ownershipService.createOwnership(userId, snippetId);
    } catch (Exception e) {
      log.error("Error creating ownership for snippet: {}", e.getMessage());
      return FullResponse.create(
          "Something went wrong creating the ownership for the snippet",
          "Empty",
          null,
          HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  // ownership/getPermission funciona por postman
  @GetMapping("/permission/{userId}/for/{snippetId}")
  public ResponseEntity<ResponseDto<Boolean>> hasOwnerPermission(
      @PathVariable String userId, @PathVariable Long snippetId) {
    log.info("Checking ownership permission for user with id: {}", userId);
    try {
      ResponseEntity<ResponseDto<Boolean>> ownerPermission =
          ownershipService.hasOwnerPermission(userId, snippetId);
      log.info("Returning ownership permission for user with id: {}", userId);
      return ownerPermission;
    } catch (HttpClientErrorException.Forbidden e) {
      log.error("User does not have permission to update this snippet");
      return FullResponse.create(
          "User does not have permission to update this snippet",
          "snippet",
          null,
          HttpStatus.FORBIDDEN);
    } catch (Exception e) {
      log.error("Error while getting the ownership permission for the snippet: {}", e.getMessage());
      return FullResponse.create(
          "Something went wrong getting the ownership permission for the snippet",
          "ownerPermission",
          false,
          HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
}
