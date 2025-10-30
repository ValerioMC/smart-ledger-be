package com.smartledger.dto;

import com.smartledger.entity.Category;
import com.smartledger.entity.TransactionType;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Schema(description = "Transaction response")
public record TransactionResponse(
        @Schema(description = "Transaction ID", example = "1")
        Long id,

        @Schema(description = "User ID", example = "1")
        Long userId,

        @Schema(description = "Transaction type", example = "EXPENSE")
        TransactionType type,

        @Schema(description = "Transaction category", example = "GROCERIES")
        Category category,

        @Schema(description = "Transaction amount", example = "150.50")
        BigDecimal amount,

        @Schema(description = "Transaction date", example = "2025-10-30")
        LocalDate date,

        @Schema(description = "Transaction description", example = "Weekly grocery shopping")
        String description,

        @Schema(description = "Creation timestamp")
        LocalDateTime createdAt,

        @Schema(description = "Last update timestamp")
        LocalDateTime updatedAt
) {}
