package com.services.group4.permission.service;

import com.services.group4.permission.model.Reader;
import com.services.group4.permission.repository.ReaderRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ReaderService {

  private final ReaderRepository readerRepository;
  private final OwnershipService ownershipService;
  private final ValidationService validationService;

  public ReaderService(ReaderRepository readerRepository, OwnershipService ownershipService, ValidationService validationService) {
    this.readerRepository = readerRepository;
    this.ownershipService = ownershipService;
    this.validationService = validationService;
  }

  public ResponseEntity<String> shareSnippet(Long ownerId, Long snippetId, Long targetUserId) {
    if (!validationService.isUserIdValid(targetUserId)) {
      return new ResponseEntity<>("Invalid target user ID", HttpStatus.BAD_REQUEST);
    }
    if (!ownershipService.isOwner(ownerId, snippetId)) {
      return new ResponseEntity<>("User is not the owner of the snippet", HttpStatus.FORBIDDEN);
    }
    Reader reader = new Reader(targetUserId, snippetId);
    readerRepository.save(reader);
    return new ResponseEntity<>("Snippet shared successfully", HttpStatus.OK);
  }

  public boolean isReader(Long userId, Long snippetId) {
    return readerRepository.findReaderByUserIdAndSnippetId(userId, snippetId).isPresent();
  }

  public ResponseEntity<String> getReaderPermission(Long userId, Long snippetId) {
    if (!validationService.isUserIdValid(userId)) {
      return new ResponseEntity<>("User isn't valid, it doesn't exists", HttpStatus.BAD_REQUEST);
    }
    if (isReader(userId, snippetId)) {
      return new ResponseEntity<>("User is a reader of the snippet", HttpStatus.OK);
    }
    return new ResponseEntity<>("User is not a reader of the snippet", HttpStatus.FORBIDDEN);
  }
}
