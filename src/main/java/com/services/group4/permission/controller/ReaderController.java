package com.services.group4.permission.controller;

import com.services.group4.permission.model.Reader;
import com.services.group4.permission.repository.ReaderRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/reader")
public class ReaderController {

  private final ReaderRepository readerRepository;

  public ReaderController(ReaderRepository readerRepository) {
    this.readerRepository = readerRepository;
  }


  @PostMapping("/create")
  public ResponseEntity<String> addReader(@RequestBody Reader reader) {
    try {
      readerRepository.save(reader);
      return new ResponseEntity<>("Reader created", HttpStatus.CREATED);
    } catch (Exception e) {
      System.out.println(e.getMessage());
      return new ResponseEntity<>("Something went wrong creating the Reader",
              HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @GetMapping("/user/{id}")
  public ResponseEntity<Reader> getReaderByUserId(@PathVariable Long id) {
    Optional<Reader> reader = readerRepository.findReaderByUserId(id);
    return reader.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                    .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
  }

  @GetMapping("/snippet/{id}")
  public ResponseEntity<Reader> getReaderBySnippetId(@PathVariable Long id) {
    Optional<Reader> reader = readerRepository.findReaderBySnippetId(id);
    return reader.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
        .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
  }

  @GetMapping("/{id}")
  public ResponseEntity<Reader> getReaderById(@PathVariable Long id) {
    Optional<Reader> reader = readerRepository.findById(id);
    return reader.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
        .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
  }

  @GetMapping
  public ResponseEntity<Iterable<Reader>> getAllReaders() {
    return new ResponseEntity<>(readerRepository.findAll(), HttpStatus.OK);
  }

  //delete
  @DeleteMapping("/delete/{id}")
  public ResponseEntity<String> deleteReader(@PathVariable Long id) {
    try {
      readerRepository.deleteById(id);
      return new ResponseEntity<>("Reader deleted", HttpStatus.OK);
    } catch (Exception e) {
      System.out.println(e.getMessage());
      return new ResponseEntity<>("Something went wrong deleting the Reader",
              HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
}