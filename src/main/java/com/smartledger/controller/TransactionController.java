package com.smartledger.controller;

import com.smartledger.dto.TransactionRequest;
import com.smartledger.dto.TransactionResponse;
import com.smartledger.entity.TransactionType;
import com.smartledger.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/transactions")
@Tag(name = "Transactions", description = "Transaction management endpoints")
@SecurityRequirement(name = "Bearer Authentication")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping
    @Operation(summary = "Create a new transaction", description = "Creates a new income or expense transaction for the authenticated user")
    public ResponseEntity<TransactionResponse> createTransaction(
            @Valid @RequestBody TransactionRequest request,
            Authentication authentication) {
        String username = authentication.getName();
        TransactionResponse response = transactionService.createTransaction(username, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    @Operation(summary = "Get all transactions", description = "Retrieves all transactions for the authenticated user")
    public ResponseEntity<List<TransactionResponse>> getAllTransactions(Authentication authentication) {
        String username = authentication.getName();
        List<TransactionResponse> transactions = transactionService.getAllTransactions(username);
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/type/{type}")
    @Operation(summary = "Get transactions by type", description = "Retrieves transactions filtered by type (INCOME or EXPENSE)")
    public ResponseEntity<List<TransactionResponse>> getTransactionsByType(
            @PathVariable TransactionType type,
            Authentication authentication) {
        String username = authentication.getName();
        List<TransactionResponse> transactions = transactionService.getTransactionsByType(username, type);
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/date-range")
    @Operation(summary = "Get transactions by date range", description = "Retrieves transactions within a specified date range")
    public ResponseEntity<List<TransactionResponse>> getTransactionsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            Authentication authentication) {
        String username = authentication.getName();
        List<TransactionResponse> transactions = transactionService.getTransactionsByDateRange(username, startDate, endDate);
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get transaction by ID", description = "Retrieves a specific transaction by its ID")
    public ResponseEntity<TransactionResponse> getTransactionById(
            @PathVariable Long id,
            Authentication authentication) {
        String username = authentication.getName();
        TransactionResponse transaction = transactionService.getTransactionById(username, id);
        return ResponseEntity.ok(transaction);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update transaction", description = "Updates an existing transaction")
    public ResponseEntity<TransactionResponse> updateTransaction(
            @PathVariable Long id,
            @Valid @RequestBody TransactionRequest request,
            Authentication authentication) {
        String username = authentication.getName();
        TransactionResponse response = transactionService.updateTransaction(username, id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete transaction", description = "Deletes a transaction")
    public ResponseEntity<Void> deleteTransaction(
            @PathVariable Long id,
            Authentication authentication) {
        String username = authentication.getName();
        transactionService.deleteTransaction(username, id);
        return ResponseEntity.noContent().build();
    }
}
