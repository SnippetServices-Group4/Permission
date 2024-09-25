package com.services.group4.permission.service;

import com.services.group4.permission.dto.SnippetDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class SnippetService {

    @Autowired
    private RestTemplate restTemplate;

  private final String SNIPPET_SERVICE_URL = "http://localhost:8080/snippets";

    public SnippetDTO getSnippetById(Long id) {
        return restTemplate.getForObject(SNIPPET_SERVICE_URL + "/" + id, SnippetDTO.class);
    }

    // Otros métodos para consumir el servicio REST según sea necesario
}