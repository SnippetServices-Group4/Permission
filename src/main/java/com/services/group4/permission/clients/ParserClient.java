package com.services.group4.permission.clients;

import com.services.group4.permission.dto.FormattingRequestDto;
import com.services.group4.permission.dto.ResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "parser", url = "${parser.service.url}")
public interface ParserClient {
    @RequestMapping(method = RequestMethod.POST, value = "/parsers/format/{snippetId}")
    ResponseEntity<ResponseDto<Object>>runFormatting(@RequestBody FormattingRequestDto formatRequest, @PathVariable Long snippetId);
}
