package com.LMS.library_management.Service;
import org.modelmapper.ModelMapper;
import com.LMS.library_management.Dto.AuthorDTO;
import com.LMS.library_management.Models.Author;
import com.LMS.library_management.Exception.ResourceNotFoundException;
import com.LMS.library_management.Repository.AuthorRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuthorService {
    private final AuthorRepository authorRepository;
    private final ModelMapper modelMapper;
    private static final Logger logger = LoggerFactory.getLogger(AuthorService.class);

    public AuthorService(AuthorRepository authorRepository, ModelMapper modelMapper) {
        this.authorRepository = authorRepository;
        this.modelMapper = modelMapper;
    }

    public Author createAuthor(AuthorDTO dto) {
        Author author = modelMapper.map(dto, Author.class);
        Author saved = authorRepository.save(author);
        logger.info("Created author with id {}", saved.getId());
        return saved;
    }


    public Author updateAuthor(Long id, AuthorDTO dto) {
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Author not found with id " + id));
        author.setName(dto.getName());
        author.setBiography(dto.getBiography());
        Author updated = authorRepository.save(author);
        logger.info("Updated author with id {}", updated.getId());
        return updated;
    }

    public Author getAuthorById(Long id) {
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Author not found with id " + id));
        return author;
    }

    public List<Author> getAllAuthors() {
        return authorRepository.findAll();
    }

    public void deleteAuthor(Long id) {
        if (!authorRepository.existsById(id)) {
            throw new ResourceNotFoundException("Author not found with id " + id);
        }
        authorRepository.deleteById(id);
        logger.info("Deleted author with id {}", id);
    }
}
