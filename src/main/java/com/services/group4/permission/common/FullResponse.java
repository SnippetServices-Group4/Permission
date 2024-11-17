package com.services.group4.permission.common;

import com.services.group4.permission.dto.ResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class FullResponse {

  public static <T> ResponseEntity<ResponseDto<T>> create(
      String message, String name, T data, HttpStatus status) {
    DataTuple<T> tuple = new DataTuple<>(name, data);
    ResponseDto<T> responseDto = new ResponseDto<>(message, tuple);
    return new ResponseEntity<>(responseDto, status);
  }
}
