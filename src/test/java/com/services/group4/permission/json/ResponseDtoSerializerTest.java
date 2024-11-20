package com.services.group4.permission.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.services.group4.permission.common.DataTuple;
import com.services.group4.permission.dto.ResponseDto;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ResponseDtoSerializerTest {

  private final ObjectMapper objectMapper = new ObjectMapper();

  @Test
  void testSerialize_ValidResponseDto() throws Exception {
    // Crear el DataTuple que se usará en el ResponseDto
    DataTuple<List<Long>> dataTuple = new DataTuple<>("snippetList", List.of(1L, 2L, 3L));
    ResponseDto<List<Long>> responseDto = new ResponseDto<>("Test message", dataTuple);

    String json = objectMapper.writeValueAsString(responseDto);

    assertNotNull(json);
    assertTrue(json.contains("\"message\":\"Test message\""));
    assertTrue(json.contains("\"snippetList\":[1,2,3]"));
  }

  @Test
  void testSerialize_InvalidResponseDto() throws Exception {
    DataTuple<List<Long>> dataTuple = new DataTuple<>("snippetList", null);
    ResponseDto<List<Long>> responseDto = new ResponseDto<>("Test message", dataTuple);

    String json = objectMapper.writeValueAsString(responseDto);

    assertNotNull(json);
    assertTrue(json.contains("\"snippetList\":null"));
  }
}
