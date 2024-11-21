package com.services.group4.permission.controller.test;

import com.services.group4.permission.model.communication.CommunicationMessage;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {

  @GetMapping("/ping")
  public String index() {
    return "pong";
  }

  @GetMapping("/parser/communication")
  public CommunicationMessage testParserCommunication() {
    return new CommunicationMessage("Permission", "Communication from Parser to Permission works!");
  }

  @GetMapping("/snippet/communication")
  public CommunicationMessage testSnippetCommunication() {
    return new CommunicationMessage(
        "Permission", "Communication from Snippet to Permission works!");
  }
}
