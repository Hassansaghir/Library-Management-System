// service/BookService.java
package com.LMS.library_management.Service;
import org.modelmapper.ModelMapper;
import com.LMS.library_management.Dto.BookDTO;
import com.LMS.library_management.Models.Author;
import com.LMS.library_management.Models.Book;
import com.LMS.library_management.Models.Category;
import com.LMS.library_management.Exception.BadRequestException;
import com.LMS.library_management.Exception.ResourceNotFoundException;
import com.LMS.library_management.Repository.AuthorRepository;
import com.LMS.library_management.Repository.BookRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class BookService {
    private final ModelMapper modelMapper;
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private static final Logger logger = LoggerFactory.getLogger(BookService.class);

    public BookService(ModelMapper modelMapper, BookRepository bookRepository, AuthorRepository authorRepository) {
        this.modelMapper = modelMapper;
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
    }

    public Book createBook(BookDTO dto) {
        Author author = authorRepository.findById(dto.getAuthorId())
                .orElseThrow(() -> new ResourceNotFoundException("Author not found with id " + dto.getAuthorId()));
        if (bookRepository.findByTitleContainingIgnoreCase(dto.getTitle()).stream()
                .anyMatch(b -> b.getIsbn().equals(dto.getIsbn()))) {
            throw new BadRequestException("Book with the same ISBN already exists");
        }
        Book book = modelMapper.map(dto,Book.class);
        Book saved = bookRepository.save(book);
        logger.info("Created book with id {}", saved.getId());
        return saved;
    }

    public Book updateBook(Long id, BookDTO dto) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id " + id));

        Author author = authorRepository.findById(dto.getAuthorId())
                .orElseThrow(() -> new ResourceNotFoundException("Author not found with id " + dto.getAuthorId()));

        book.setTitle(dto.getTitle());
        book.setIsbn(dto.getIsbn());
        book.setCategory(dto.getCategory());
        book.setAuthor(author);
        book.setAvailable(dto.isAvailable());

        Book updated = bookRepository.save(book);
        logger.info("Updated book with id {}", updated.getId());
        return updated;
    }

    public Book getBookById(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id " + id));
        return book;
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public void deleteBook(Long id) {
        if (!bookRepository.existsById(id)) {
            throw new ResourceNotFoundException("Book not found with id " + id);
        }
        bookRepository.deleteById(id);
        logger.info("Deleted book with id {}", id);
    }

    public List<Book> searchBooks(String title, Category category, Long authorId) {
        if (title != null && !title.isBlank()) {
            return bookRepository.findByTitleContainingIgnoreCase(title);
        } else if (category != null) {
            return bookRepository.findByCategory(category);
        } else if (authorId != null) {
            return bookRepository.findByAuthorId(authorId);
        } else {
            return getAllBooks();
        }
    }

    public List<Book> getBooksByAuthor(Long authorId) {
        return bookRepository.findByAuthorId(authorId);
    }

    public void updateBookAvailability(Long bookId, boolean available) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id " + bookId));
        book.setAvailable(available);
        bookRepository.save(book);
        logger.info("Updated availability of book id {} to {}", bookId, available);
    }
}
