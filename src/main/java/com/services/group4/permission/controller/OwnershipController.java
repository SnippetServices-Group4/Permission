package com.services.group4.permission.controller;

import com.services.group4.permission.common.FullResponse;
import com.services.group4.permission.dto.RequestDtoSnippet;
import com.services.group4.permission.dto.ResponseDto;
import com.services.group4.permission.service.OwnershipService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

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
    try {
      String userId = requestData.userId();
      Long snippetId = requestData.snippetId();
      return ownershipService.createOwnership(userId, snippetId);
    } catch (Exception e) {
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
    try {
      ResponseEntity<ResponseDto<Boolean>> ownerPermission =
          ownershipService.hasOwnerPermission(userId, snippetId);
      return ownerPermission;
    } catch (HttpClientErrorException.Forbidden e) {
      return FullResponse.create(
          "User does not have permission to update this snippet",
          "snippet",
          null,
          HttpStatus.FORBIDDEN);
    } catch (Exception e) {
      return FullResponse.create(
          "Something went wrong getting the ownership permission for the snippet",
          "ownerPermission",
          false,
          HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
}
