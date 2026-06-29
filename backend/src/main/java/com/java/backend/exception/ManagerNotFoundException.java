package com.java.backend.exception;

public class ManagerNotFoundException extends RuntimeException {
    public ManagerNotFoundException(Long id) {
        super("Không tìm thấy manager với ID: " + id);
    }
}
