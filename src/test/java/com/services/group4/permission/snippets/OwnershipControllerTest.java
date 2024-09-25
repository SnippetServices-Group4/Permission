package com.services.group4.permission.snippets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.services.group4.permission.controller.OwnershipController;
import com.services.group4.permission.dto.SnippetDTO;
import com.services.group4.permission.model.Ownership;
import com.services.group4.permission.model.SnippetUser;
import com.services.group4.permission.repository.OwnershipRepository;
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

@WebMvcTest(OwnershipController.class)
public class OwnershipControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SnippetService snippetService;

    @MockBean
    private OwnershipRepository ownershipRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testCreateOwnership() throws Exception {
        SnippetDTO snippetDTO = new SnippetDTO();
        snippetDTO.setId(1L);
        snippetDTO.setContent("Sample content");

        SnippetUser user = new SnippetUser();
        user.setId(1L);
        user.setUsername("John Doe");

        Ownership ownership = new Ownership();
        ownership.setId(1L);
        ownership.setUser(user);
        ownership.setSnippet(snippetDTO);

        Mockito.when(snippetService.getSnippetById(1L)).thenReturn(snippetDTO);
        Mockito.when(ownershipRepository.save(Mockito.any(Ownership.class))).thenReturn(ownership);

        mockMvc.perform(post("/ownership")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(ownership)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.user.id").value(1L))
                .andExpect(jsonPath("$.user.name").value("John Doe"))
                .andExpect(jsonPath("$.snippet.id").value(1L))
                .andExpect(jsonPath("$.snippet.content").value("Sample content"));
    }

    @Test
    public void testGetOwnershipById() throws Exception {
        SnippetDTO snippetDTO = new SnippetDTO();
        snippetDTO.setId(1L);
        snippetDTO.setContent("Sample content");

        SnippetUser user = new SnippetUser();
        user.setId(1L);
        user.setUsername("John Doe");

        Ownership ownership = new Ownership();
        ownership.setId(1L);
        ownership.setUser(user);
        ownership.setSnippet(snippetDTO);

        Mockito.when(ownershipRepository.findById(1L)).thenReturn(Optional.of(ownership));

        mockMvc.perform(get("/ownership/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.user.id").value(1L))
                .andExpect(jsonPath("$.user.name").value("John Doe"))
                .andExpect(jsonPath("$.snippet.id").value(1L))
                .andExpect(jsonPath("$.snippet.content").value("Sample content"));
    }

    @Test
    public void testUpdateOwnership() throws Exception {
        SnippetDTO snippetDTO = new SnippetDTO();
        snippetDTO.setId(1L);
        snippetDTO.setContent("Updated content");

        SnippetUser user = new SnippetUser();
        user.setId(1L);
        user.setUsername("John Doe");

        Ownership ownership = new Ownership();
        ownership.setId(1L);
        ownership.setUser(user);
        ownership.setSnippet(snippetDTO);

        Mockito.when(ownershipRepository.findById(1L)).thenReturn(Optional.of(ownership));
        Mockito.when(ownershipRepository.save(Mockito.any(Ownership.class))).thenReturn(ownership);

        mockMvc.perform(put("/ownership/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(ownership)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.user.id").value(1L))
                .andExpect(jsonPath("$.user.name").value("John Doe"))
                .andExpect(jsonPath("$.snippet.id").value(1L))
                .andExpect(jsonPath("$.snippet.content").value("Updated content"));
    }

    @Test
    public void testDeleteOwnership() throws Exception {
        Mockito.doNothing().when(ownershipRepository).deleteById(1L);

        mockMvc.perform(delete("/ownership/1"))
                .andExpect(status().isNoContent());
    }
}