package com.smartledger.dto;

import com.smartledger.entity.Category;
import com.smartledger.entity.TransactionType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

@Schema(description = "Request to create or update a transaction")
public record TransactionRequest(
        @Schema(description = "Transaction type (INCOME or EXPENSE)", example = "EXPENSE")
        @NotNull(message = "Type is required")
        TransactionType type,

        @Schema(description = "Transaction category", example = "GROCERIES")
        @NotNull(message = "Category is required")
        Category category,

        @Schema(description = "Transaction amount", example = "150.50")
        @NotNull(message = "Amount is required")
        @DecimalMin(value = "0.01", message = "Amount must be greater than 0")
        BigDecimal amount,

        @Schema(description = "Transaction date", example = "2025-10-30")
        @NotNull(message = "Date is required")
        LocalDate date,

        @Schema(description = "Transaction description", example = "Weekly grocery shopping")
        String description
) {}
