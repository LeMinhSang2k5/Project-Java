package com.java.backend.service;

import com.java.backend.entity.User;
import com.java.backend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AuthService authService;

    private User activeUser;
    private User inactiveUser;

    @BeforeEach
    void setUp() {
        activeUser = new User();
        activeUser.setEmail("test@gmail.com");
        activeUser.setPassword("123456");
        activeUser.setActive(true);

        inactiveUser = new User();
        inactiveUser.setEmail("inactive@gmail.com");
        inactiveUser.setPassword("123456");
        inactiveUser.setActive(false);
    }

    @Test
    void login_Success() {
        when(userRepository.findByEmail("test@gmail.com")).thenReturn(Optional.of(activeUser));
        User result = authService.login("test@gmail.com", "123456");
        assertNotNull(result);
        assertEquals("test@gmail.com", result.getEmail());
    }

    @Test
    void login_UserNotFound() {
        when(userRepository.findByEmail("unknown@gmail.com")).thenReturn(Optional.empty());
        Exception exception = assertThrows(RuntimeException.class, () -> {
            authService.login("unknown@gmail.com", "123456");
        });
        assertEquals("Invalid password", exception.getMessage());
    }

    @Test
    void login_UserInactive() {
        when(userRepository.findByEmail("inactive@gmail.com")).thenReturn(Optional.of(inactiveUser));
        Exception exception = assertThrows(RuntimeException.class, () -> {
            authService.login("inactive@gmail.com", "123456");
        });
        assertEquals("Invalid password", exception.getMessage());
    }

    @Test
    void login_WrongPassword() {
        when(userRepository.findByEmail("test@gmail.com")).thenReturn(Optional.of(activeUser));
        Exception exception = assertThrows(RuntimeException.class, () -> {
            authService.login("test@gmail.com", "wrongpass");
        });
        assertEquals("Invalid password", exception.getMessage());
    }
}
