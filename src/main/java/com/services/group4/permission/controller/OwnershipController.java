package com.services.group4.permission.controller;

import com.services.group4.permission.dto.ResponseDto;
import com.services.group4.permission.model.Ownership;
import com.services.group4.permission.repository.OwnershipRepository;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import com.services.group4.permission.service.OwnershipService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ownership")
public class OwnershipController {

  private final OwnershipService ownershipService;
  private final OwnershipRepository ownershipRepository;

  public OwnershipController(OwnershipService ownershipService, OwnershipRepository ownershipRepository) {
    this.ownershipService = ownershipService;
    this.ownershipRepository = ownershipRepository;
  }

  @PostMapping("/create")
  public ResponseEntity<String> addOwnership(@RequestBody Ownership ownership) {
    try {
      ownershipRepository.save(ownership);
      return new ResponseEntity<>("Ownership created", HttpStatus.CREATED);
    } catch (Exception e) {
      System.out.println(e.getMessage());
      return new ResponseEntity<>(
          "Something went wrong creating the ownership", HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @GetMapping("/user/{id}")
  public ResponseEntity<Ownership> getOwnershipByUserId(@PathVariable Long id) {
    Optional<Ownership> ownership = ownershipRepository.findOwnershipByUserId(id);
    return ownership
        .map(value -> new ResponseEntity<>(value, HttpStatus.OK))
        .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
  }

  @GetMapping("/snippet/{id}")
  public ResponseEntity<Ownership> getOwnershipBySnippetId(@PathVariable Long id) {
    Optional<Ownership> ownership = ownershipRepository.findOwnershipBySnippetId(id);
    return ownership
        .map(value -> new ResponseEntity<>(value, HttpStatus.OK))
        .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
  }

  @GetMapping
  public ResponseEntity<Iterable<Ownership>> getAllOwnerships() {
    return new ResponseEntity<>(ownershipRepository.findAll(), HttpStatus.OK);
  }

  @DeleteMapping("/delete/{id}")
  public ResponseEntity<String> deleteOwnership(@PathVariable Long id) {
    try {
      ownershipRepository.deleteById(id);
      return new ResponseEntity<>("Ownership deleted", HttpStatus.OK);
    } catch (Exception e) {
      System.out.println(e.getMessage());
      return new ResponseEntity<>(
          "Something went wrong deleting the ownership", HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }


  // TODO: new routes for snippet-service

  // ownership/created funciona por postman
  @PostMapping("/createRelation")
  public ResponseEntity<ResponseDto<Long>> createOwnership(@RequestBody Map<String, Object> requestData) {
    try {
      Long userId = ((Integer) requestData.get("userId")).longValue();
      Long snippetId = ((Integer) requestData.get("snippetId")).longValue();
      return ownershipService.createOwnership(userId, snippetId);
    } catch (Exception e) {
      System.out.println(e.getMessage());
      return new ResponseEntity<>(
          new ResponseDto<>("Something went wrong creating the ownership for the snippet",null),
          HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  // ownership/getPermission funciona por postman
  @GetMapping("/permission/{userId}/for/{snippetId}")
  public ResponseEntity<ResponseDto<Boolean>> hasOwnerPermission(
      @PathVariable Long userId, @PathVariable Long snippetId) {
      try {
        return ownershipService.hasOwnerPermission(userId, snippetId);
      } catch (Exception e) {
        return new ResponseEntity<>(new ResponseDto<>(e.getMessage(), false), HttpStatus.INTERNAL_SERVER_ERROR);
      }
  }


}
