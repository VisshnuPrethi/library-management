package com.library.service;
import com.library.dto.BookDto;
import com.library.exception.ResourceNotFoundException;
import com.library.model.Book;
import com.library.repository.BookRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class BookService {
    private static final Logger log = LoggerFactory.getLogger(BookService.class);
    private final BookRepository repo;
    public BookService(BookRepository repo) { this.repo = repo; }
    public List<Book> getAll() { return repo.findAll(); }
    public Book getById(Long id) { return repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Book not found: " + id)); }
    public List<Book> search(String k) { return (k == null || k.isBlank()) ? repo.findAll() : repo.search(k.trim()); }
    @Transactional
    public Book add(BookDto d, String by) {
        Book b = new Book(d.getTitle(), d.getAuthor(), d.getCategory(), d.getIsbn(), d.getAvailableCopies());
        Book saved = repo.save(b);
        log.info("Book added [{}] by {}", saved.getId(), by);
        return saved;
    }
    @Transactional
    public Book update(Long id, BookDto d, String by) {
        Book b = getById(id);
        b.setTitle(d.getTitle()); b.setAuthor(d.getAuthor()); b.setCategory(d.getCategory());
        b.setIsbn(d.getIsbn()); b.setAvailableCopies(d.getAvailableCopies());
        log.info("Book updated [{}] by {}", id, by);
        return repo.save(b);
    }
    @Transactional
    public void delete(Long id, String by) {
        repo.delete(getById(id));
        log.info("Book deleted [{}] by {}", id, by);
    }
}
