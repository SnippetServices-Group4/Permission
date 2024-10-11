package com.services.group4.permission.model;

import jakarta.persistence.*;

@Entity
@Table
public class SnippetUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userID;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String email;

    public SnippetUser() {
    }

    // Added for testing purposes
    public SnippetUser(Long userID, String username, String password, String email) {
      this.userID = userID;
      this.username = username;
      this.password = password;
      this.email = email;
    }

    public SnippetUser(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public Long getUserID() {
        return userID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
