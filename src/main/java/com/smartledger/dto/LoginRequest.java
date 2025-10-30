package com.smartledger.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Login request with user credentials")
public record LoginRequest(
    @Schema(description = "Username", example = "admin", required = true)
    @NotBlank(message = "Username is required")
    String username,

    @Schema(description = "Password", example = "admin123", required = true)
    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    String password
) {}
