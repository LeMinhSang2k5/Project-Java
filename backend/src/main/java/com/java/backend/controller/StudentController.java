package com.java.backend.controller;

import com.java.backend.entity.Student;
import com.java.backend.exception.ParentNotFoundException;
import com.java.backend.exception.StudentNotFoundException;
import com.java.backend.service.StudentService;
import com.java.backend.enums.Gender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.format.DateTimeParseException;
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
            String city = (String) requestData.get("city");
            String district = (String) requestData.get("district");
            String grade = (String) requestData.get("grade");

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

            if (dateOfBirthStr == null || dateOfBirthStr.trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("message", "Ngày sinh không được để trống"));
            }

            try {
                dateOfBirth = LocalDate.parse(dateOfBirthStr);

            } catch (DateTimeParseException e) {
                return ResponseEntity.badRequest()
                        .body(Map.of("message", "Ngày sinh không đúng định dạng yyyy-MM-dd"));
            }

            // Kiểm tra ngày tương lai
            if (dateOfBirth.isAfter(LocalDate.now())) {
                return ResponseEntity.badRequest()
                        .body(Map.of("message", "Ngày sinh không được ở tương lai"));
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

            Object roleObj = requestData.get("role");
            if (roleObj == null || roleObj.toString().trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("message", "Trường role là bắt buộc và phải là STUDENT"));
            }
            if (!"STUDENT".equalsIgnoreCase(roleObj.toString().trim())) {
                return ResponseEntity.badRequest()
                        .body(Map.of("message", "Khi tạo học sinh, role chỉ được phép là STUDENT"));
            }

            if (parentId != null && !studentService.parentExists(parentId)) {
                return ResponseEntity.badRequest()
                        .body(Map.of("message", "Không tìm thấy phụ huynh với ID: " + parentId));
            }

            // Tạo Student object
            Student student = studentService.createStudent(email, password, fullName, studentClass, code, gender,
                    dateOfBirth, city, district, grade);

            // Lưu trước để có ID, sau đó liên kết với parent nếu có
            Student saved = studentService.saveStudent(student);

            if (parentId != null) {
                studentService.linkStudentToParent(saved.getId(), parentId);
                // load again để đảm bảo parent được nạp và trả về
                saved = studentService.getStudentById(saved.getId());
            }

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
            return ResponseEntity.ok(student);
        } catch (StudentNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "Lỗi khi lấy thông tin học sinh: " + e.getMessage());
            return ResponseEntity.internalServerError().body(error);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateStudent(@PathVariable Long id, @RequestBody Map<String, Object> requestData) {
        try {
            // Lấy student hiện tại
            Student existing = studentService.getStudentById(id);

            // Cập nhật các trường nếu có trong request
            if (requestData.get("email") != null)
                existing.setEmail((String) requestData.get("email"));
            if (requestData.get("password") != null)
                existing.setPassword((String) requestData.get("password"));
            if (requestData.get("fullName") != null)
                existing.setFullName((String) requestData.get("fullName"));
            if (requestData.get("code") != null)
                existing.setCode((String) requestData.get("code"));
            if (requestData.get("studentClass") != null)
                existing.setStudentClass((String) requestData.get("studentClass"));
            if (requestData.get("gender") != null) {
                try {
                    existing.setGender(
                            com.java.backend.enums.Gender.valueOf(((String) requestData.get("gender")).toUpperCase()));
                } catch (Exception ignore) {
                }
            }

            // Hỗ trợ cả "dateOfBirth" và "date_of_birth"
            Object dobObj = requestData.get("dateOfBirth");
            if (dobObj == null)
                dobObj = requestData.get("date_of_birth");
            if (dobObj != null) {
                String dobStr = dobObj.toString();
                LocalDate dob;
                try {
                    dob = LocalDate.parse(dobStr);
                } catch (DateTimeParseException e) {
                    return ResponseEntity.badRequest()
                            .body(Map.of("message", "Ngày sinh không đúng định dạng yyyy-MM-dd"));
                }
                if (dob.isAfter(LocalDate.now())) {
                    return ResponseEntity.badRequest().body(Map.of("message", "Ngày sinh không được ở tương lai"));
                }
                existing.setDateOfBirth(dob);
            }

            // Lưu các thay đổi trước khi liên kết parent
            Student saved = studentService.updateStudent(existing);

            // Lấy parentId từ nhiều biến thể: parentId, parent_id
            Object parentVal = requestData.get("parentId");
            if (parentVal == null)
                parentVal = requestData.get("parent_id");
            Long parentId = null;
            if (parentVal != null) {
                if (parentVal instanceof Number) {
                    parentId = ((Number) parentVal).longValue();
                } else {
                    try {
                        parentId = Long.parseLong(parentVal.toString());
                    } catch (NumberFormatException ignore) {
                    }
                }
            }

            if (parentId != null) {
                if (!studentService.parentExists(parentId)) {
                    return ResponseEntity.badRequest()
                            .body(Map.of("message", "Không tìm thấy phụ huynh với ID: " + parentId));
                }
                studentService.linkStudentToParent(saved.getId(), parentId);
                saved = studentService.getStudentById(saved.getId());
            }

            return ResponseEntity.ok(saved);
        } catch (StudentNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (ParentNotFoundException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
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
        } catch (StudentNotFoundException e) {
            return ResponseEntity.notFound().build();
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
        } catch (ParentNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "Lỗi khi lấy danh sách học sinh: " + e.getMessage());
            return ResponseEntity.internalServerError().body(error);
        }
    }
}