package com.library.service;
import com.library.dto.AuthDtos.RegisterRequest;
import com.library.exception.ResourceNotFoundException;
import com.library.model.Role;
import com.library.model.User;
import com.library.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class UserService {
    private static final Logger log = LoggerFactory.getLogger(UserService.class);
    private final UserRepository repo;
    private final PasswordEncoder encoder;
    public UserService(UserRepository repo, PasswordEncoder encoder) {
        this.repo = repo; this.encoder = encoder;
    }
    @Transactional
    public User register(RegisterRequest r) {
        if (repo.existsByUsername(r.getUsername())) throw new IllegalArgumentException("Username already taken");
        if (repo.existsByEmail(r.getEmail())) throw new IllegalArgumentException("Email already in use");
        User u = new User(r.getUsername(), r.getEmail(), encoder.encode(r.getPassword()), Role.USER);
        User saved = repo.save(u);
        log.info("Registered user: {}", saved.getUsername());
        return saved;
    }
    public List<User> getAll() { return repo.findAll(); }
    public User getById(Long id) { return repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found")); }
    public User getByUsername(String username) { return repo.findByUsername(username).orElseThrow(() -> new ResourceNotFoundException("User not found")); }
}
