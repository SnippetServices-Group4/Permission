package com.services.group4.permission.service.async;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.services.group4.permission.dto.FormatRulesDto;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.stream.ObjectRecord;
import org.springframework.data.redis.connection.stream.StreamRecords;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class FormatEventProducer {
  private final String streamKey;
  private final RedisTemplate<String, String> redis;

  @Autowired
  public FormatEventProducer(
      @Value("${stream.initial.format.key}") String streamKey,
      @NotNull RedisTemplate<String, String> redis) {
    this.streamKey = streamKey;
    this.redis = redis;
  }

  public void emit(String jsonMessage) {
    log.info("Emitting message to stream: {}", jsonMessage);
    ObjectRecord<String, String> result =
        StreamRecords.newRecord().ofObject(jsonMessage).withStreamKey(streamKey);

    redis.opsForStream().add(result);
  }

  public void publishEvent(Long snippetId, FormatRulesDto config) {
    log.info("Trying to publish format event for snippet with id: {}", snippetId);
    ObjectMapper mapper = new ObjectMapper();
    try {
      // Create the JSON for the `config` field
      String jsonPayloadString = mapper.writeValueAsString(config);

      // Construct the entire message structure
      Map<String, Object> message =
          Map.of(
              "snippetId", snippetId,
              "formatRules", jsonPayloadString);

      // Serialize the complete message to a JSON string
      String finalMessageJson = mapper.writeValueAsString(message);

      // Send the message using emit
      log.info("Emitting message to stream: {}", finalMessageJson);
      emit(finalMessageJson);
    } catch (Exception e) {
      log.error("Error serializing message: {}", e.getMessage());
      System.err.println("Error serializing message: " + e.getMessage());
    }
  }
}
