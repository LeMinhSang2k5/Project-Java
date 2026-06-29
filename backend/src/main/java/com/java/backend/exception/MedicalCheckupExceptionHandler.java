package com.java.backend.exception;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.java.backend.controller.MedicalCheckupController;

@RestControllerAdvice(assignableTypes = MedicalCheckupController.class)
public class MedicalCheckupExceptionHandler {

    @ExceptionHandler(MedicalCheckupNotificationNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleNotificationNotFound(
            MedicalCheckupNotificationNotFoundException e) {
        return ResponseEntity.status(404).body(Map.of("message", e.getMessage()));
    }

    @ExceptionHandler(MedicalCheckupResultNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleResultNotFound(MedicalCheckupResultNotFoundException e) {
        return ResponseEntity.status(404).body(Map.of("message", e.getMessage()));
    }

    @ExceptionHandler(StudentNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleStudentNotFound(StudentNotFoundException e) {
        return ResponseEntity.status(404).body(Map.of("message", e.getMessage()));
    }

    @ExceptionHandler(ParentNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleParentNotFound(ParentNotFoundException e) {
        return ResponseEntity.status(404).body(Map.of("message", e.getMessage()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgument(IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, String>> handleUnreadable(HttpMessageNotReadableException e) {
        return ResponseEntity.badRequest()
                .body(Map.of("message", "Dữ liệu không hợp lệ hoặc định dạng ngày giờ không đúng"));
    }
}
