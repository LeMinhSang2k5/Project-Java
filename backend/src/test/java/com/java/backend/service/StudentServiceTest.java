package com.java.backend.service;

import com.java.backend.entity.Parent;
import com.java.backend.entity.Student;
import com.java.backend.entity.User;
import com.java.backend.enums.Gender;
import com.java.backend.enums.Role;
import com.java.backend.exception.ParentNotFoundException;
import com.java.backend.exception.StudentNotFoundException;
import com.java.backend.repository.ParentRepository;
import com.java.backend.repository.StudentRepository;
import com.java.backend.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ParentRepository parentRepository;

    @InjectMocks
    private StudentService studentService;

    @Test
    void createStudent_shouldPopulateDefaultsWhenNullValues() {
        Student student = studentService.createStudent(
                "student@example.com", "secret", "Alice", "10A", "S001",
                null, null, "Hanoi", "Cau Giay", "10");

        assertEquals(Role.STUDENT, student.getRole());
        assertTrue(student.isActive());
        assertEquals(Gender.MALE, student.getGender()); // default is MALE
        assertEquals(LocalDate.of(2010, 1, 1), student.getDateOfBirth()); // default
        assertEquals("10A", student.getStudentClass());
        assertEquals("S001", student.getCode());
    }

    @Test
    void createStudent_shouldPopulateValues() {
        Student student = studentService.createStudent(
                "student@example.com", "secret", "Alice", "10A", "S001",
                Gender.FEMALE, LocalDate.of(2005, 1, 1), "Hanoi", "Cau Giay", "10");

        assertEquals(Gender.FEMALE, student.getGender());
        assertEquals(LocalDate.of(2005, 1, 1), student.getDateOfBirth());
    }

    @Test
    void saveStudent_shouldSaveSuccessfully() {
        Student student = new Student();
        student.setEmail("new@example.com");
        when(userRepository.findByEmail("new@example.com")).thenReturn(Optional.empty());
        when(studentRepository.save(student)).thenReturn(student);

        Student result = studentService.saveStudent(student);

        assertNotNull(result);
        verify(studentRepository).save(student);
    }

    @Test
    void saveStudent_shouldThrowWhenEmailAlreadyExists() {
        Student student = new Student();
        student.setEmail("dup@example.com");
        student.setId(1L);
        User existingUser = new User();
        existingUser.setId(2L);
        existingUser.setEmail("dup@example.com");

        when(userRepository.findByEmail("dup@example.com")).thenReturn(Optional.of(existingUser));

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> studentService.saveStudent(student));

        assertTrue(exception.getMessage().contains("Email đã tồn tại"));
        verify(studentRepository, never()).save(any());
    }

    @Test
    void saveStudent_shouldThrowWhenRepositoryThrows() {
        Student student = new Student();
        student.setEmail("error@example.com");
        when(userRepository.findByEmail("error@example.com")).thenReturn(Optional.empty());
        when(studentRepository.save(student)).thenThrow(new IllegalArgumentException("DB Error"));

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> studentService.saveStudent(student));

        assertTrue(exception.getMessage().contains("Lỗi khi lưu học sinh"));
    }

    @Test
    void getAllStudents_shouldReturnList() {
        when(studentRepository.findAll()).thenReturn(List.of(new Student()));
        List<Student> result = studentService.getAllStudents();
        assertEquals(1, result.size());
    }

    @Test
    void getStudentById_shouldReturnStudent() {
        Student student = new Student();
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        Student result = studentService.getStudentById(1L);
        assertNotNull(result);
    }

    @Test
    void getStudentById_shouldThrowStudentNotFoundException() {
        when(studentRepository.findById(404L)).thenReturn(Optional.empty());

        StudentNotFoundException exception = assertThrows(StudentNotFoundException.class,
                () -> studentService.getStudentById(404L));

        assertTrue(exception.getMessage().contains("404"));
    }

    @Test
    void parentExists_shouldReturnTrueIfExists() {
        when(parentRepository.existsById(1L)).thenReturn(true);
        assertTrue(studentService.parentExists(1L));
    }

    @Test
    void parentExists_shouldReturnFalseIfNotExists() {
        when(parentRepository.existsById(2L)).thenReturn(false);
        assertFalse(studentService.parentExists(2L));
    }

    @Test
    void updateStudent_shouldSaveSuccessfully() {
        Student student = new Student();
        student.setId(1L);
        when(studentRepository.existsById(1L)).thenReturn(true);
        when(studentRepository.save(student)).thenReturn(student);

        Student result = studentService.updateStudent(student);

        assertNotNull(result);
        verify(studentRepository).save(student);
    }

    @Test
    void updateStudent_shouldThrowWhenStudentDoesNotExist() {
        Student student = new Student();
        student.setId(12L);
        when(studentRepository.existsById(12L)).thenReturn(false);

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> studentService.updateStudent(student));

        assertTrue(exception.getMessage().contains("Student not found"));
    }

    @Test
    void updateStudent_shouldThrowWhenRepositoryThrows() {
        Student student = new Student();
        student.setId(1L);
        when(studentRepository.existsById(1L)).thenReturn(true);
        when(studentRepository.save(student)).thenThrow(new IllegalArgumentException("DB Error"));

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> studentService.updateStudent(student));

        assertTrue(exception.getMessage().contains("Lỗi khi cập nhật học sinh"));
    }

    @Test
    void deleteStudent_shouldDeleteSuccessfully() {
        when(studentRepository.existsById(1L)).thenReturn(true);
        studentService.deleteStudent(1L);
        verify(studentRepository).deleteById(1L);
    }

    @Test
    void deleteStudent_shouldThrowWhenStudentDoesNotExist() {
        when(studentRepository.existsById(99L)).thenReturn(false);

        StudentNotFoundException exception = assertThrows(StudentNotFoundException.class,
                () -> studentService.deleteStudent(99L));

        assertTrue(exception.getMessage().contains("99"));
    }

    @Test
    void getStudentsByParent_shouldReturnList() {
        when(parentRepository.existsById(1L)).thenReturn(true);
        when(studentRepository.findByParentId(1L)).thenReturn(List.of(new Student()));

        List<Student> result = studentService.getStudentsByParent(1L);
        assertEquals(1, result.size());
    }

    @Test
    void getStudentsByParent_shouldThrowWhenParentMissing() {
        when(parentRepository.existsById(7L)).thenReturn(false);

        ParentNotFoundException exception = assertThrows(ParentNotFoundException.class,
                () -> studentService.getStudentsByParent(7L));

        assertTrue(exception.getMessage().contains("7"));
    }

    @Test
    void linkStudentToParent_shouldPersistRelationship() {
        Student student = new Student();
        student.setId(1L);
        Parent parent = new Parent();
        parent.setId(2L);

        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(parentRepository.findById(2L)).thenReturn(Optional.of(parent));
        when(studentRepository.save(any(Student.class))).thenAnswer(invocation -> invocation.getArgument(0));

        studentService.linkStudentToParent(1L, 2L);

        assertSame(parent, student.getParent());
        verify(studentRepository).save(student);
    }

    @Test
    void linkStudentToParent_shouldThrowWhenStudentMissing() {
        when(studentRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(StudentNotFoundException.class,
                () -> studentService.linkStudentToParent(1L, 2L));
    }

    @Test
    void linkStudentToParent_shouldThrowWhenParentMissing() {
        when(studentRepository.findById(1L)).thenReturn(Optional.of(new Student()));
        when(parentRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(ParentNotFoundException.class,
                () -> studentService.linkStudentToParent(1L, 2L));
    }
}
