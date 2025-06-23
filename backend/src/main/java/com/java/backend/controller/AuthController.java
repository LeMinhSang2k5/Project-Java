package com.java.backend.controller;

import com.java.backend.entity.User;
import com.java.backend.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:3000")
public class AuthController {

    @Autowired
    private AuthService authService;

    public static class LoginResponse {
        private Long id;
        private String email;
        private String fullName;
        private String role;
        
        // constructor
        public LoginResponse(Long id, String email, String fullName, String role) {
            this.id = id;
            this.email = email;
            this.fullName = fullName;
            this.role = role;
        }
        
        // getters & setters
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        
        public String getFullName() { return fullName; }
        public void setFullName(String fullName) { this.fullName = fullName; }
        
        public String getRole() { return role; }
        public void setRole(String role) { this.role = role; }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {
        try {
            User userLogin = authService.login(user.getEmail(), user.getPassword());
            LoginResponse response = new LoginResponse(
                userLogin.getId(),
                userLogin.getEmail(),
                userLogin.getFullName(),
                userLogin.getRole().toString()
            );
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(401).body("Email hoặc mật khẩu không đúng");
        }
    }
} 