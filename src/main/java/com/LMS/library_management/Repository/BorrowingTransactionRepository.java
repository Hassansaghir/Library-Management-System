package com.LMS.library_management.Repository;

import com.LMS.library_management.Models.BorrowingTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BorrowingTransactionRepository extends JpaRepository<BorrowingTransaction, Long> {

    List<BorrowingTransaction> findByBorrowerId(Long borrowerId);

    List<BorrowingTransaction> findByBookIsbn(String isbn); // use Book's PK type
}
