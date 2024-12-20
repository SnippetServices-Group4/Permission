package com.services.group4.permission.service;

import com.services.group4.permission.clients.ParserClient;
import com.services.group4.permission.dto.FormatRulesDto;
import com.services.group4.permission.dto.FormattingRequestDto;
import com.services.group4.permission.dto.ResponseDto;
import com.services.group4.permission.dto.snippet.SnippetResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ParserService {
  private final ParserClient parserClient;

  public ParserService(ParserClient parserClient) {
    this.parserClient = parserClient;
  }

  public ResponseEntity<ResponseDto<Object>> runFormatting(
      SnippetResponseDto snippet, FormatRulesDto formatRules) {
    log.info(
        "Redirecting formatting for snippet with id: {}",
        snippet.snippetId() + "to parser service");
    FormattingRequestDto formattingRequest =
        new FormattingRequestDto(
            formatRules, snippet.language().getLangName(), snippet.language().getVersion());
    return parserClient.runFormatting(formattingRequest, snippet.snippetId());
  }
}
