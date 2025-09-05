package com.LMS.library_management.Service;
import org.modelmapper.ModelMapper;
import com.LMS.library_management.Dto.BorrowingTransactionDTO;
import com.LMS.library_management.Models.Book;
import com.LMS.library_management.Models.Borrower;
import com.LMS.library_management.Models.BorrowingTransaction;
import com.LMS.library_management.Models.BorrowingStatus;
import com.LMS.library_management.Exception.BadRequestException;
import com.LMS.library_management.Exception.ResourceNotFoundException;
import com.LMS.library_management.Repository.BookRepository;
import com.LMS.library_management.Repository.BorrowerRepository;
import com.LMS.library_management.Repository.BorrowingTransactionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class BorrowingTransactionService {

    private final ModelMapper modelMapper;
    private final BorrowingTransactionRepository transactionRepository;
    private final BookRepository bookRepository;
    private final BorrowerRepository borrowerRepository;
    private static final Logger logger = LoggerFactory.getLogger(BorrowingTransactionService.class);

    public BorrowingTransactionService(ModelMapper modelMapper, BorrowingTransactionRepository transactionRepository,
                                       BookRepository bookRepository,
                                       BorrowerRepository borrowerRepository) {
        this.modelMapper = modelMapper;
        this.transactionRepository = transactionRepository;
        this.bookRepository = bookRepository;
        this.borrowerRepository = borrowerRepository;
    }

    public BorrowingTransaction borrowBook(BorrowingTransactionDTO dto) {
        Book book = bookRepository.findById(dto.getBookId())
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id " + dto.getBookId()));

        if (!book.isAvailable()) {
            throw new BadRequestException("Book with id " + dto.getBookId() + " is currently unavailable");
        }

        Borrower borrower = borrowerRepository.findById(dto.getBorrowerId())
                .orElseThrow(() -> new ResourceNotFoundException("Borrower not found with id " + dto.getBorrowerId()));

        BorrowingTransaction transaction = modelMapper.map(dto,BorrowingTransaction.class);
        transaction.setStatus(BorrowingStatus.BORROWED);

        // Mark book as unavailable
        book.setAvailable(false);
        bookRepository.save(book);

        BorrowingTransaction saved = transactionRepository.save(transaction);
        logger.info("Book id {} borrowed by borrower id {} with transaction id {}", book.getId(), borrower.getId(), saved.getId());
        return saved;
    }

    public BorrowingTransaction returnBook(Long transactionId) {
        BorrowingTransaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found with id " + transactionId));

        if (transaction.getStatus() == BorrowingStatus.RETURNED) {
            throw new BadRequestException("Book has already been returned for transaction id " + transactionId);
        }

        transaction.setReturnDate(LocalDate.now());
        transaction.setStatus(BorrowingStatus.BORROWED);

        // Mark book as available
        Book book = transaction.getBook();
        book.setAvailable(true);
        bookRepository.save(book);

        BorrowingTransaction updated = transactionRepository.save(transaction);
        logger.info("Book id {} returned for transaction id {}", book.getId(), transactionId);
        return updated;
    }

    public BorrowingTransaction getTransactionById(Long id) {
        return transactionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found with id " + id));
    }

    public List<BorrowingTransaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    public void deleteTransaction(Long id) {
        if (!transactionRepository.existsById(id)) {
            throw new ResourceNotFoundException("Transaction not found with id " + id);
        }
        transactionRepository.deleteById(id);
        logger.info("Deleted transaction with id {}", id);
    }
}
