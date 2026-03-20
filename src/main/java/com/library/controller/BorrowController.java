package com.library.controller;
import com.library.dto.BorrowRequestDto;
import com.library.service.BorrowService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/borrow")
public class BorrowController {
    private final BorrowService svc;
    public BorrowController(BorrowService svc) { this.svc = svc; }

    @PostMapping("/request/{bookId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<BorrowRequestDto> request(@PathVariable Long bookId, @AuthenticationPrincipal UserDetails u) {
        return ResponseEntity.ok(svc.request(bookId, u.getUsername()));
    }

    @GetMapping("/my-requests")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<BorrowRequestDto>> mine(@AuthenticationPrincipal UserDetails u) {
        return ResponseEntity.ok(svc.myRequests(u.getUsername()));
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('LIBRARIAN')")
    public ResponseEntity<List<BorrowRequestDto>> all() { return ResponseEntity.ok(svc.all()); }

    @PutMapping("/requests/{id}/approve")
    @PreAuthorize("hasRole('LIBRARIAN')")
    public ResponseEntity<BorrowRequestDto> approve(@PathVariable Long id, @AuthenticationPrincipal UserDetails u) {
        return ResponseEntity.ok(svc.process(id, true, u.getUsername()));
    }

    @PutMapping("/requests/{id}/reject")
    @PreAuthorize("hasRole('LIBRARIAN')")
    public ResponseEntity<BorrowRequestDto> reject(@PathVariable Long id, @AuthenticationPrincipal UserDetails u) {
        return ResponseEntity.ok(svc.process(id, false, u.getUsername()));
    }
}
