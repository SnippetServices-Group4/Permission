package com.services.group4.permission.service.async;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.services.group4.permission.model.FormatConfig;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.stream.ObjectRecord;
import org.springframework.data.redis.connection.stream.StreamRecords;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class FormatEventProducer {
  private final String streamKey;
  private final RedisTemplate<String, String> redis;

  @Autowired
  public FormatEventProducer(
      @Value("${stream.format.key}") String streamKey,
      @NotNull RedisTemplate<String, String> redis) {
    this.streamKey = streamKey;
    this.redis = redis;
  }

  public void emit(EventMessage product) {
    ObjectRecord<String, EventMessage> result =
        StreamRecords.newRecord().ofObject(product).withStreamKey(streamKey);

    redis.opsForStream().add(result);
  }

  public void publishEvent(Long snippetId, FormatConfig config) {
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
