package com.library.controller;
import com.library.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService svc;
    public UserController(UserService svc) { this.svc = svc; }

    @GetMapping
    @PreAuthorize("hasRole('LIBRARIAN')")
    public ResponseEntity<List<Map<String,Object>>> all() {
        return ResponseEntity.ok(svc.getAll().stream()
            .map(u -> Map.<String,Object>of(
                "id", u.getId(), "username", u.getUsername(),
                "email", u.getEmail(), "role", u.getRole().name(), "enabled", u.isEnabled()))
            .collect(Collectors.toList()));
    }
}
