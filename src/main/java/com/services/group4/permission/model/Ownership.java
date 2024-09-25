package com.services.group4.permission.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.services.group4.permission.dto.SnippetDTO;
import jakarta.persistence.*;

@Entity
public class Ownership {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long OwnerShipID;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private SnippetUser user;

    @Transient
    private SnippetDTO snippet;

    public Ownership() {
    }

    public Ownership(SnippetUser user, SnippetDTO snippet) {
        this.user = user;
        this.snippet = snippet;
    }

    public Long getOwnerShipID() {
        return OwnerShipID;
    }

    public void setOwnerShipID(Long id) {
        this.OwnerShipID = id;
    }

    public SnippetUser getUser() {
        return user;
    }

    public void setUser(SnippetUser user) {
        this.user = user;
    }

    public SnippetDTO getSnippet() {
        return snippet;
    }

    public void setSnippet(SnippetDTO snippet) {
        this.snippet = snippet;
    }

  public String toJson() {
    ObjectMapper objectMapper = new ObjectMapper();
    try {
      return objectMapper.writeValueAsString(this);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
      return "{}";
    }
  }
}