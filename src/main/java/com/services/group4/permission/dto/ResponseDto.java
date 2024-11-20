package com.services.group4.permission.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.services.group4.permission.common.DataTuple;
import com.services.group4.permission.common.json.ResponseDtoDeserializer;
import com.services.group4.permission.common.json.ResponseDtoSerializer;

@JsonSerialize(using = ResponseDtoSerializer.class)
@JsonDeserialize(using = ResponseDtoDeserializer.class)
public record ResponseDto<T>(String message, DataTuple<T> data) {}
