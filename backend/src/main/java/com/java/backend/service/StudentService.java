package com.java.backend.service;

import com.java.backend.entity.Student;
import com.java.backend.repository.StudentRepository;
import com.java.backend.repository.UserRepository;
import com.java.backend.enums.Gender;
import com.java.backend.enums.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private UserRepository userRepository;

    // Method để tạo Student từ các parameters riêng lẻ
    public Student createStudent(String email, String password, String fullName,
            String studentClass, String code, Gender gender, LocalDate dateOfBirth) {
        Student student = new Student();

        // Set User fields
        student.setEmail(email);
        student.setPassword(password);
        student.setFullName(fullName);
        student.setRole(Role.STUDENT);
        student.setActive(true);

        // Set Student fields
        student.setCode(code);
        student.setStudentClass(studentClass);
        student.setGender(gender != null ? gender : Gender.MALE);
        student.setDateOfBirth(dateOfBirth != null ? dateOfBirth : LocalDate.of(2010, 1, 1));

        return student;
    }

    public Student saveStudent(Student student) {
        try {
            System.out.println("=== StudentService Debug ===");
            System.out.println("Before validation - Email: " + student.getEmail());
            System.out.println("Before validation - FullName: " + student.getFullName());
            System.out.println("Before validation - Password: " + student.getPassword());
            System.out.println("Before validation - Code: " + student.getCode());
            System.out.println("Before validation - StudentClass: " + student.getStudentClass());

            // Validation
            if (student.getEmail() == null || student.getEmail().trim().isEmpty()) {
                throw new RuntimeException("Email không được để trống");
            }
            if (student.getFullName() == null || student.getFullName().trim().isEmpty()) {
                throw new RuntimeException("Họ tên không được để trống");
            }
            if (student.getPassword() == null || student.getPassword().trim().isEmpty()) {
                throw new RuntimeException("Mật khẩu không được để trống");
            }
            if (student.getCode() == null || student.getCode().trim().isEmpty()) {
                throw new RuntimeException("Mã số sinh viên không được để trống");
            }
            if (student.getStudentClass() == null || student.getStudentClass().trim().isEmpty()) {
                throw new RuntimeException("Lớp học không được để trống");
            }

            // Kiểm tra email đã tồn tại trong bảng users (vì Student extends User)
            if (userRepository.findByEmail(student.getEmail()).isPresent()) {
                throw new RuntimeException("Email đã tồn tại trong hệ thống");
            }

            // Kiểm tra mã số sinh viên đã tồn tại
            if (studentRepository.findByCode(student.getCode()).isPresent()) {
                throw new RuntimeException("Mã số sinh viên đã tồn tại");
            }

            // Đặt giá trị mặc định nếu chưa có
            if (student.getDateOfBirth() == null) {
                student.setDateOfBirth(java.time.LocalDate.of(2010, 1, 1));
            }
            if (student.getGender() == null) {
                student.setGender(com.java.backend.enums.Gender.MALE);
            }

            // Đảm bảo role được set là STUDENT
            student.setRole(com.java.backend.enums.Role.STUDENT);

            // Đảm bảo isActive được set
            student.setActive(true);

            System.out.println("Before save - Email: " + student.getEmail());
            System.out.println("Before save - FullName: " + student.getFullName());
            System.out.println("Before save - Password: " + student.getPassword());
            System.out.println("Before save - Code: " + student.getCode());
            System.out.println("Before save - Role: " + student.getRole());
            System.out.println("Before save - IsActive: " + student.isActive());
            System.out.println("Before save - StudentClass: " + student.getStudentClass());
            System.out.println("Before save - Gender: " + student.getGender());
            System.out.println("Before save - DateOfBirth: " + student.getDateOfBirth());
            System.out.println("Before save - ID: " + student.getId());
            System.out.println("============================");

            return studentRepository.save(student);
        } catch (Exception e) {
            System.err.println("StudentService Error: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Lỗi khi lưu học sinh: " + e.getMessage(), e);
        }
    }

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public Student getStudentById(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + id));
    }

    public Student updateStudent(Student student) {
        try {
            if (!studentRepository.existsById(student.getId())) {
                throw new RuntimeException("Student not found with id: " + student.getId());
            }
            return studentRepository.save(student);
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi cập nhật học sinh: " + e.getMessage(), e);
        }
    }

    public void deleteStudent(Long id) {
        try {
            if (!studentRepository.existsById(id)) {
                throw new RuntimeException("Student not found with id: " + id);
            }
            studentRepository.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi xóa học sinh: " + e.getMessage(), e);
        }
    }
}