package com.services.group4.permission.service;

import com.services.group4.permission.model.Reader;
import com.services.group4.permission.repository.ReaderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ReaderService {

  private ReaderRepository readerRepository;
  private OwnershipService ownershipService;
  private ValidationService validationService;

  public ResponseEntity<Reader> createReader(Long userId, Long snippetId) {
    if (!validationService.isUserIdValid(userId)) {
      return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    }
    Reader reader = new Reader(userId, snippetId);
    readerRepository.save(reader);
    return new ResponseEntity<>(reader, HttpStatus.CREATED);
  }

  public ResponseEntity<String> shareSnippet(Long ownerId, Long snippetId, Long targetUserId) {
    if (!ownershipService.isOwner(ownerId, snippetId)) {
      return new ResponseEntity<>("User is not the owner of the snippet", HttpStatus.FORBIDDEN);
    }
    if (!validationService.isUserIdValid(targetUserId)) {
      return new ResponseEntity<>("Invalid target user ID", HttpStatus.BAD_REQUEST);
    }
    Reader reader = new Reader(targetUserId, snippetId);
    readerRepository.save(reader);
    return new ResponseEntity<>("Snippet shared successfully", HttpStatus.OK);
  }
}