package com.java.backend.exception;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.java.backend.controller.VaccinationScheduleController;

@RestControllerAdvice(assignableTypes = VaccinationScheduleController.class)
public class VaccinationScheduleExceptionHandler {

    @ExceptionHandler(VaccinationScheduleNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleNotFound(VaccinationScheduleNotFoundException e) {
        return ResponseEntity.status(404).body(Map.of("message", e.getMessage()));
    }

    @ExceptionHandler(StudentNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleStudentNotFound(StudentNotFoundException e) {
        return ResponseEntity.status(404).body(Map.of("message", e.getMessage()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgument(IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, String>> handleUnreadable(HttpMessageNotReadableException e) {
        return ResponseEntity.badRequest()
                .body(Map.of("message", "Dữ liệu không hợp lệ hoặc định dạng không đúng"));
    }
}
