package com.services.group4.permission.snippets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.services.group4.permission.dto.SnippetDTO;
import com.services.group4.permission.model.Ownership;
import com.services.group4.permission.model.SnippetUser;
import com.services.group4.permission.repository.OwnershipRepository;
import com.services.group4.permission.service.SnippetService;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;

import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class OwnershipControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SnippetService snippetService;

    @MockBean
    private OwnershipRepository ownershipRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
      Mockito.reset(ownershipRepository);
    }

//  @BeforeAll
//  public static void setup(@Autowired OwnershipRepository ownershipRepository) {
//    OwnershipControllerTest.ownershipRepository = ownershipRepository;
//    ownershipRepository.deleteAll();
//
//    SnippetDTO snippetDTO = new SnippetDTO();
//    snippetDTO.setId(11L);
//    snippetDTO.setTitle("Sample title");
//    snippetDTO.setContent("Sample content");
//
//    SnippetUser user = new SnippetUser("Jane Doe", "password","user@mail.com");
//
//    Ownership ownership = new Ownership(user, snippetDTO);
//
//    ownershipRepository.save(ownership);
//  }

    @Test
    @Order(1)
    public void testCreateOwnership() throws Exception {
      SnippetDTO snippetDTO = new SnippetDTO();
      snippetDTO.setId(12L);
      snippetDTO.setTitle("Sample title 2");
      snippetDTO.setContent("Sample content 2");

      SnippetUser user = new SnippetUser();
      user.setId(12L);
      user.setUsername("John Doe");
      user.setPassword("password2");
      user.setEmail("user2@mail.com");

      Ownership ownership = new Ownership(user, snippetDTO);
      Mockito.when(ownershipRepository.save(Mockito.any(Ownership.class))).thenReturn(ownership);
      ownershipRepository.save(ownership);


      mockMvc.perform(post("/ownership/create")
              .contentType(MediaType.APPLICATION_JSON)
              .content(objectMapper.writeValueAsString(ownership)))
              .andExpect(status().isCreated())
              .andExpect(content().string("OwnerShip relation created"));

      System.out.println("Ownership: " + ownershipRepository.findOwnershipByUser_Username("John Doe").orElse(null));

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
        ownership.setOwnerShipID(1L);
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
        ownership.setOwnerShipID(1L);
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