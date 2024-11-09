package com.services.group4.permission.controller;

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
  public ResponseEntity<ResponseDto<List<Long>>> getAllowedSnippets(@PathVariable Long userId) {
    try {
      return permissionService.getAllowedSnippets(userId);
    } catch (Exception e) {
      System.out.println("Error: " + e.getMessage());
      return new ResponseEntity<>(new ResponseDto<>(e.getMessage(), null),HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  // ownership/delete funciona por postman
  @DeleteMapping("/deleteRelation")
  public ResponseEntity<ResponseDto<Long>> deleteOwnership(@RequestBody Map<String, Object> requestData) {
    try {
      Long userId = ((Integer) requestData.get("userId")).longValue();
      Long snippetId = ((Integer) requestData.get("snippetId")).longValue();
      return permissionService.deletePermissionsOfSnippet(userId, snippetId);
    } catch (Exception e) {
      System.out.println(e.getMessage());
      return new ResponseEntity<>(
          new ResponseDto<>("Something went wrong deleting the ownership of the snippet",null),
          HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
}
