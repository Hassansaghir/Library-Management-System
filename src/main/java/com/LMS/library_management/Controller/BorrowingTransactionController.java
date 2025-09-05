package com.LMS.library_management.Controller;

import com.LMS.library_management.Dto.BorrowingTransactionDTO;
import com.LMS.library_management.Models.BorrowingTransaction;
import com.LMS.library_management.Service.BorrowingTransactionService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class BorrowingTransactionController {

    private final BorrowingTransactionService transactionService;
    private static final Logger logger = LoggerFactory.getLogger(BorrowingTransactionController.class);

    public BorrowingTransactionController(BorrowingTransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/borrow")
    public ResponseEntity<BorrowingTransaction> borrowBook(@Valid @RequestBody BorrowingTransactionDTO dto) {
        BorrowingTransaction transaction = transactionService.borrowBook(dto);
        logger.info("Book borrowed with transaction id {}", transaction.getId());
        return ResponseEntity.ok(transaction);
    }

    @PostMapping("/return/{id}")
    public ResponseEntity<BorrowingTransaction> returnBook(@PathVariable Long id) {
        BorrowingTransaction transaction = transactionService.returnBook(id);
        logger.info("Book returned with transaction id {}", transaction.getId());
        return ResponseEntity.ok(transaction);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BorrowingTransaction> getTransactionById(@PathVariable Long id) {
        BorrowingTransaction transaction = transactionService.getTransactionById(id);
        return ResponseEntity.ok(transaction);
    }

    @GetMapping
    public ResponseEntity<List<BorrowingTransaction>> getAllTransactions() {
        List<BorrowingTransaction> transactions = transactionService.getAllTransactions();
        return ResponseEntity.ok(transactions);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable Long id) {
        transactionService.deleteTransaction(id);
        logger.info("Transaction deleted with id {}", id);
        return ResponseEntity.noContent().build();
    }
}
