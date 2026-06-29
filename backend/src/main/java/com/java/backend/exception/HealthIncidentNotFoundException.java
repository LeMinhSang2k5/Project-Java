package com.java.backend.exception;

public class HealthIncidentNotFoundException extends RuntimeException {
    public HealthIncidentNotFoundException(Long id) {
        super("Không tìm thấy sự cố y tế với ID: " + id);
    }
}
