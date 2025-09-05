package com.LMS.library_management.Repository;
import com.LMS.library_management.Models.Book;
import com.LMS.library_management.Models.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findByTitleContainingIgnoreCase(String title);
    List<Book> findByCategory(Category category);
    List<Book> findByAuthorId(Long authorId);
    boolean existsByIsbn(String isbn);
}