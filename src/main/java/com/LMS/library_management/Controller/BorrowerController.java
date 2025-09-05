package com.LMS.library_management.Controller;

import com.LMS.library_management.Dto.BorrowerDTO;
import com.LMS.library_management.Models.Borrower;
import com.LMS.library_management.Service.BorrowerService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/borrowers")
public class BorrowerController {

    private final BorrowerService borrowerService;
    private static final Logger logger = LoggerFactory.getLogger(BorrowerController.class);

    public BorrowerController(BorrowerService borrowerService) {
        this.borrowerService = borrowerService;
    }

    @PostMapping
    public ResponseEntity<Borrower> createBorrower(@Valid @RequestBody BorrowerDTO dto) {
        Borrower created = borrowerService.createBorrower(dto);
        logger.info("Borrower created with id {}", created.getId());
        return ResponseEntity.ok(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Borrower> updateBorrower(@PathVariable Long id, @Valid @RequestBody BorrowerDTO dto) {
        Borrower updated = borrowerService.updateBorrower(id, dto);
        logger.info("Borrower updated with id {}", updated.getId());
        return ResponseEntity.ok(updated);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Borrower> getBorrowerById(@PathVariable Long id) {
        Borrower borrower = borrowerService.getBorrowerById(id);
        return ResponseEntity.ok(borrower);
    }

    @GetMapping
    public ResponseEntity<List<Borrower>> getAllBorrowers() {
        List<Borrower> borrowers = borrowerService.getAllBorrowers();
        return ResponseEntity.ok(borrowers);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBorrower(@PathVariable Long id) {
        borrowerService.deleteBorrower(id);
        logger.info("Borrower deleted with id {}", id);
        return ResponseEntity.noContent().build();
    }
}
