package com.services.group4.permission.controller;

import com.services.group4.permission.model.Ownership;
import com.services.group4.permission.repository.OwnershipRepository;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ownership")
public class OwnershipController {

  private final OwnershipRepository ownershipRepository;

  public OwnershipController(OwnershipRepository ownershipRepository) {
    this.ownershipRepository = ownershipRepository;
  }


  @PostMapping("/create")
  public ResponseEntity<String> addOwnership(@RequestBody Ownership ownership) {
    try {
      ownershipRepository.save(ownership);
      return new ResponseEntity<>("Ownership created", HttpStatus.CREATED);
    } catch (Exception e) {
      System.out.println(e.getMessage());
      return new ResponseEntity<>("Something went wrong creating the ownership",
              HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @GetMapping("/{id}")
  public ResponseEntity<Ownership> getOwnershipById(@PathVariable Long id) {
    return ownershipRepository.findById(id)
            .map(value -> new ResponseEntity<>(value, HttpStatus.OK))
            .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
  }

  @GetMapping
  public ResponseEntity<Iterable<Ownership>> getAllOwnerships() {
    return new ResponseEntity<>(ownershipRepository.findAll(), HttpStatus.OK);
  }

  //delete
  @DeleteMapping("/delete/{id}")
  public ResponseEntity<String> deleteOwnership(@PathVariable Long id) {
    try {
      ownershipRepository.deleteById(id);
      return new ResponseEntity<>("Ownership deleted", HttpStatus.OK);
    } catch (Exception e) {
      System.out.println(e.getMessage());
      return new ResponseEntity<>("Something went wrong deleting the ownership",
              HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
}