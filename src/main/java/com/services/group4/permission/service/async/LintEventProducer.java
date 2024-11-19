package com.services.group4.permission.service.async;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.services.group4.permission.dto.LintRulesDto;
import java.util.Map;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.stream.ObjectRecord;
import org.springframework.data.redis.connection.stream.StreamRecords;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

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

  public void emit(String jsonMessage) {
    ObjectRecord<String, String> result =
        StreamRecords.newRecord().ofObject(jsonMessage).withStreamKey(streamKey);

    redis.opsForStream().add(result);
  }

  public void publishEvent(Long snippetId, LintRulesDto config) {
    ObjectMapper mapper = new ObjectMapper();
    try {
      // Create the JSON for the `config` field
      String jsonPayloadString = mapper.writeValueAsString(config);

      // Construct the entire message structure
      Map<String, Object> message =
          Map.of(
              "snippetId", snippetId,
              "config", jsonPayloadString);

      // Serialize the complete message to a JSON string
      String finalMessageJson = mapper.writeValueAsString(message);

      // Send the message using emit
      emit(finalMessageJson);
    } catch (Exception e) {
      System.err.println("Error serializing message: " + e.getMessage());
    }
  }
}
