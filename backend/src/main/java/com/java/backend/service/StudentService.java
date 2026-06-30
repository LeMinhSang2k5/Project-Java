package com.java.backend.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.java.backend.entity.Student;
import com.java.backend.exception.ParentNotFoundException;
import com.java.backend.exception.StudentNotFoundException;
import com.java.backend.repository.StudentRepository;
import com.java.backend.repository.UserRepository;
import com.java.backend.repository.ParentRepository;
import com.java.backend.enums.Gender;
import com.java.backend.enums.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

@Service
public class StudentService {
    private static final Logger logger = LoggerFactory.getLogger(StudentService.class);


    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ParentRepository parentRepository;

    // Method để tạo Student từ các parameters riêng lẻ
    public Student createStudent(String email, String password, String fullName,
            String studentClass, String code, Gender gender, LocalDate dateOfBirth,
            String city, String district, String grade) {
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
        student.setCity(city);
        student.setDistrict(district);
        student.setGrade(grade);

        return student;
    }

    public Student saveStudent(Student student) {
        try {
            if (userRepository.findByEmail(student.getEmail())
                    .filter(u -> !u.getId().equals(student.getId()))
                    .isPresent()) {
                throw new RuntimeException("Email đã tồn tại");
            }
            return studentRepository.save(student);
        } catch (Exception e) {
            logger.error("StudentService Error: {}", e.getMessage());
            logger.error(e.getMessage(), e);
            throw new RuntimeException("Lỗi khi lưu học sinh: " + e.getMessage(), e);
        }
    }

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public Student getStudentById(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new StudentNotFoundException(id));
    }

    public boolean parentExists(Long parentId) {
        return parentRepository.existsById(parentId);
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
        if (!studentRepository.existsById(id)) {
            throw new StudentNotFoundException(id);
        }
        studentRepository.deleteById(id);
    }

    // Method để lấy danh sách học sinh theo parent ID
    public List<Student> getStudentsByParent(Long parentId) {
        if (!parentRepository.existsById(parentId)) {
            throw new ParentNotFoundException(parentId);
        }
        return studentRepository.findByParentId(parentId);
    }

    // Method để liên kết học sinh với phụ huynh
    public void linkStudentToParent(Long studentId, Long parentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new StudentNotFoundException(studentId));

        com.java.backend.entity.Parent parent = parentRepository.findById(parentId)
                .orElseThrow(() -> new ParentNotFoundException(parentId));

        student.setParent(parent);
        studentRepository.save(student);
    }

}