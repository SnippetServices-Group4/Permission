package com.services.group4.permission.service.async;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.services.group4.permission.dto.LintRulesDto;
import java.util.Map;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.stream.ObjectRecord;
import org.springframework.data.redis.connection.stream.StreamRecords;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class LintEventProducer {
  private final String streamKey;
  private final ReactiveRedisTemplate<String, String> redis;

  @Autowired
  public LintEventProducer(
      @Value("${stream.initial.lint.key}") String streamKey,
      @NotNull ReactiveRedisTemplate<String, String> redis) {
    this.streamKey = streamKey;
    this.redis = redis;
  }

  public Mono<ObjectRecord<String, String>> emit(String jsonMessage) {
    ObjectRecord<String, String> result =
        StreamRecords.newRecord().ofObject(jsonMessage).withStreamKey(streamKey);

    return redis.opsForStream().add(result).thenReturn(result);
  }

  public void publishEvent(Long snippetId, LintRulesDto config) {
    System.out.println("\nLINT EVENT PRODUCER\n\n");

    ObjectMapper mapper = new ObjectMapper();
    try {
      // Create the JSON for the `config` field
      String jsonPayloadString = mapper.writeValueAsString(config);

      // Construct the entire message structure
      Map<String, Object> message =
          Map.of(
              "snippetId", snippetId,
              "lintRules", jsonPayloadString);

      // Serialize the complete message to a JSON string
      String finalMessageJson = mapper.writeValueAsString(message);

      // Send the message using emit
      emit(finalMessageJson).block();
    } catch (Exception e) {
      System.err.println("Error serializing message: " + e.getMessage());
    }
  }
}
