package com.services.group4.permission.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import java.time.Instant;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
public class TokenService {

  private static final long PRE_REFRESH_MARGIN_SECONDS = 300; // Refresh 5 minutes before expiration
  private final ReentrantLock lock = new ReentrantLock();
  private final RestTemplate restTemplate;
  private final Cache<String, String> tokenCache;
  private String accessToken;
  private Instant tokenExpiry;

  // Constructor injection
  public TokenService(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
    this.tokenCache =
        Caffeine.newBuilder()
            .expireAfterWrite(24, TimeUnit.HOURS) // Example cache expiration
            .build();
  }

  public String getAccessToken() {
    String cachedToken = tokenCache.getIfPresent("accessToken");
    if (cachedToken != null && isTokenExpiredOrCloseToExpiring()) {
      return cachedToken;
    }

    refreshAccessToken();
    return accessToken;
  }

  private boolean isTokenExpiredOrCloseToExpiring() {
    return tokenExpiry != null
        && !Instant.now().isAfter(tokenExpiry.minusSeconds(PRE_REFRESH_MARGIN_SECONDS));
  }

  private void refreshAccessToken() {
    lock.lock();
    try {
      if (accessToken != null && isTokenExpiredOrCloseToExpiring()) {
        return;
      }

      String url = "https://dev-ybvfkgr1bd82iozp.us.auth0.com/oauth/token";

      HttpEntity<MultiValueMap<String, String>> requestEntity = httpOptions();

      ResponseEntity<String> response =
          restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);

      ObjectMapper mapper = new ObjectMapper();
      JsonNode responseJson = mapper.readTree(response.getBody());
      this.accessToken = responseJson.get("access_token").asText();
      int expiresIn = responseJson.get("expires_in").asInt();
      this.tokenExpiry = Instant.now().plusSeconds(expiresIn);

      // Cache the token
      tokenCache.put("accessToken", accessToken);
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      lock.unlock();
    }
  }

  private @NotNull HttpEntity<MultiValueMap<String, String>> httpOptions() {
    HttpHeaders headers = new HttpHeaders();
    headers.set("Content-Type", "application/x-www-form-urlencoded");

    MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
    body.add("grant_type", "client_credentials");
    body.add("client_id", "G5JC0DlwnrIv7YlY03lqCdBcKqY0RLI4");
    body.add("client_secret", "SD4jJCvJdfGU4UWGtUgD2otbRxAlH3mVTwnzXBNlVIvjv9AR22Llt3XW3m5XTOKZ");
    body.add("audience", "https://dev-ybvfkgr1bd82iozp.us.auth0.com/api/v2/");
    body.add("scope", "read:users read:user_idp_tokens");

    return new HttpEntity<>(body, headers);
  }
}
