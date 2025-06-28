package com.java.backend.controller;

import com.java.backend.entity.Student;
import com.java.backend.service.StudentService;
import com.java.backend.enums.Gender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/students") // Sửa lại endpoint để đồng bộ với frontend gọi /api/students
@CrossOrigin(origins = "http://localhost:3000")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @PostMapping
    public ResponseEntity<?> createStudent(@RequestBody Map<String, Object> requestData) {
        try {
            String email = (String) requestData.get("email");
            String password = (String) requestData.get("password");
            String fullName = (String) requestData.get("fullName");
            String code = (String) requestData.get("code"); // Mã số sinh viên
            String studentClass = (String) requestData.get("studentClass");
            String genderStr = (String) requestData.get("gender");
            String dateOfBirthStr = (String) requestData.get("dateOfBirth");
            
            // Extract parentId
            Long parentId = null;
            if (requestData.get("parentId") != null) {
                if (requestData.get("parentId") instanceof Integer) {
                    parentId = ((Integer) requestData.get("parentId")).longValue();
                } else if (requestData.get("parentId") instanceof Long) {
                    parentId = (Long) requestData.get("parentId");
                }
            }

            // Parse gender
            Gender gender = null;
            if (genderStr != null) {
                try {
                    gender = Gender.valueOf(genderStr.toUpperCase());
                } catch (IllegalArgumentException e) {
                    gender = Gender.MALE; // default
                }
            }

            // Parse date
            LocalDate dateOfBirth = null;
            if (dateOfBirthStr != null && !dateOfBirthStr.trim().isEmpty()) {
                try {
                    dateOfBirth = LocalDate.parse(dateOfBirthStr);
                } catch (Exception e) {
                    dateOfBirth = LocalDate.of(2010, 1, 1); // default
                }
            }

            // Validation
            if (email == null || email.trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("message", "Email không được để trống"));
            }
            if (fullName == null || fullName.trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("message", "Họ tên không được để trống"));
            }
            if (password == null || password.trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("message", "Mật khẩu không được để trống"));
            }
            if (code == null || code.trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("message", "Mã số sinh viên không được để trống"));
            }
            if (studentClass == null || studentClass.trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("message", "Lớp học không được để trống"));
            }

            // Tạo Student object
            Student student = studentService.createStudent(email, password, fullName, studentClass, code, gender,
                    dateOfBirth);
            
            // Liên kết với parent nếu có
            if (parentId != null) {
                studentService.linkStudentToParent(student.getId(), parentId);
            }
            
            Student saved = studentService.saveStudent(student);
            return ResponseEntity.ok(saved);
        } catch (Exception e) {
            System.err.println("Error creating student: " + e.getMessage());
            e.printStackTrace();
            Map<String, String> error = new HashMap<>();
            error.put("message", "Lỗi khi tạo học sinh: " + e.getMessage());
            return ResponseEntity.internalServerError().body(error);
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllStudents() {
        try {
            List<Student> students = studentService.getAllStudents();
            return ResponseEntity.ok(students);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "Lỗi khi lấy danh sách học sinh: " + e.getMessage());
            return ResponseEntity.internalServerError().body(error);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getStudent(@PathVariable Long id) {
        try {
            Student student = studentService.getStudentById(id);
            if (student != null) {
                return ResponseEntity.ok(student);
            } else {
                Map<String, String> error = new HashMap<>();
                error.put("message", "Không tìm thấy học sinh với ID: " + id);
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "Lỗi khi lấy thông tin học sinh: " + e.getMessage());
            return ResponseEntity.internalServerError().body(error);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateStudent(@PathVariable Long id, @RequestBody Student student) {
        try {
            student.setId(id);
            Student updated = studentService.updateStudent(student);
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "Lỗi khi cập nhật học sinh: " + e.getMessage());
            return ResponseEntity.internalServerError().body(error);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteStudent(@PathVariable Long id) {
        try {
            studentService.deleteStudent(id);
            Map<String, String> success = new HashMap<>();
            success.put("message", "Xóa học sinh thành công");
            return ResponseEntity.ok(success);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "Lỗi khi xóa học sinh: " + e.getMessage());
            return ResponseEntity.internalServerError().body(error);
        }
    }

    // Endpoint để lấy danh sách học sinh theo parent ID
    @GetMapping("/by-parent/{parentId}")
    public ResponseEntity<?> getStudentsByParent(@PathVariable Long parentId) {
        try {
            List<Student> students = studentService.getStudentsByParent(parentId);
            return ResponseEntity.ok(students);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "Lỗi khi lấy danh sách học sinh: " + e.getMessage());
            return ResponseEntity.internalServerError().body(error);
        }
    }
}