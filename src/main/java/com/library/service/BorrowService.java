package com.library.service;
import com.library.dto.BorrowRequestDto;
import com.library.exception.ResourceNotFoundException;
import com.library.model.*;
import com.library.model.BorrowRequest.Status;
import com.library.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class BorrowService {
    private static final Logger log = LoggerFactory.getLogger(BorrowService.class);
    private final BorrowRequestRepository borrowRepo;
    private final BookRepository bookRepo;
    private final UserRepository userRepo;
    public BorrowService(BorrowRequestRepository borrowRepo, BookRepository bookRepo, UserRepository userRepo) {
        this.borrowRepo = borrowRepo; this.bookRepo = bookRepo; this.userRepo = userRepo;
    }
    @Transactional
    public BorrowRequestDto request(Long bookId, String username) {
        User user = userRepo.findByUsername(username).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Book book = bookRepo.findById(bookId).orElseThrow(() -> new ResourceNotFoundException("Book not found"));
        if (!borrowRepo.findPending(user.getId(), bookId).isEmpty()) throw new IllegalStateException("You already have a pending request for this book");
        if (book.getAvailableCopies() < 1) throw new IllegalStateException("No copies available");
        BorrowRequest saved = borrowRepo.save(new BorrowRequest(user, book));
        log.info("Borrow request [{}] by {} for book [{}]", saved.getId(), username, bookId);
        return new BorrowRequestDto(saved);
    }
    @Transactional(readOnly = true)
    public List<BorrowRequestDto> myRequests(String username) {
        User user = userRepo.findByUsername(username).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return borrowRepo.findByUser(user).stream().map(BorrowRequestDto::new).toList();
    }
    @Transactional(readOnly = true)
    public List<BorrowRequestDto> all() {
        return borrowRepo.findAllWithDetails().stream().map(BorrowRequestDto::new).toList();
    }
    @Transactional
    public BorrowRequestDto process(Long id, boolean approve, String by) {
        BorrowRequest r = borrowRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Request not found"));
        if (r.getStatus() != Status.PENDING) throw new IllegalStateException("Already processed");
        if (approve) {
            Book b = r.getBook();
            if (b.getAvailableCopies() < 1) throw new IllegalStateException("No copies available");
            b.setAvailableCopies(b.getAvailableCopies() - 1);
            bookRepo.save(b);
            r.setStatus(Status.APPROVED);
            log.info("Request [{}] APPROVED by {}", id, by);
        } else {
            r.setStatus(Status.REJECTED);
            log.info("Request [{}] REJECTED by {}", id, by);
        }
        r.setProcessedAt(LocalDateTime.now());
        return new BorrowRequestDto(borrowRepo.save(r));
    }
}
