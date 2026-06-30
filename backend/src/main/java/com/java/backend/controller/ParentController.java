package com.java.backend.controller;

import com.java.backend.entity.Parent;
import com.java.backend.entity.Student;
import com.java.backend.enums.Role;
import com.java.backend.repository.MedicalCheckupNotificationRepository;
import com.java.backend.repository.VaccinationScheduleRepository;
import com.java.backend.service.ParentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/api/parent")
@CrossOrigin(origins = "http://localhost:3000")
public class ParentController {

    private static final Pattern PHONE_PATTERN = Pattern.compile("^0\\d{9}$");
    private static final String PHONE_VALIDATION_MESSAGE =
            "Số điện thoại phải gồm đúng 10 chữ số và bắt đầu bằng 0";

    @Autowired
    private ParentService parentService;
    @Autowired
    private MedicalCheckupNotificationRepository medicalCheckupNotificationRepository;
    @Autowired
    private VaccinationScheduleRepository vaccinationScheduleRepository;

    @PostMapping
    public ResponseEntity<Object> createParent(@RequestBody Parent parent) {
        if (parent.getRole() == null) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "Trường role là bắt buộc và phải là PARENT"));
        }
        if (parent.getRole() != Role.PARENT) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "Khi tạo phụ huynh, role chỉ được phép là PARENT"));
        }
        if (parent.getPhoneNumber() == null || parent.getPhoneNumber().trim().isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "Số điện thoại không được để trống"));
        }
        String phoneNumber = parent.getPhoneNumber().trim();
        if (!isValidPhoneNumber(phoneNumber)) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", PHONE_VALIDATION_MESSAGE));
        }
        parent.setPhoneNumber(phoneNumber);

        Parent saved = parentService.saveParent(parent);
        return ResponseEntity.ok(saved);
    }

    private boolean isValidPhoneNumber(String phoneNumber) {
        return phoneNumber != null && PHONE_PATTERN.matcher(phoneNumber).matches();
    }

    @GetMapping("/{parentId}/students")
    public ResponseEntity<Object> getStudentsOfParent(@PathVariable Long parentId) {
        try {
            List<Student> students = parentService.getStudentsOfParent(parentId);
            if (students != null && !students.isEmpty()) {
                return ResponseEntity.ok(students);
            } else {
                return ResponseEntity.ok("Phụ huynh chưa có học sinh nào được liên kết.");
            }
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Parent> getParent(@PathVariable Long id) {
        try {
            Parent parent = parentService.getParentById(id);
            return ResponseEntity.ok(parent);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/link-student")
    public ResponseEntity<Object> linkStudentToParent(@RequestParam Long parentId, @RequestParam Long studentId) {
        try {
            parentService.linkStudentToParent(parentId, studentId);
            return ResponseEntity.ok("Liên kết học sinh với phụ huynh thành công");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Lỗi liên kết: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteParent(@PathVariable Long id) {
        try {
            parentService.deleteParent(id);
            return ResponseEntity.ok("Xóa thành công");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Lỗi xóa phụ huynh: " + e.getMessage());
        }
    }

    // Endpoint để xác nhận phiếu tiêm chủng/kiểm tra y tế
    @PostMapping("/confirm/{formId}")
    public ResponseEntity<Map<String, String>> confirmForm(@PathVariable Long formId) {
        try {
            boolean exists = medicalCheckupNotificationRepository.existsById(formId)
                    || vaccinationScheduleRepository.existsById(formId);
            if (!exists) {
                Map<String, String> error = new HashMap<>();
                error.put("message", "Không tìm thấy phiếu với ID: " + formId);
                return ResponseEntity.status(404).body(error);
            }
            // TODO: cập nhật trạng thái xác nhận thực tế theo loại biểu mẫu.
            Map<String, String> response = new HashMap<>();
            response.put("message", "Xác nhận thành công cho phiếu ID: " + formId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Xác nhận thất bại");
            return ResponseEntity.badRequest().body(error);
        }
    }
}
