package com.services.group4.permission.controller;

import com.services.group4.permission.common.FullResponse;
import com.services.group4.permission.dto.ResponseDto;
import com.services.group4.permission.model.Reader;
import com.services.group4.permission.repository.ReaderRepository;
import com.services.group4.permission.service.PermissionService;
import com.services.group4.permission.service.ReaderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/permissions")
public class PermissionController {

  private final PermissionService permissionService;

  public PermissionController(PermissionService permissionService) {
    this.permissionService = permissionService;
  }

  // new routes for snippet-service

  @GetMapping("/allowedSnippets/{userId}")
  public ResponseEntity<ResponseDto<List<Long>>> getAllowedSnippets(@PathVariable String userId) {
    try {
      return permissionService.getAllowedSnippets(userId);
    } catch (Exception e) {
      System.out.println("Error: " + e.getMessage());
      return FullResponse.create("User doesn't have permission to view any snippet", "Snippet", null, HttpStatus.NOT_FOUND);
    }
  }

  @GetMapping("/{userId}/for/{snippetId}")
  public ResponseEntity<ResponseDto<Boolean>> hasPermission(@PathVariable String userId, @PathVariable Long snippetId) {
    return permissionService.hasPermissionOnSnippet(userId, snippetId);

  }

  // ownership/delete funciona por postman
  @DeleteMapping("/deleteRelation")
  public ResponseEntity<ResponseDto<Long>> deleteOwnership(@RequestBody Map<String, Object> requestData) {
    try {
      String userId = ((String) requestData.get("userId"));
      Long snippetId = ((Integer) requestData.get("snippetId")).longValue();
      return permissionService.deletePermissionsOfSnippet(userId, snippetId);
    } catch (Exception e) {
      return FullResponse.create("Something went wrong deleting the ownership of the snippet", "Ownership", null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
}
