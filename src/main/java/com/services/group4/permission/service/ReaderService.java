package com.services.group4.permission.service;

import com.services.group4.permission.common.FullResponse;
import com.services.group4.permission.dto.ResponseDto;
import com.services.group4.permission.model.Reader;
import com.services.group4.permission.repository.ReaderRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ReaderService {

  private final ReaderRepository readerRepository;
  private final OwnershipService ownershipService;
  private final ValidationService validationService;

  public ReaderService(
      ReaderRepository readerRepository,
      OwnershipService ownershipService,
      ValidationService validationService) {
    this.readerRepository = readerRepository;
    this.ownershipService = ownershipService;
    this.validationService = validationService;
  }

  public ResponseEntity<ResponseDto<String>> shareSnippet(
      String ownerId, Long snippetId, String targetUserId) {
    if (!validationService.isUserIdValid(targetUserId)) {
      return FullResponse.create(
          "User isn't valid, it doesn't exists", "ownerId", ownerId, HttpStatus.BAD_REQUEST);
    }
    if (!ownershipService.isOwner(ownerId, snippetId)) {
      return FullResponse.create(
          "User is not the owner of the snippet", "ownerId", ownerId, HttpStatus.FORBIDDEN);
    }
    Reader reader = new Reader(targetUserId, snippetId);
    readerRepository.save(reader);
    return FullResponse.create(
        "Snippet shared successfully", "targetUserId", targetUserId, HttpStatus.OK);
  }

  public boolean isReader(String userId, Long snippetId) {
    return readerRepository.findReaderByUserIdAndSnippetId(userId, snippetId).isPresent();
  }

  public boolean hasReader(Long snippetId) {
    return readerRepository.findReadersBySnippetId(snippetId).isPresent();
  }

  public ResponseEntity<ResponseDto<Boolean>> getReaderPermission(String userId, Long snippetId) {
    if (!validationService.isUserIdValid(userId)) {
      return FullResponse.create(
          "User isn't valid, it doesn't exists", "readerPermission", false, HttpStatus.BAD_REQUEST);
    }
    if (isReader(userId, snippetId)) {
      return FullResponse.create(
          "User is a reader of the snippet", "readerPermission", true, HttpStatus.OK);
    }
    return FullResponse.create(
        "User is not a reader of the snippet", "readerPermission", false, HttpStatus.FORBIDDEN);
  }

  public Optional<List<Long>> findSnippetIdsByUserId(String userId) {
    return readerRepository.findSnippetIdByUserId(userId);
  }

  public ResponseEntity<ResponseDto<Long>> deleteReaders(Long snippetId) {
    if (hasReader(snippetId)) {
      Optional<List<Reader>> readers = readerRepository.findReadersBySnippetId(snippetId);
      if (readers.isPresent()) {
        readerRepository.deleteAll(readers.get());
        return FullResponse.create("Readers deleted", "snippetId", snippetId, HttpStatus.OK);
      }
    }
    return FullResponse.create(
        "Snippet wasn't shared with other users", "snippetId", snippetId, HttpStatus.NO_CONTENT);
  }
}
