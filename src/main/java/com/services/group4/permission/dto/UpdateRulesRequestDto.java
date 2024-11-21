package com.services.group4.permission.dto;

import jakarta.validation.constraints.NotNull;

public record UpdateRulesRequestDto<T>(
    //    @NotNull(message = "User ID is required") String userId,
    @NotNull(message = "Rules are required") T rules) {}
