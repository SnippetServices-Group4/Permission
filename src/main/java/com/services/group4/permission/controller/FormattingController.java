package com.services.group4.permission.controller;

import com.services.group4.permission.common.DataTuple;
import com.services.group4.permission.dto.FormatRulesDto;
import com.services.group4.permission.dto.ResponseDto;
import com.services.group4.permission.dto.UpdateRulesRequestDto;
import com.services.group4.permission.model.FormatConfig;
import com.services.group4.permission.service.FormattingService;
import com.services.group4.permission.service.OwnershipService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/formatting")
public class FormattingController {
  private final FormattingService formattingService;
  private final OwnershipService ownershipService;

  public FormattingController(FormattingService formattingService, OwnershipService ownershipService) {
    this.formattingService = formattingService;
    this.ownershipService = ownershipService;
  }

  @PostMapping("/update/rules")
  public ResponseEntity<ResponseDto<List<Long>>> updateRulesAndFormat(@RequestBody UpdateRulesRequestDto<FormatRulesDto> req) {
    try {
      System.out.println("Updating rules");
      FormatConfig config = formattingService.updateRules(req);

      System.out.println("Getting snippets");
      Optional<List<Long>> snippetsId = ownershipService.findSnippetIdsByUserId(config.getUserId());

      Optional<Integer> snippetsInQueue = Optional.of(0);

      if (snippetsId.isPresent()) {
        System.out.println("Formatting snippets");
        snippetsInQueue = formattingService.asyncFormat(snippetsId.get(), config);
      }

      String message = snippetsInQueue
          .map(i -> "Formatting of " + i + " snippets in progress.")
          .orElse("No snippets to format");

      List<Long> snippetsIds = snippetsId.orElse(List.of());

      return new ResponseEntity<>(new ResponseDto<>(message, new DataTuple<>("snippetsIds", snippetsIds)), HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
}
