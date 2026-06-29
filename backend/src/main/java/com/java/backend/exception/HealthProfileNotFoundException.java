package com.java.backend.exception;

public class HealthProfileNotFoundException extends RuntimeException {
    public HealthProfileNotFoundException(Long id) {
        super("Không tìm thấy hồ sơ sức khỏe với ID: " + id);
    }

    public HealthProfileNotFoundException(Long studentId, boolean byStudent) {
        super("Không tìm thấy hồ sơ sức khỏe cho học sinh với ID: " + studentId);
    }
}
