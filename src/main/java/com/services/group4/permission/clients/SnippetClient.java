package com.services.group4.permission.clients;

import com.services.group4.permission.dto.ResponseDto;
import com.services.group4.permission.dto.snippet.SnippetResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "snippet", url = "${snippet.service.url}")
public interface SnippetClient {
    @RequestMapping(method = RequestMethod.GET, value = "/snippets/{snippetId}")
    ResponseEntity<ResponseDto<SnippetResponseDto>> getSnippetInfo(@PathVariable Long snippetId);
}
