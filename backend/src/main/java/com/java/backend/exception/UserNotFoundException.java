package com.java.backend.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(Long id) {
        super("Không tìm thấy user với ID: " + id);
    }
}
