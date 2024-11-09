package com.services.group4.permission.service;

import com.services.group4.permission.model.Reader;
import com.services.group4.permission.repository.ReaderRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
      return new ResponseEntity<>("User isn't valid, it doesn't exists", HttpStatus.BAD_REQUEST);
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

  public ResponseEntity<List<Long>> getAllowedSnippets(Long userId) {
    if (!validationService.isUserIdValid(userId)) {
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    try {
        Optional<List<Long>> readerSnippets = readerRepository.findSnippetIdByUserId(userId);
        Optional<List<Long>> ownerSnippets = ownershipService.findSnippetIdsByUserId(userId);
        List<Long> allowedSnippets = new ArrayList<>();

        readerSnippets.ifPresent(allowedSnippets::addAll);
        ownerSnippets.ifPresent(allowedSnippets::addAll);

        return new ResponseEntity<>(allowedSnippets, HttpStatus.OK);
    } catch (Exception e) {
        System.out.println("Error: " + e.getMessage());
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
}
