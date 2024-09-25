package com.services.group4.permission.controller;

import com.services.group4.permission.dto.SnippetDTO;
import com.services.group4.permission.model.Ownership;
import com.services.group4.permission.repository.OwnershipRepository;
import com.services.group4.permission.service.SnippetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ownership")
public class OwnershipController {

    @Autowired
    private OwnershipRepository ownershipRepository;

    @Autowired
    private SnippetService snippetService;

    @PostMapping("")
    public ResponseEntity<String> createOwnership2(@RequestBody Ownership ownership) {
      try {
        SnippetDTO snippet = snippetService.getSnippetById(ownership.getSnippet().getId());
        if (snippet == null) {
          return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        ownershipRepository.save(ownership);
        return new ResponseEntity<>("OwnerShip relation created", HttpStatus.CREATED);
      } catch (Exception e) {
        System.out.println(e.getMessage());
        return new ResponseEntity<>("Something went wrong creating the Snippet",
            HttpStatus.INTERNAL_SERVER_ERROR);
      }
    }

    @PostMapping("/create")
    public ResponseEntity<String> createOwnership(@RequestBody Ownership ownership) {
      ownershipRepository.save(ownership);
      return new ResponseEntity<>("OwnerShip relation created", HttpStatus.CREATED);
    }

    // Otros métodos CRUD según sea necesario
}