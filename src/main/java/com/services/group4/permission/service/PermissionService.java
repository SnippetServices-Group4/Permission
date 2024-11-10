package com.services.group4.permission.service;

import com.services.group4.permission.common.FullResponse;
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

  public ResponseEntity<ResponseDto<List<Long>>> getAllowedSnippets(String userId) {
    if (!validationService.isUserIdValid(userId)) {
        return FullResponse.create("User isn't valid, it doesn't exists", "snippetList", null, HttpStatus.BAD_REQUEST);
    }

    Optional<List<Long>> readerSnippets = readerService.findSnippetIdsByUserId(userId);
    Optional<List<Long>> ownerSnippets = ownershipService.findSnippetIdsByUserId(userId);
    List<Long> allowedSnippets = new ArrayList<>();

    readerSnippets.ifPresent(allowedSnippets::addAll);
    ownerSnippets.ifPresent(allowedSnippets::addAll);

    return FullResponse.create("User has permission to this snippets", "snippetList", allowedSnippets, HttpStatus.OK);
  }

  public ResponseEntity<ResponseDto<Long>> deletePermissionsOfSnippet(String userId, Long snippetId) {
    if (!validationService.isUserIdValid(userId)) {
      return FullResponse.create("User isn't valid, it doesn't exists", "snippetId", snippetId, HttpStatus.BAD_REQUEST);
    }

    ResponseEntity<ResponseDto<Long>> responseOwnership = ownershipService.deleteOwnership(userId, snippetId);
    ResponseEntity<ResponseDto<Long>> responseReader = readerService.deleteReaders(snippetId);

    if (responseOwnership.getStatusCode().equals(HttpStatus.OK)){
      if (responseReader.getStatusCode().equals(HttpStatus.OK)) {
        return FullResponse.create("Permissions and shared relations deleted successfully", "snippetId", snippetId, HttpStatus.OK);
      } else if (responseReader.getStatusCode().equals(HttpStatus.FORBIDDEN)) {
        return FullResponse.create("Permissions deleted successfully", "snippetId", snippetId, HttpStatus.OK);
      }
    }

    return responseOwnership;
  }

  public ResponseEntity<ResponseDto<Boolean>> hasPermissionOnSnippet(String userId, Long snippetId) {
    if (!validationService.isUserIdValid(userId)) {
      return FullResponse.create("User isn't valid, it doesn't exists", "permission", null, HttpStatus.BAD_REQUEST);
    }

    ResponseEntity<ResponseDto<Boolean>> readerPermission = readerService.getReaderPermission(userId, snippetId);
    ResponseEntity<ResponseDto<Boolean>> ownerPermission = ownershipService.hasOwnerPermission(userId, snippetId);
    if (Objects.requireNonNull(ownerPermission.getBody()).data().data() ||
        Objects.requireNonNull(readerPermission.getBody()).data().data()) {
      return FullResponse.create("User has permission to this snippet", "permission", true, HttpStatus.OK);
    } else {
      return FullResponse.create("User doesn't have permission to this snippet", "permission", false, HttpStatus.FORBIDDEN);
    }
  }
}
