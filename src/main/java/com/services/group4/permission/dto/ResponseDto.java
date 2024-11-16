package com.services.group4.permission.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.services.group4.permission.common.DataTuple;
import com.services.group4.permission.common.ResponseDtoSerializer;

@JsonSerialize(using = ResponseDtoSerializer.class)
public record ResponseDto<T>(String message, DataTuple<T> data) {}
