package com.java.backend.exception;

public class ParentNotFoundException extends RuntimeException {
    public ParentNotFoundException(Long id) {
        super("Không tìm thấy phụ huynh với ID: " + id);
    }
}
