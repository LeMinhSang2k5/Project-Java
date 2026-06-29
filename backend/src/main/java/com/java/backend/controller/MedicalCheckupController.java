package com.java.backend.controller;

import com.java.backend.entity.MedicalCheckupNotification;
import com.java.backend.entity.MedicalCheckupResult;
import com.java.backend.entity.Student;
import com.java.backend.exception.StudentNotFoundException;
import com.java.backend.service.MedicalCheckupService;
import com.java.backend.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/medical-checkup")
@CrossOrigin(origins = "http://localhost:3000")
public class MedicalCheckupController {

    @Autowired
    private MedicalCheckupService service;

    @Autowired
    private StudentService studentService;

    @PostMapping("/notify")
    public ResponseEntity<MedicalCheckupNotification> createNotification(
            @RequestBody MedicalCheckupNotification notification) {
        if (notification.getStatus() == null || notification.getStatus().trim().isEmpty()) {
            notification.setStatus("PENDING");
        }
        MedicalCheckupNotification saved = service.saveNotification(notification);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @GetMapping("/notifications")
    public List<MedicalCheckupNotification> getNotificationsByParent(
            @RequestParam(required = false) Long parentId,
            @RequestParam(required = false) Long studentId,
            @RequestParam(required = false) String status) {
        if (parentId != null) {
            return service.getNotificationsByParent(parentId);
        }
        if (studentId != null) {
            return service.getNotificationsByStudent(studentId);
        }
        if (status != null) {
            return service.getNotificationsByStatus(status);
        }
        return service.getAllNotifications();
    }

    @PutMapping("/notification/{id}/consent")
    public ResponseEntity<MedicalCheckupNotification> updateNotificationConsent(
            @PathVariable Long id,
            @RequestBody String status) {
        service.validateConsentStatus(status);
        MedicalCheckupNotification notification = service.getNotificationOrThrow(id);
        notification.setStatus(status.trim().toUpperCase());
        notification.setParentConsentDate(LocalDateTime.now());
        return ResponseEntity.ok(service.saveNotification(notification));
    }

    @PostMapping("/notify/bulk")
    public ResponseEntity<?> createBulkNotifications(@RequestBody Map<String, Object> payload) {
        Object studentIdsObj = payload.get("studentIds");
        if (!(studentIdsObj instanceof List<?> studentIds) || studentIds.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "Danh sách studentIds không được để trống"));
        }

        String content = (String) payload.get("content");
        String scheduledDate = (String) payload.get("scheduledDate");
        if (content == null || content.trim().isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "Nội dung thông báo không được để trống"));
        }
        if (scheduledDate == null || scheduledDate.trim().isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "Ngày khám dự kiến không được để trống"));
        }

        List<MedicalCheckupNotification> notifications = new ArrayList<>();
        for (Object studentIdObj : studentIds) {
            Long studentId = Long.valueOf(studentIdObj.toString());
            Student student = studentService.getStudentById(studentId);
            if (student.getParent() == null || student.getParent().getId() == null) {
                throw new IllegalArgumentException(
                        "Học sinh với ID: " + studentId + " chưa được liên kết với phụ huynh");
            }

            MedicalCheckupNotification notification = new MedicalCheckupNotification();
            notification.setStudentId(studentId);
            notification.setParentId(student.getParent().getId());
            notification.setContent(content);
            notification.setScheduledDate(LocalDateTime.parse(scheduledDate));
            notification.setStatus("PENDING");
            notifications.add(service.saveNotification(notification));
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(notifications);
    }

    @PostMapping("/result")
    public ResponseEntity<MedicalCheckupResult> createResult(@RequestBody MedicalCheckupResult result) {
        MedicalCheckupResult saved = service.saveResult(result);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @GetMapping("/results")
    public List<Map<String, Object>> getResults(
            @RequestParam(required = false) Long parentId,
            @RequestParam(required = false) Long studentId) {
        List<MedicalCheckupResult> results;
        if (parentId != null) {
            results = service.getResultsByParent(parentId);
        } else if (studentId != null) {
            results = service.getResultsByStudent(studentId);
        } else {
            results = List.of();
        }

        return results.stream().map(result -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", result.getId());
            map.put("studentId", result.getStudentId());
            map.put("checkupDate", result.getCheckupDate());
            map.put("result", result.getResult());
            map.put("notes", result.getNotes());
            map.put("abnormal", result.isAbnormal());
            map.put("createdBy", result.getCreatedBy());
            map.put("studentConfirmation", result.getStudentConfirmation());
            map.put("studentConfirmationDate", result.getStudentConfirmationDate());
            try {
                Student student = studentService.getStudentById(result.getStudentId());
                map.put("studentName", student.getFullName());
            } catch (StudentNotFoundException e) {
                map.put("studentName", null);
            }
            return map;
        }).collect(Collectors.toList());
    }

    @PutMapping("/result/{id}")
    public ResponseEntity<MedicalCheckupResult> updateResult(
            @PathVariable Long id,
            @RequestBody MedicalCheckupResult result) {
        service.getResultOrThrow(id);
        result.setId(id);
        return ResponseEntity.ok(service.saveResult(result));
    }

    @PutMapping("/result/{id}/student-confirm")
    public ResponseEntity<MedicalCheckupResult> studentConfirmResult(@PathVariable Long id) {
        MedicalCheckupResult result = service.getResultOrThrow(id);
        result.setStudentConfirmation(true);
        result.setStudentConfirmationDate(LocalDateTime.now());
        return ResponseEntity.ok(service.saveResult(result));
    }
}
