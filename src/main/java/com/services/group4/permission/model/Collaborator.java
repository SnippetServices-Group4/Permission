package com.services.group4.permission.model;

import com.services.group4.permission.dto.SnippetDTO;
import jakarta.persistence.*;

@Entity
public class Collaborator {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private SnippetUser user;

    @Transient
    private SnippetDTO snippet;

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
}