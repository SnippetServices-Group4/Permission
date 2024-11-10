package com.services.group4.permission.service;

import com.services.group4.permission.dto.ResponseDto;
import com.services.group4.permission.model.Reader;
import com.services.group4.permission.repository.ReaderRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class PermissionService {

  private final ReaderService readerService;
  private final OwnershipService ownershipService;
  private final ValidationService validationService;

  public PermissionService(ReaderService readerService, OwnershipService ownershipService, ValidationService validationService) {
    this.readerService = readerService;
    this.ownershipService = ownershipService;
    this.validationService = validationService;
  }

  public ResponseEntity<ResponseDto<List<Long>>> getAllowedSnippets(Long userId) {
    if (!validationService.isUserIdValid(userId)) {
        return new ResponseEntity<>(new ResponseDto<>("User isn't valid, it doesn't exists", null),HttpStatus.BAD_REQUEST);
    }

    Optional<List<Long>> readerSnippets = readerService.findSnippetIdsByUserId(userId);
    Optional<List<Long>> ownerSnippets = ownershipService.findSnippetIdsByUserId(userId);
    List<Long> allowedSnippets = new ArrayList<>();

    readerSnippets.ifPresent(allowedSnippets::addAll);
    ownerSnippets.ifPresent(allowedSnippets::addAll);

    return new ResponseEntity<>(new ResponseDto<>("User has permission to this snippets", allowedSnippets), HttpStatus.OK);
  }

  public ResponseEntity<ResponseDto<Long>> deletePermissionsOfSnippet(Long userId, Long snippetId) {
    //TODO: delete in process
    if (!validationService.isUserIdValid(userId)) {
      return new ResponseEntity<>(new ResponseDto<>("User isn't valid, it doesn't exists", userId), HttpStatus.BAD_REQUEST);
    }

    ResponseEntity<ResponseDto<Long>> responseOwnership = ownershipService.deleteOwnership(userId, snippetId);
    ResponseEntity<ResponseDto<Long>> responseReader = readerService.deleteReaders(snippetId);

    if (responseOwnership.getStatusCode().equals(HttpStatus.OK)){
      if (responseReader.getStatusCode().equals(HttpStatus.OK)) {
        return new ResponseEntity<>(new ResponseDto<>("Permissions and shared relations deleted successfully", snippetId), HttpStatus.OK);
      } else if (responseReader.getStatusCode().equals(HttpStatus.FORBIDDEN)) {
        return new ResponseEntity<>(new ResponseDto<>("Permissions deleted successfully", snippetId), HttpStatus.OK);
      }
    }

    return responseOwnership;
  }

  public ResponseEntity<ResponseDto<Boolean>> hasPermissionOnSnippet(Long userId, Long snippetId) {
    if (!validationService.isUserIdValid(userId)) {
      return new ResponseEntity<>(new ResponseDto<>("User isn't valid, it doesn't exists", null),HttpStatus.BAD_REQUEST);
    }

    ResponseEntity<ResponseDto<Boolean>> readerPermission = readerService.getReaderPermission(userId, snippetId);
    ResponseEntity<ResponseDto<Boolean>> ownerPermission = ownershipService.hasOwnerPermission(userId, snippetId);
    if (Objects.requireNonNull(ownerPermission.getBody()).data() ||
        Objects.requireNonNull(readerPermission.getBody()).data()) {
      return new ResponseEntity<>(new ResponseDto<>("User has permission to this snippet", true), HttpStatus.OK);
    } else {
      return new ResponseEntity<>(new ResponseDto<>("User doesn't have permission to this snippet", false), HttpStatus.FORBIDDEN);
    }


  }
}
