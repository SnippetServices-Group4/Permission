package com.services.group4.permission.controller;

import com.services.group4.permission.dto.SnippetDTO;
import com.services.group4.permission.model.Collaborator;
import com.services.group4.permission.repository.CollaboratorRepository;
import com.services.group4.permission.service.SnippetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/collaborator")
public class CollaboratorController {

    private final CollaboratorRepository collaboratorRepository;

    @Autowired
    private SnippetService snippetService;

    public CollaboratorController(CollaboratorRepository collaboratorRepository) {
        this.collaboratorRepository = collaboratorRepository;
    }

    @PostMapping
    public ResponseEntity<Collaborator> createCollaborator(@RequestBody Collaborator collaborator) {
        SnippetDTO snippet = snippetService.getSnippetById(collaborator.getSnippet().getId());
        if (snippet == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        collaborator.setSnippet(snippet);
        Collaborator savedCollaborator = collaboratorRepository.save(collaborator);
        return new ResponseEntity<>(savedCollaborator, HttpStatus.CREATED);
    }

    // Otros métodos CRUD según sea necesario
}