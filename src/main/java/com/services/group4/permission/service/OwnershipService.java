package com.services.group4.permission.service;

import com.services.group4.permission.dto.ResponseDto;
import com.services.group4.permission.model.Ownership;
import com.services.group4.permission.repository.OwnershipRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OwnershipService {

  private final OwnershipRepository ownershipRepository;
  private final ValidationService validationService;

  public OwnershipService(OwnershipRepository ownershipRepository, ValidationService validationService) {
    this.ownershipRepository = ownershipRepository;
    this.validationService = validationService;
  }

  public ResponseEntity<ResponseDto<Long>> createOwnership(Long userId, Long snippetId) {
    if (!validationService.isUserIdValid(userId)) {
      return new ResponseEntity<>(new ResponseDto<>("User isn't valid, it doesn't exists",userId) ,HttpStatus.BAD_REQUEST);
    }
    Ownership ownership = new Ownership(userId, snippetId);
    ownershipRepository.save(ownership);
    return new ResponseEntity<>(new ResponseDto<>("Ownership created",null), HttpStatus.CREATED);
  }

  public boolean isOwner(Long userId, Long snippetId) {
    return ownershipRepository.findOwnershipByUserIdAndSnippetId(userId, snippetId).isPresent();
  }

  public ResponseEntity<ResponseDto<Long>> hasOwnerPermission(Long userId, Long snippetId) {
    if (!validationService.isUserIdValid(userId)) {
      return new ResponseEntity<>(new ResponseDto<>("User isn't valid, it doesn't exists", userId), HttpStatus.BAD_REQUEST);
    }
    if (isOwner(userId, snippetId)) {
      return new ResponseEntity<>(new ResponseDto<>("User is the owner of the snippet",userId), HttpStatus.OK);
    }
    return new ResponseEntity<>(new ResponseDto<>("User is not the owner of the snippet", userId), HttpStatus.FORBIDDEN);
  }

  public Optional<List<Long>> findSnippetIdsByUserId(Long userId) {
    return ownershipRepository.findSnippetIdsByUserId(userId);
  }

  public ResponseEntity<ResponseDto<Long>> deleteOwnership(Long userId, Long snippetId) {
    if (!validationService.isUserIdValid(userId)) {
      return new ResponseEntity<>(new ResponseDto<>("User isn't valid, it doesn't exists", userId), HttpStatus.BAD_REQUEST);
    }
    Optional<Ownership> ownership = ownershipRepository.findOwnershipByUserIdAndSnippetId(userId, snippetId);
    if (ownership.isPresent()) {
      ownershipRepository.delete(ownership.get());
      return new ResponseEntity<>(new ResponseDto<>("Ownership deleted", snippetId), HttpStatus.OK);
    }
    return new ResponseEntity<>(new ResponseDto<>("User doesn't have permission over this snippet", snippetId), HttpStatus.FORBIDDEN);
  }
}
