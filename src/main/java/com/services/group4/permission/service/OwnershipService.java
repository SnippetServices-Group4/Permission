package com.services.group4.permission.service;

import com.services.group4.permission.model.Ownership;
import com.services.group4.permission.repository.OwnershipRepository;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class OwnershipService {

  @Autowired private RestTemplate restTemplate;

  @Autowired private OwnershipRepository ownershipRepository;

  public ResponseEntity<Ownership> createSnippetAndOwnership(
      Long userId, Map<String, Object> snippetData) {
    try {
      // Enviar solicitud al microservicio de Snippets
      String snippetServiceUrl = "http://snippet-service/snippets/create";
      ResponseEntity<Map> response =
          restTemplate.postForEntity(snippetServiceUrl, snippetData, Map.class);

      // Verificar si la creación del snippet fue exitosa
      if (response.getStatusCode() == HttpStatus.CREATED) {
        Map<String, Object> createdSnippet = response.getBody();
        if (createdSnippet != null && createdSnippet.containsKey("snippetID")) {
          // Crear y guardar el ownership
          Long snippetId = ((Number) createdSnippet.get("snippetID")).longValue();
          Ownership ownership = new Ownership(userId, snippetId);
          ownershipRepository.save(ownership);
          return new ResponseEntity<>(ownership, HttpStatus.CREATED);
        }
      }
      return new ResponseEntity<>(null, response.getStatusCode());
    } catch (Exception e) {
      System.out.println(e.getMessage());
      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
}
