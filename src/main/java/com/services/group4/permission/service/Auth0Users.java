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

  public ResponseDto<List<UserDto>> getUsers(String excludeUserId) {
    String accessToken = tokenService.getAccessToken();
    HttpHeaders headers = new HttpHeaders();
    headers.set("Authorization", "Bearer " + accessToken);
    headers.set("Accept", "application/json");

    List<UserDto> userDtos = new ArrayList<>();
    boolean hasMoreUsers = true;
    int page = 0;
    int perPage = 50;

    try {
      while (hasMoreUsers) {
        String url = String.format("https://dev-ybvfkgr1bd82iozp.us.auth0.com/api/v2/users?page=%d&per_page=%d", page, perPage);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(response.getBody());

        if (root.isArray() && !root.isEmpty()) {
          for (JsonNode node : root) {
            String userId = node.get("user_id").asText();
            if (!userId.equals(excludeUserId)) {
              String username = node.has("given_name") ? node.get("given_name").asText() : node.get("nickname").asText();
              userDtos.add(new UserDto(userId, username));
            }
          }
          page++;
        } else {
          hasMoreUsers = false;
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    return new ResponseDto<>("Users retrieved successfully", new DataTuple<>("users", userDtos));
  }

}
