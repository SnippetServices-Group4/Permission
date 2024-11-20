package com.services.group4.permission.service;

import com.services.group4.permission.clients.SnippetClient;
import com.services.group4.permission.dto.ResponseDto;
import com.services.group4.permission.dto.snippet.SnippetResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class SnippetService {
    private final SnippetClient snippetClient;

    public SnippetService(SnippetClient snippetClient) {
        this.snippetClient = snippetClient;
    }

    public ResponseEntity<ResponseDto<SnippetResponseDto>> getSnippetInfo(Long snippetId) {
        return snippetClient.getSnippetInfo(snippetId);
    }
}
