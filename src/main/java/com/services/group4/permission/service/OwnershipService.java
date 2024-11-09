package com.services.group4.permission.service;

import com.services.group4.permission.model.Ownership;
import com.services.group4.permission.repository.OwnershipRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class OwnershipService {

  private final OwnershipRepository ownershipRepository;
  private final ValidationService validationService;

  public OwnershipService(OwnershipRepository ownershipRepository, ValidationService validationService) {
    this.ownershipRepository = ownershipRepository;
    this.validationService = validationService;
  }

  public ResponseEntity<String> createOwnership(Long userId, Long snippetId) {
    if (!validationService.isUserIdValid(userId)) {
      return new ResponseEntity<>("User isn't valid, it doesn't exists", HttpStatus.BAD_REQUEST);
    }
    Ownership ownership = new Ownership(userId, snippetId);
    ownershipRepository.save(ownership);
    return new ResponseEntity<>("Ownership created", HttpStatus.CREATED);
  }

  public boolean isOwner(Long userId, Long snippetId) {
    return ownershipRepository.findOwnershipByUserIdAndSnippetId(userId, snippetId).isPresent();
  }

  public ResponseEntity<String> getOwnershipPermission(Long userId, Long snippetId) {
    if (!validationService.isUserIdValid(userId)) {
      return new ResponseEntity<>("User isn't valid, it doesn't exists", HttpStatus.BAD_REQUEST);
    }
    if (isOwner(userId, snippetId)) {
      return new ResponseEntity<>("User is the owner of the snippet", HttpStatus.OK);
    }
    return new ResponseEntity<>("User is not the owner of the snippet", HttpStatus.FORBIDDEN);
  }
}
