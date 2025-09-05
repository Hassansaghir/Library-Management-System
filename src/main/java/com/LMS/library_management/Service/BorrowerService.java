// service/BorrowerService.java
package com.LMS.library_management.Service;

import com.LMS.library_management.Dto.BorrowerDTO;
import com.LMS.library_management.Models.Borrower;
import com.LMS.library_management.Exception.ResourceNotFoundException;
import com.LMS.library_management.Repository.BorrowerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BorrowerService {
    private final BorrowerRepository borrowerRepository;
    private static final Logger logger = LoggerFactory.getLogger(BorrowerService.class);

    public BorrowerService(BorrowerRepository borrowerRepository) {
        this.borrowerRepository = borrowerRepository;
    }

    public Borrower createBorrower(BorrowerDTO dto) {
        Borrower borrower = new Borrower();
        borrower.setEmail(dto.getEmail());
        borrower.setName(dto.getName());
        borrower.setPhoneNumber(dto.getPhoneNumber());
        Borrower saved = borrowerRepository.save(borrower);
        logger.info("Created borrower with id {}", saved.getId());
        return saved;
    }

    public Borrower updateBorrower(Long id, BorrowerDTO dto){
        Borrower borrower = borrowerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id " + id));
        borrower.setEmail(dto.getEmail());
        borrower.setName(dto.getName());
        borrower.setPhoneNumber(dto.getPhoneNumber());
        Borrower saved = borrowerRepository.save(borrower);
        logger.info("Updated borrower of name:", dto.getName());
        return borrower;
    }

    public Borrower getBorrowerById(Long id) {
        return borrowerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Borrower not found with id " + id));
    }
    public List<Borrower> getAllBorrowers(){
        return  borrowerRepository.findAll();
    }
    public void deleteBorrower(long id){
        if (!borrowerRepository.existsById(id)) {
            throw new ResourceNotFoundException("borrower not found with id " + id);
        }
        borrowerRepository.deleteById(id);
        logger.info("Deleted book with id {}", id);
    }

}