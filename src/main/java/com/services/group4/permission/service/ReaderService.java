package com.services.group4.permission.service;

import com.services.group4.permission.dto.ResponseDto;
import com.services.group4.permission.model.Ownership;
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

  public ResponseEntity<ResponseDto<Long>> shareSnippet(Long ownerId, Long snippetId, Long targetUserId) {
    if (!validationService.isUserIdValid(targetUserId)) {
      return new ResponseEntity<>(new ResponseDto<>("User isn't valid, it doesn't exists", ownerId), HttpStatus.BAD_REQUEST);
    }
    if (!ownershipService.isOwner(ownerId, snippetId)) {
      return new ResponseEntity<>(new ResponseDto<>("User is not the owner of the snippet", ownerId), HttpStatus.FORBIDDEN);
    }
    Reader reader = new Reader(targetUserId, snippetId);
    readerRepository.save(reader);
    return new ResponseEntity<>(new ResponseDto<>("Snippet shared successfully", targetUserId), HttpStatus.OK);
  }

  public boolean isReader(Long userId, Long snippetId) {
    return readerRepository.findReaderByUserIdAndSnippetId(userId, snippetId).isPresent();
  }

  public boolean hasReader(Long snippetId) {
    return readerRepository.findReadersBySnippetId(snippetId).isPresent();
  }

  public ResponseEntity<ResponseDto<Boolean>> getReaderPermission(Long userId, Long snippetId) {
    if (!validationService.isUserIdValid(userId)) {
      return new ResponseEntity<>(new ResponseDto<>("User isn't valid, it doesn't exists", false), HttpStatus.BAD_REQUEST);
    }
    if (isReader(userId, snippetId)) {
      return new ResponseEntity<>(new ResponseDto<>("User is a reader of the snippet", true), HttpStatus.OK);
    }
    return new ResponseEntity<>(new ResponseDto<>("User is not a reader of the snippet", false), HttpStatus.FORBIDDEN);
  }

  public Optional<List<Long>> findSnippetIdsByUserId(Long userId) {
    return readerRepository.findSnippetIdByUserId(userId);
  }

  public ResponseEntity<ResponseDto<Long>> deleteReaders(Long snippetId) {
    if (hasReader(snippetId)) {
      Optional<List<Reader>> readers = readerRepository.findReadersBySnippetId(snippetId);
      if (readers.isPresent()) {
        readerRepository.deleteAll(readers.get());
        return new ResponseEntity<>(new ResponseDto<>("Readers deleted", snippetId), HttpStatus.OK);
      }
    }
    return new ResponseEntity<>(new ResponseDto<>("Snippet wasn't shared with other users", snippetId), HttpStatus.NO_CONTENT);
  }
}
