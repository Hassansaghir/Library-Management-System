// dto/BorrowingTransactionDTO.java
package com.LMS.library_management.Dto;

import com.LMS.library_management.Models.BorrowingStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class BorrowingTransactionDTO {
    private Long id;

    @NotNull(message = "Book ID is required")
    private Long bookId;

    @NotNull(message = "Borrower ID is required")
    private Long borrowerId;

    private LocalDate borrowDate;

    private LocalDate returnDate;

    private BorrowingStatus status;


}
