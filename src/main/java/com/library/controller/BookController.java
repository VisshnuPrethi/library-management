package com.library.controller;
import com.library.dto.BookDto;
import com.library.model.Book;
import com.library.service.BookService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/books")
public class BookController {
    private final BookService svc;
    public BookController(BookService svc) { this.svc = svc; }

    @GetMapping
    public ResponseEntity<List<Book>> all() { return ResponseEntity.ok(svc.getAll()); }

    @GetMapping("/{id}")
    public ResponseEntity<Book> one(@PathVariable Long id) { return ResponseEntity.ok(svc.getById(id)); }

    @GetMapping("/search")
    public ResponseEntity<List<Book>> search(@RequestParam(required=false) String keyword) {
        return ResponseEntity.ok(svc.search(keyword));
    }

    @PostMapping
    @PreAuthorize("hasRole('LIBRARIAN')")
    public ResponseEntity<Book> add(@Valid @RequestBody BookDto dto, @AuthenticationPrincipal UserDetails u) {
        return ResponseEntity.ok(svc.add(dto, u.getUsername()));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('LIBRARIAN')")
    public ResponseEntity<Book> update(@PathVariable Long id, @Valid @RequestBody BookDto dto, @AuthenticationPrincipal UserDetails u) {
        return ResponseEntity.ok(svc.update(id, dto, u.getUsername()));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('LIBRARIAN')")
    public ResponseEntity<Map<String,String>> delete(@PathVariable Long id, @AuthenticationPrincipal UserDetails u) {
        svc.delete(id, u.getUsername());
        return ResponseEntity.ok(Map.of("message","Book deleted"));
    }
}
