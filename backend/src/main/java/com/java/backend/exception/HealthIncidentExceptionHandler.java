package com.java.backend.exception;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.java.backend.controller.HealthIncidentController;

@RestControllerAdvice(assignableTypes = HealthIncidentController.class)
public class HealthIncidentExceptionHandler {

    @ExceptionHandler(HealthIncidentNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleNotFound(HealthIncidentNotFoundException e) {
        return ResponseEntity.status(404).body(Map.of("message", e.getMessage()));
    }

    @ExceptionHandler(StudentNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleStudentNotFound(StudentNotFoundException e) {
        return ResponseEntity.status(404).body(Map.of("message", e.getMessage()));
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleUserNotFound(UserNotFoundException e) {
        return ResponseEntity.status(404).body(Map.of("message", e.getMessage()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgument(IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Map<String, String>> handleTypeMismatch(MethodArgumentTypeMismatchException e) {
        String paramName = e.getName();
        String message;
        if ("status".equals(paramName)) {
            message = "Trạng thái sự cố không hợp lệ";
        } else if ("incidentTime".equals(paramName)) {
            message = "Thời gian sự cố không đúng định dạng";
        } else if ("incidentType".equals(paramName)) {
            message = "Loại sự cố không hợp lệ";
        } else {
            message = "Giá trị không hợp lệ cho tham số: " + paramName;
        }
        return ResponseEntity.badRequest().body(Map.of("message", message));
    }
}
