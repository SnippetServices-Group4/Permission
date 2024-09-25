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

    @PostMapping
    public ResponseEntity<Ownership> createOwnership(@RequestBody Ownership ownership) {
        SnippetDTO snippet = snippetService.getSnippetById(ownership.getSnippet().getId());
        if (snippet == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Ownership savedOwnership = ownershipRepository.save(ownership);
        return new ResponseEntity<>(savedOwnership, HttpStatus.CREATED);
    }

    // Otros métodos CRUD según sea necesario
}