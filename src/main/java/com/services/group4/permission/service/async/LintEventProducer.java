package com.services.group4.permission.service.async;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.services.group4.permission.model.LintConfig;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.stream.ObjectRecord;
import org.springframework.data.redis.connection.stream.StreamRecords;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class LintEventProducer {
  private final String streamKey;
  private final RedisTemplate<String, String> redis;

  @Autowired
  public LintEventProducer(
      @Value("${stream.lint.key}") String streamKey, @NotNull RedisTemplate<String, String> redis) {
    this.streamKey = streamKey;
    this.redis = redis;
  }

  public void emit(EventMessage product) {
    ObjectRecord<String, EventMessage> result =
        StreamRecords.newRecord().ofObject(product).withStreamKey(streamKey);

    redis.opsForStream().add(result);
  }

  public void publishEvent(Long snippetId, LintConfig config) {
    ObjectMapper mapper = new ObjectMapper();
    try {
      String jsonPayloadString = mapper.writeValueAsString(config);
      EventMessage product = new EventMessage(snippetId, jsonPayloadString);
      emit(product);
    } catch (Exception e) {
      System.err.println("Error serializing jsonPayload: " + e.getMessage());
    }
  }
}
