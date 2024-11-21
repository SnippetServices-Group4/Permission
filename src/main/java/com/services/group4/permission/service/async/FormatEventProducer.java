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
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class FormatEventProducer {
  private final String streamKey;
  private final ReactiveRedisTemplate<String, String> redis;

  @Autowired
  public FormatEventProducer(
      @Value("${stream.initial.format.key}") String streamKey,
      @NotNull ReactiveRedisTemplate<String, String> redis) {
    this.streamKey = streamKey;
    this.redis = redis;
  }

  public Mono<ObjectRecord<String, String>> emit(String jsonMessage) {
    ObjectRecord<String, String> result =
        StreamRecords.newRecord().ofObject(jsonMessage).withStreamKey(streamKey);
      log.info("Emitting message to stream: {}", jsonMessage);
    return redis.opsForStream().add(result).thenReturn(result);
  }

  public void publishEvent(Long snippetId, FormatRulesDto config) {
    log.info("Trying to publish format event for snippet with id: {}", snippetId);
    System.out.println("\nFORMAT EVENT PRODUCER\n\n");

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
      emit(finalMessageJson).block();
      log.info("Emitting message to stream: {}", finalMessageJson);
    } catch (Exception e) {
      log.error("Error serializing message: {}", e.getMessage());
      System.err.println("Error serializing message: " + e.getMessage());
    }
  }
}
