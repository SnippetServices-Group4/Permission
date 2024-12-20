package com.services.group4.permission.service;

import com.services.group4.permission.common.FullResponse;
import com.services.group4.permission.dto.ResponseDto;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class PermissionService {

  private final ReaderService readerService;
  private final OwnershipService ownershipService;

  public PermissionService(
      ReaderService readerService,
      OwnershipService ownershipService) {
    this.readerService = readerService;
    this.ownershipService = ownershipService;
  }

  public ResponseEntity<ResponseDto<List<Long>>> getAllowedSnippets(String userId) {
    Optional<List<Long>> readerSnippets = readerService.findSnippetIdsByUserId(userId);
    Optional<List<Long>> ownerSnippets = ownershipService.findSnippetIdsByUserId(userId);
    List<Long> allowedSnippets = new ArrayList<>();

    readerSnippets.ifPresent(allowedSnippets::addAll);
    ownerSnippets.ifPresent(allowedSnippets::addAll);

    return FullResponse.create(
        "User has permission to this snippets", "snippetList", allowedSnippets, HttpStatus.OK);
  }

  public ResponseEntity<ResponseDto<Long>> deletePermissionsOfSnippet(
      String userId, Long snippetId) {

    ResponseEntity<ResponseDto<Long>> responseOwnership =
        ownershipService.deleteOwnership(userId, snippetId);
    ResponseEntity<ResponseDto<Long>> responseReader = readerService.deleteReaders(snippetId);

    if (responseOwnership.getStatusCode().equals(HttpStatus.OK)) {
      if (responseReader.getStatusCode().equals(HttpStatus.OK)) {
        return FullResponse.create(
            "Permissions and shared relations deleted successfully",
            "snippetId",
            snippetId,
            HttpStatus.OK);
      } else if (responseReader.getStatusCode().equals(HttpStatus.FORBIDDEN)) {
        return FullResponse.create(
            "Permissions deleted successfully", "snippetId", snippetId, HttpStatus.OK);
      }
    }

    return responseOwnership;
  }

  public ResponseEntity<ResponseDto<Boolean>> hasPermissionOnSnippet(
      String userId, Long snippetId) {
    ResponseEntity<ResponseDto<Boolean>> readerPermission =
        readerService.getReaderPermission(userId, snippetId);
    ResponseEntity<ResponseDto<Boolean>> ownerPermission =
        ownershipService.hasOwnerPermission(userId, snippetId);
    if (Objects.requireNonNull(ownerPermission.getBody()).data().data()
        || Objects.requireNonNull(readerPermission.getBody()).data().data()) {
      return FullResponse.create(
          "User has permission to this snippet", "permission", true, HttpStatus.OK);
    } else {
      return FullResponse.create(
          "User doesn't have permission to this snippet",
          "permission",
          false,
          HttpStatus.FORBIDDEN);
    }
  }
}
