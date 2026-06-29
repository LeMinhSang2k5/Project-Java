package com.java.backend.exception;

public class MedicalCheckupNotificationNotFoundException extends RuntimeException {
    public MedicalCheckupNotificationNotFoundException(Long id) {
        super("Không tìm thấy thông báo khám với ID: " + id);
    }
}
