package com.library.controller;
import com.library.dto.AuthDtos.*;
import com.library.model.User;
import com.library.security.JwtUtils;
import com.library.service.UserService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private static final Logger log = LoggerFactory.getLogger(AuthController.class);
    private final AuthenticationManager authManager;
    private final JwtUtils jwtUtils;
    private final UserService userService;
    public AuthController(AuthenticationManager authManager, JwtUtils jwtUtils, UserService userService) {
        this.authManager = authManager; this.jwtUtils = jwtUtils; this.userService = userService;
    }
    @PostMapping("/register")
    public ResponseEntity<MessageResponse> register(@Valid @RequestBody RegisterRequest req) {
        userService.register(req);
        return ResponseEntity.ok(new MessageResponse("Registration successful"));
    }
    @PostMapping("/login")
    public ResponseEntity<Object> login(@Valid @RequestBody LoginRequest req) {
        try {
            Authentication auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.getUsername(), req.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(auth);
            String jwt = jwtUtils.generate(auth);
            User user = userService.getByUsername(req.getUsername());
            log.info("Login: {}", req.getUsername().replaceAll("[\r\n]","_"));
            return ResponseEntity.ok(new JwtResponse(jwt, user.getUsername(), user.getRole().name()));
        } catch (BadCredentialsException e) {
            log.warn("Failed login: {}", req.getUsername().replaceAll("[\r\n]","_"));
            return ResponseEntity.status(401).body(new MessageResponse("Invalid username or password"));
        } catch (DisabledException e) {
            return ResponseEntity.status(401).body(new MessageResponse("Account disabled"));
        }
    }
}
