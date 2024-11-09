package com.services.group4.permission.dto;

import jakarta.validation.constraints.NotNull;

public record UpdateRulesRequestDto(
    @NotNull(message = "User ID is required")
    Long userId,
    @NotNull(message = "Rules are required")
    String rules) {
}
