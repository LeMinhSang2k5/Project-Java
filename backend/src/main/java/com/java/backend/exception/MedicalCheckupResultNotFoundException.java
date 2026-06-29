package com.java.backend.exception;

public class MedicalCheckupResultNotFoundException extends RuntimeException {
    public MedicalCheckupResultNotFoundException(Long id) {
        super("Không tìm thấy kết quả khám với ID: " + id);
    }
}
