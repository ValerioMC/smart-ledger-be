package com.smartledger.dto;

import com.smartledger.entity.Role;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Set;

@Schema(description = "Login response containing JWT token, username and user roles")
public record LoginResponse(
    @Schema(description = "JWT token for authenticating subsequent requests",
            example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    String token,

    @Schema(description = "Authenticated username", example = "admin")
    String username,

    @Schema(description = "Roles assigned to the user", example = "[\"ADMIN\"]")
    Set<Role> roles
) {}
