package com.java.backend.exception;

public class StudentNotFoundException extends RuntimeException {
    public StudentNotFoundException(Long id) {
        super("Không tìm thấy học sinh với ID: " + id);
    }
}
