package com.services.group4.permission.utils;

import org.springframework.web.client.RestTemplate;

public class ModuleChecker {
  public static boolean isSnippetModuleRunning() {
    try {
      RestTemplate restTemplate = new RestTemplate();
      restTemplate.getForObject("http://localhost:8080/test/permission/communication", String.class);
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  public static boolean isParserModuleRunning() {
    try {
      RestTemplate restTemplate = new RestTemplate();
      restTemplate.getForObject("http://localhost:8082/test/permission/communication", String.class);
      return true;
    } catch (Exception e) {
      return false;
    }
  }
}
