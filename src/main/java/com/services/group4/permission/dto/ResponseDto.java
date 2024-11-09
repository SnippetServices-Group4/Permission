package com.services.group4.permission.dto;

public record ResponseDto<T>(String message, T data) {
}
