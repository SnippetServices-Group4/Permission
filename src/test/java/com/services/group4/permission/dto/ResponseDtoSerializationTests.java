package com.services.group4.permission.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.services.group4.permission.common.DataTuple;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ResponseDtoSerializationTests {
  @Test
  void testResponseDtoSerialization() throws JsonProcessingException {
    DataTuple<String> data = new DataTuple<>("key", "value");
    ResponseDto<String> response = new ResponseDto<>("Success", data);

    String json = new ObjectMapper().writeValueAsString(response);

    assertNotNull(json);
    assertTrue(json.contains("\"message\":\"Success\""));
    assertTrue(json.contains("\"data\":{\"key\":\"value\"}"));
  }

}
