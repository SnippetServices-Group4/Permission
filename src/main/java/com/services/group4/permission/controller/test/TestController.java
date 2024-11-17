package com.services.group4.permission.controller.test;

import com.services.group4.permission.model.communication.CommunicationMessage;
import com.services.group4.permission.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/test")
public class TestController {

  private final RestTemplate restTemplate;

  @Autowired
  public TestController(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }

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

  @GetMapping("/getToken")
  public String getToken() {
    return new TokenService(restTemplate).getAccessToken();
  }

  @GetMapping("/getAllUsers")
  public String getAllUsers() {

    return restTemplate.getForObject("http://localhost:8081/user", String.class);
  }
}