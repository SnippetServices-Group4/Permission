package com.services.group4.permission.controller;

import com.services.group4.permission.common.FullResponse;
import com.services.group4.permission.dto.RequestDtoSnippet;
import com.services.group4.permission.dto.ResponseDto;
import com.services.group4.permission.service.PermissionService;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/permissions")
public class PermissionController {

  private final PermissionService permissionService;

  public PermissionController(PermissionService permissionService) {
    this.permissionService = permissionService;
  }

  @GetMapping("/allowedSnippets/{userId}")
  public ResponseEntity<ResponseDto<List<Long>>> getAllowedSnippets(@PathVariable String userId) {
    log.info("Getting all snippets that user with id {} has permission to view", userId);
    try {
      ResponseEntity<ResponseDto<List<Long>>> allowedSnippets =
          permissionService.getAllowedSnippets(userId);
      log.info("Returning all snippets that user with id {} has permission to view", userId);
      return allowedSnippets;
    } catch (Exception e) {
      log.error("User doesn't have permission to view any snippet");
      return FullResponse.create(
          "User doesn't have permission to view any snippet",
          "Snippet",
          null,
          HttpStatus.NOT_FOUND);
    }
  }

  @GetMapping("/{userId}/for/{snippetId}")
  public ResponseEntity<ResponseDto<Boolean>> hasPermission(
      @PathVariable String userId, @PathVariable Long snippetId) {
    log.info("Checking permission for user with id: {}", userId);
    return permissionService.hasPermissionOnSnippet(userId, snippetId);
  }

  // ownership/delete funciona por postman
  @DeleteMapping("/deleteRelation")
  public ResponseEntity<ResponseDto<Long>> deleteOwnership(
      @RequestBody RequestDtoSnippet requestData) {
    log.info("Trying to delete ownership for snippet with id: {}", requestData.snippetId());
    try {
      String userId = requestData.userId();
      Long snippetId = requestData.snippetId();
      return permissionService.deletePermissionsOfSnippet(userId, snippetId);
    } catch (Exception e) {
      log.error("Error deleting ownership for snippet: {}", e.getMessage());
      return FullResponse.create(
          "Something went wrong deleting the ownership of the snippet",
          "Ownership",
          null,
          HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
}
