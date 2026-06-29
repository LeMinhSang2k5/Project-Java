package com.java.backend.exception;

public class SchoolHealthDocNotFoundException extends RuntimeException {
    public SchoolHealthDocNotFoundException(Long id) {
        super("Không tìm thấy tài liệu với ID: " + id);
    }
}
