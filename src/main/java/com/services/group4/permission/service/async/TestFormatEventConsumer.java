package com.services.group4.permission.service.async;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Duration;
import java.util.Map;
import org.austral.ingsis.redis.RedisStreamConsumer;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.stream.ObjectRecord;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.stream.StreamReceiver;
import org.springframework.stereotype.Component;

@Component
public class TestFormatEventConsumer extends RedisStreamConsumer<EventMessage> {
  @Autowired
  public TestFormatEventConsumer(
      @Value("${stream.format.key}") String streamKey,
      @Value("test-format-group") String groupId,
      @NotNull RedisTemplate<String, String> redis) {
    super(streamKey, groupId, redis);
  }

  @Override
  protected void onMessage(@NotNull ObjectRecord<String, EventMessage> objectRecord) {
    EventMessage product = objectRecord.getValue();
    System.out.println("SnippetId: " + product.snippetId());

    ObjectMapper mapper = new ObjectMapper();
    try {
      Map<String, Object> payloadMap = mapper.readValue(product.config(), new TypeReference<>() {});
      System.out.println("JSON Payload: " + payloadMap);
    } catch (Exception e) {
      System.err.println("Error deserializing jsonPayload: " + e.getMessage());
    }
  }

  @Override
  protected @NotNull StreamReceiver.StreamReceiverOptions<
          String, ObjectRecord<String, EventMessage>>
      options() {
    return StreamReceiver.StreamReceiverOptions.builder()
        .pollTimeout(Duration.ofSeconds(1))
        .targetType(EventMessage.class)
        .build();
  }
}
