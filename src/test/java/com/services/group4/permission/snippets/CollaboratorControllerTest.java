package com.services.group4.permission.snippets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.services.group4.permission.controller.CollaboratorController;
import com.services.group4.permission.dto.SnippetDTO;
import com.services.group4.permission.model.Collaborator;
import com.services.group4.permission.repository.CollaboratorRepository;
import com.services.group4.permission.service.SnippetService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CollaboratorController.class)
public class CollaboratorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SnippetService snippetService;

    @MockBean
    private CollaboratorRepository collaboratorRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testCreateCollaborator() throws Exception {
        SnippetDTO snippetDTO = new SnippetDTO();
        snippetDTO.setId(1L);
        snippetDTO.setContent("Sample content");

        Collaborator collaborator = new Collaborator();
        collaborator.setId(1L);
        collaborator.setSnippet(snippetDTO);

        Mockito.when(snippetService.getSnippetById(1L)).thenReturn(snippetDTO);
        Mockito.when(collaboratorRepository.save(Mockito.any(Collaborator.class))).thenReturn(collaborator);

        mockMvc.perform(post("/collaborator")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(collaborator)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.snippet.id").value(1L))
                .andExpect(jsonPath("$.snippet.content").value("Sample content"));
    }

    @Test
    public void testGetCollaboratorById() throws Exception {
        SnippetDTO snippetDTO = new SnippetDTO();
        snippetDTO.setId(1L);
        snippetDTO.setContent("Sample content");

        Collaborator collaborator = new Collaborator();
        collaborator.setId(1L);
        collaborator.setSnippet(snippetDTO);

        Mockito.when(collaboratorRepository.findById(1L)).thenReturn(Optional.of(collaborator));

        mockMvc.perform(get("/collaborator/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.snippet.id").value(1L))
                .andExpect(jsonPath("$.snippet.content").value("Sample content"));
    }

    @Test
    public void testUpdateCollaborator() throws Exception {
        SnippetDTO snippetDTO = new SnippetDTO();
        snippetDTO.setId(1L);
        snippetDTO.setContent("Updated content");

        Collaborator collaborator = new Collaborator();
        collaborator.setId(1L);
        collaborator.setSnippet(snippetDTO);

        Mockito.when(collaboratorRepository.findById(1L)).thenReturn(Optional.of(collaborator));
        Mockito.when(collaboratorRepository.save(Mockito.any(Collaborator.class))).thenReturn(collaborator);

        mockMvc.perform(put("/collaborator/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(collaborator)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.snippet.id").value(1L))
                .andExpect(jsonPath("$.snippet.content").value("Updated content"));
    }

    @Test
    public void testDeleteCollaborator() throws Exception {
        Mockito.doNothing().when(collaboratorRepository).deleteById(1L);

        mockMvc.perform(delete("/collaborator/1"))
                .andExpect(status().isNoContent());
    }
}