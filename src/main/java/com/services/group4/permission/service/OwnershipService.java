package com.services.group4.permission.service;

import com.services.group4.permission.model.Ownership;
import com.services.group4.permission.repository.OwnershipRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class OwnershipService {

  private OwnershipRepository ownershipRepository;
  private ValidationService validationService;

  public ResponseEntity<Ownership> createOwnership(Long userId, Long snippetId) {
    if (!validationService.isUserIdValid(userId)) {
      return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    }
    Ownership ownership = new Ownership(userId, snippetId);
    ownershipRepository.save(ownership);
    return new ResponseEntity<>(ownership, HttpStatus.CREATED);
  }

  public boolean isOwner(Long userId, Long snippetId) {
    return ownershipRepository.findOwnershipByUserIdAndSnippetId(userId, snippetId).isPresent();
  }
}
