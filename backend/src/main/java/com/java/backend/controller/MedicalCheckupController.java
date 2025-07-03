package com.java.backend.controller;

import com.java.backend.entity.MedicalCheckupNotification;
import com.java.backend.entity.MedicalCheckupResult;
import com.java.backend.service.MedicalCheckupService;
import com.java.backend.service.StudentService;
import com.java.backend.entity.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.ArrayList;
import java.time.LocalDateTime;
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

    // Notification
    @PostMapping("/notify")
    public MedicalCheckupNotification createNotification(@RequestBody MedicalCheckupNotification n) {
        return service.saveNotification(n);
    }

    @GetMapping("/notifications")
    public List<MedicalCheckupNotification> getNotificationsByParent(@RequestParam(required = false) Long parentId,
            @RequestParam(required = false) Long studentId, @RequestParam(required = false) String status) {
        if (parentId != null)
            return service.getNotificationsByParent(parentId);
        if (studentId != null)
            return service.getNotificationsByStudent(studentId);
        if (status != null)
            return service.getNotificationsByStatus(status);
        // Nếu không truyền gì, trả về tất cả
        return service.getAllNotifications();
    }

    @PutMapping("/notification/{id}/consent")
    public MedicalCheckupNotification updateNotificationConsent(@PathVariable Long id, @RequestBody String status) {
        MedicalCheckupNotification n = service.getNotification(id);
        if (n != null) {
            n.setStatus(status);
            n.setParentConsentDate(java.time.LocalDateTime.now());
            return service.saveNotification(n);
        }
        return null;
    }

    @PostMapping("/notify/bulk")
    public List<MedicalCheckupNotification> createBulkNotifications(@RequestBody Map<String, Object> payload) {
        List<Integer> studentIds = (List<Integer>) payload.get("studentIds");
        String content = (String) payload.get("content");
        String scheduledDate = (String) payload.get("scheduledDate");

        List<MedicalCheckupNotification> notifications = new ArrayList<>();
        for (Integer id : studentIds) {
            MedicalCheckupNotification n = new MedicalCheckupNotification();
            n.setStudentId(id.longValue());
            Student student = studentService.getStudentById(id.longValue());
            if (student.getParent() == null || student.getParent().getId() == null)
                continue;
            n.setParentId(student.getParent().getId());
            n.setContent(content);
            n.setScheduledDate(LocalDateTime.parse(scheduledDate));
            n.setStatus("PENDING");
            notifications.add(service.saveNotification(n));
        }
        return notifications;
    }

    // Result
    @PostMapping("/result")
    public MedicalCheckupResult createResult(@RequestBody MedicalCheckupResult r) {
        return service.saveResult(r);
    }

    @GetMapping("/results")
    public List<Map<String, Object>> getResults(@RequestParam(required = false) Long parentId,
            @RequestParam(required = false) Long studentId) {
        List<MedicalCheckupResult> results;
        if (parentId != null) {
            results = service.getResultsByParent(parentId);
        } else if (studentId != null) {
            results = service.getResultsByStudent(studentId);
        } else {
            results = List.of();
        }
        // Bổ sung tên học sinh vào từng kết quả
        return results.stream().map(r -> {
            Map<String, Object> map = new java.util.HashMap<>();
            map.put("id", r.getId());
            map.put("studentId", r.getStudentId());
            map.put("checkupDate", r.getCheckupDate());
            map.put("result", r.getResult());
            map.put("notes", r.getNotes());
            map.put("abnormal", r.isAbnormal());
            map.put("createdBy", r.getCreatedBy());
            map.put("studentConfirmation", r.getStudentConfirmation());
            map.put("studentConfirmationDate", r.getStudentConfirmationDate());
            // Lấy tên học sinh
            Student s = studentService.getStudentById(r.getStudentId());
            map.put("studentName", s != null ? s.getFullName() : null);
            return map;
        }).collect(Collectors.toList());
    }

    @PutMapping("/result/{id}")
    public MedicalCheckupResult updateResult(@PathVariable Long id, @RequestBody MedicalCheckupResult r) {
        MedicalCheckupResult old = service.getResult(id);
        if (old != null) {
            r.setId(id);
            return service.saveResult(r);
        }
        return null;
    }

    @PutMapping("/result/{id}/student-confirm")
    public MedicalCheckupResult studentConfirmResult(@PathVariable Long id) {
        MedicalCheckupResult result = service.getResult(id);
        if (result != null) {
            result.setStudentConfirmation(true);
            result.setStudentConfirmationDate(LocalDateTime.now());
            return service.saveResult(result);
        }
        return null;
    }
}