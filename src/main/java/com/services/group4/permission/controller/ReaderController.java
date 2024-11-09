package com.services.group4.permission.controller;

import com.services.group4.permission.dto.ResponseDto;
import com.services.group4.permission.model.Reader;
import com.services.group4.permission.repository.ReaderRepository;
import com.services.group4.permission.service.ReaderService;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reader")
public class ReaderController {

  private final ReaderRepository readerRepository;
  private final ReaderService readerService;

  public ReaderController(ReaderRepository readerRepository, ReaderService readerService) {
    this.readerRepository = readerRepository;
    this.readerService = readerService;
  }

  @PostMapping("/create")
  public ResponseEntity<String> addReader(@RequestBody Reader reader) {
    try {
      readerRepository.save(reader);
      return new ResponseEntity<>("Reader created", HttpStatus.CREATED);
    } catch (Exception e) {
      System.out.println(e.getMessage());
      return new ResponseEntity<>(
          "Something went wrong creating the Reader", HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @GetMapping("/user/{id}")
  public ResponseEntity<Reader> getReaderByUserId(@PathVariable Long id) {
    Optional<Reader> reader = readerRepository.findReaderByUserId(id);
    return reader
        .map(value -> new ResponseEntity<>(value, HttpStatus.OK))
        .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
  }

  @GetMapping("/snippet/{id}")
  public ResponseEntity<Reader> getReaderBySnippetId(@PathVariable Long id) {
    Optional<Reader> reader = readerRepository.findReaderBySnippetId(id);
    return reader
        .map(value -> new ResponseEntity<>(value, HttpStatus.OK))
        .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
  }

  @GetMapping
  public ResponseEntity<Iterable<Reader>> getAllReaders() {
    return new ResponseEntity<>(readerRepository.findAll(), HttpStatus.OK);
  }

  // delete
  @DeleteMapping("/delete/{id}")
  public ResponseEntity<String> deleteReader(@PathVariable Long id) {
    try {
      readerRepository.deleteById(id);
      return new ResponseEntity<>("Reader deleted", HttpStatus.OK);
    } catch (Exception e) {
      System.out.println(e.getMessage());
      return new ResponseEntity<>(
          "Something went wrong deleting the Reader", HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }


  // new routes for snippet-service
  // reader/share funciona por postman
  @PostMapping("/share")
  public ResponseEntity<ResponseDto<Long>> shareSnippet(@RequestBody Map<String, Object> requestData) {
    Long ownerId = ((Integer) requestData.get("ownerId")).longValue();
    Long snippetId = ((Integer) requestData.get("snippetId")).longValue();
    Long targetUserId = ((Integer) requestData.get("targetUserId")).longValue();

    return readerService.shareSnippet(ownerId, snippetId, targetUserId);
  }

  // reader/getPermission funciona por postman
  @GetMapping("/permission/{userId}/for/{snippetId}")
  public ResponseEntity<ResponseDto<Boolean>> hasReaderPermission(
      @PathVariable Long userId, @PathVariable Long snippetId) {
    ResponseEntity<String> response = readerService.getReaderPermission(userId, snippetId);
    try {
      if (response.getStatusCode() == HttpStatus.OK) {
        return new ResponseEntity<>(new ResponseDto<>(response.getBody(), true), HttpStatus.OK);
      }
      return new ResponseEntity<>(new ResponseDto<>(response.getBody(),false), response.getStatusCode());
    } catch (Exception e) {
      return new ResponseEntity<>(new ResponseDto<>(e.getMessage(),false), HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @GetMapping("/allowedSnippets/{userId}")
  public ResponseEntity<ResponseDto<List<Long>>> getAllowedSnippets(@PathVariable Long userId) {
    try {
      return readerService.getAllowedSnippets(userId);
    } catch (Exception e) {
      System.out.println("Error: " + e.getMessage());
      return new ResponseEntity<>(new ResponseDto<>(e.getMessage(), null),HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
}
