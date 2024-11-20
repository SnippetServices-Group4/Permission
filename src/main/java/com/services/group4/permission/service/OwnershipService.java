package com.services.group4.permission.service;

import com.services.group4.permission.common.FullResponse;
import com.services.group4.permission.dto.ResponseDto;
import com.services.group4.permission.model.Ownership;
import com.services.group4.permission.repository.OwnershipRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class OwnershipService {

  private final OwnershipRepository ownershipRepository;

  public OwnershipService(
      OwnershipRepository ownershipRepository) {
    this.ownershipRepository = ownershipRepository;
  }

  public ResponseEntity<ResponseDto<Long>> createOwnership(String userId, Long snippetId) {
    Ownership ownership = new Ownership(userId, snippetId);
    ownershipRepository.save(ownership);
    return FullResponse.create("Ownership created", "snippetId", snippetId, HttpStatus.CREATED);
  }

  public boolean isOwner(String userId, Long snippetId) {
    return ownershipRepository.findOwnershipByUserIdAndSnippetId(userId, snippetId).isPresent();
  }

  public ResponseEntity<ResponseDto<Boolean>> hasOwnerPermission(String userId, Long snippetId) {
    if (isOwner(userId, snippetId)) {
      return FullResponse.create(
          "User is the owner of the snippet", "ownerPermission", true, HttpStatus.OK);
    }
    return FullResponse.create(
        "User is not the owner of the snippet", "ownerPermission", false, HttpStatus.FORBIDDEN);
  }

  public Optional<List<Long>> findSnippetIdsByUserId(String userId) {
    return ownershipRepository.findSnippetIdsByUserId(userId);
  }

  public ResponseEntity<ResponseDto<Long>> deleteOwnership(String userId, Long snippetId) {
    if (isOwner(userId, snippetId)) {
      Optional<Ownership> ownership =
          ownershipRepository.findOwnershipByUserIdAndSnippetId(userId, snippetId);
      if (ownership.isPresent()) {
        ownershipRepository.delete(ownership.get());
        return FullResponse.create("Ownership deleted", "snippetId", snippetId, HttpStatus.OK);
      }
    }
    return FullResponse.create(
        "User is not the owner of the snippet", "snippetId", snippetId, HttpStatus.FORBIDDEN);
  }
}
