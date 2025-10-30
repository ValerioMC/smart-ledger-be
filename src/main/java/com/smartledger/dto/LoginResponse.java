package com.smartledger.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Risposta del login contenente il token JWT e lo username")
public record LoginResponse(
    @Schema(description = "Token JWT per l'autenticazione delle richieste successive",
            example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    String token,

    @Schema(description = "Nome utente autenticato", example = "admin")
    String username
) {}
