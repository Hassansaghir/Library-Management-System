package com.LMS.library_management.Controller;

import com.LMS.library_management.Dto.BookDTO;
import com.LMS.library_management.Models.Book;
import com.LMS.library_management.Models.Category;
import com.LMS.library_management.Service.BookService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookController {

    private final BookService bookService;
    private static final Logger logger = LoggerFactory.getLogger(BookController.class);

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @PostMapping
    public ResponseEntity<Book> createBook(@Valid @RequestBody BookDTO dto) {
        Book created = bookService.createBook(dto);
        logger.info("Book created with id {}", created.getId());
        return ResponseEntity.ok(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Book> updateBook(@PathVariable Long id, @Valid @RequestBody BookDTO dto) {
        Book updated = bookService.updateBook(id, dto);
        logger.info("Book updated with id {}", updated.getId());
        return ResponseEntity.ok(updated);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Long id) {
        Book book = bookService.getBookById(id);
        return ResponseEntity.ok(book);
    }

    @GetMapping
    public ResponseEntity<List<Book>> getAllBooks(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) Category category,
            @RequestParam(required = false) Long authorId) {
        List<Book> books = bookService.searchBooks(title, category, authorId);
        return ResponseEntity.ok(books);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        logger.info("Book deleted with id {}", id);
        return ResponseEntity.noContent().build();
    }
}
