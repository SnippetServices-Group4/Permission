package com.services.group4.permission.controller;

import com.services.group4.permission.model.Reader;
import com.services.group4.permission.repository.ReaderRepository;
import com.services.group4.permission.service.ReaderService;

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

  @PostMapping("/share")
  public ResponseEntity<String> shareSnippet(
      @RequestParam Long ownerId, @RequestParam Long snippetId, @RequestParam Long targetUserId) {
    return readerService.shareSnippet(ownerId, snippetId, targetUserId);
  }

  // new routes for snippet-service
  // reader/getPermission funciona por postman
  @GetMapping("/getPermission")
  public ResponseEntity<Boolean> getReaderPermission(@RequestBody Map<String, Object> requestData) {
    Long userId = ((Integer) requestData.get("userId")).longValue();
    Long snippetId = ((Integer) requestData.get("snippetId")).longValue();
    ResponseEntity<String> response = readerService.getReaderPermission(userId, snippetId);
    try {
      if (response.getStatusCode() == HttpStatus.OK) {
        return new ResponseEntity<>(true, HttpStatus.OK);
      }
      return new ResponseEntity<>(false, response.getStatusCode());
    } catch (Exception e) {
      System.out.println(e.getMessage());
      return new ResponseEntity<>(false, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }


}
