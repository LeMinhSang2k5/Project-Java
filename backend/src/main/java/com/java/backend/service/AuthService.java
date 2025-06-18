package com.java.backend.service;

import com.java.backend.entity.User;
import com.java.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    public User login(String email, String password) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isEmpty()) {
            throw new RuntimeException("Invalid password");
        }
        User user = optionalUser.get();
        if (!user.isActive() || !password.equals(user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }
        return user;
    }
} 