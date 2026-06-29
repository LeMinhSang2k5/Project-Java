package com.java.backend.exception;

public class NurseNotFoundException extends RuntimeException {
    public NurseNotFoundException(Long id) {
        super("Không tìm thấy nurse với ID: " + id);
    }
}
