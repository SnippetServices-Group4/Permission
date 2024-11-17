package com.services.group4.permission.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.services.group4.permission.common.DataTuple;
import com.services.group4.permission.dto.ResponseDto;
import com.services.group4.permission.dto.UserDto;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class Auth0Users {

  private final TokenService tokenService;
  private final RestTemplate restTemplate;

  // Constructor injection
  @Autowired
  public Auth0Users(TokenService tokenService, RestTemplate restTemplate) {
    this.tokenService = tokenService;
    this.restTemplate = restTemplate;
  }

  public ResponseDto<List<UserDto>> getUsers() {
    // Get the access token from TokenService
    String accessToken = tokenService.getAccessToken();

    // Set the Authorization header with the access token
    HttpHeaders headers = new HttpHeaders();
    headers.set("Authorization", "Bearer " + accessToken);
    headers.set("Accept", "application/json");

    // Make the GET request to the /users endpoint
    String url = "https://dev-ybvfkgr1bd82iozp.us.auth0.com/api/v2/users";
    HttpEntity<String> entity = new HttpEntity<>(headers);

    ResponseEntity<String> response =
        restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

    // Parse the response and map to UserDto
    List<UserDto> userDtos = new ArrayList<>();
    try {
      ObjectMapper mapper = new ObjectMapper();
      JsonNode root = mapper.readTree(response.getBody());
      for (JsonNode node : root) {
        String userId = node.get("user_id").asText();
        String username = node.get("given_name").asText();
        userDtos.add(new UserDto(userId, username));
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    // Return the ResponseDto
    return new ResponseDto<>("Users retrieved successfully", new DataTuple<>("users", userDtos));
  }
}
