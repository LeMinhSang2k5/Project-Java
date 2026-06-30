package com.java.backend.controller;

import com.java.backend.entity.Student;
import com.java.backend.exception.ParentNotFoundException;
import com.java.backend.exception.StudentNotFoundException;
import com.java.backend.service.StudentService;
import com.java.backend.enums.Gender;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentControllerTest {

    @Mock
    private StudentService studentService;

    @InjectMocks
    private StudentController studentController;

    private Student student;
    private Map<String, Object> requestData;

    @BeforeEach
    void setUp() {
        student = new Student();
        student.setId(1L);
        student.setFullName("Nguyen Van A");

        requestData = new HashMap<>();
        requestData.put("email", "test@example.com");
        requestData.put("password", "password");
        requestData.put("fullName", "Nguyen Van A");
        requestData.put("code", "SV01");
        requestData.put("studentClass", "10A1");
        requestData.put("gender", "MALE");
        requestData.put("dateOfBirth", "2010-01-01");
        requestData.put("city", "Hanoi");
        requestData.put("district", "Ba Dinh");
        requestData.put("grade", "10");
        requestData.put("role", "STUDENT");
    }

    // createStudent
    
    @Test
    void createStudent_Success() {
        when(studentService.createStudent(anyString(), anyString(), anyString(), anyString(), anyString(), any(Gender.class), any(LocalDate.class), anyString(), anyString(), anyString())).thenReturn(student);
        when(studentService.saveStudent(any(Student.class))).thenReturn(student);
        
        ResponseEntity<Object> response = studentController.createStudent(requestData);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void createStudent_SuccessWithParent() {
        requestData.put("parentId", 100);
        when(studentService.parentExists(100L)).thenReturn(true);
        when(studentService.createStudent(anyString(), anyString(), anyString(), anyString(), anyString(), any(Gender.class), any(LocalDate.class), anyString(), anyString(), anyString())).thenReturn(student);
        when(studentService.saveStudent(any(Student.class))).thenReturn(student);
        when(studentService.getStudentById(1L)).thenReturn(student);
        
        ResponseEntity<Object> response = studentController.createStudent(requestData);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(studentService).linkStudentToParent(1L, 100L);
    }
    
    @Test
    void createStudent_InvalidGender_FallbacksToMale() {
        requestData.put("gender", "UNKNOWN");
        when(studentService.createStudent(anyString(), anyString(), anyString(), anyString(), anyString(), eq(Gender.MALE), any(LocalDate.class), anyString(), anyString(), anyString())).thenReturn(student);
        when(studentService.saveStudent(any(Student.class))).thenReturn(student);
        
        ResponseEntity<Object> response = studentController.createStudent(requestData);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void createStudent_NullGender_FallbacksToNull() {
        requestData.remove("gender");
        when(studentService.createStudent(anyString(), anyString(), anyString(), anyString(), anyString(), isNull(), any(LocalDate.class), anyString(), anyString(), anyString())).thenReturn(student);
        when(studentService.saveStudent(any(Student.class))).thenReturn(student);
        
        ResponseEntity<Object> response = studentController.createStudent(requestData);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void createStudent_MissingDob() {
        requestData.remove("dateOfBirth");
        ResponseEntity<Object> response = studentController.createStudent(requestData);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(((Map<?, ?>) response.getBody()).get("message").toString().contains("trống"));
    }

    @Test
    void createStudent_EmptyDob() {
        requestData.put("dateOfBirth", "   ");
        ResponseEntity<Object> response = studentController.createStudent(requestData);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(((Map<?, ?>) response.getBody()).get("message").toString().contains("trống"));
    }

    @Test
    void createStudent_InvalidDobFormat() {
        requestData.put("dateOfBirth", "2010/01/01");
        ResponseEntity<Object> response = studentController.createStudent(requestData);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void createStudent_FutureDob() {
        requestData.put("dateOfBirth", LocalDate.now().plusDays(1).toString());
        ResponseEntity<Object> response = studentController.createStudent(requestData);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void createStudent_MissingFields() {
        requestData.remove("email");
        assertEquals(HttpStatus.BAD_REQUEST, studentController.createStudent(requestData).getStatusCode());
        
        setUp();
        requestData.remove("fullName");
        assertEquals(HttpStatus.BAD_REQUEST, studentController.createStudent(requestData).getStatusCode());

        setUp();
        requestData.remove("password");
        assertEquals(HttpStatus.BAD_REQUEST, studentController.createStudent(requestData).getStatusCode());

        setUp();
        requestData.remove("code");
        assertEquals(HttpStatus.BAD_REQUEST, studentController.createStudent(requestData).getStatusCode());

        setUp();
        requestData.remove("studentClass");
        assertEquals(HttpStatus.BAD_REQUEST, studentController.createStudent(requestData).getStatusCode());
    }

    @Test
    void createStudent_MissingRole() {
        requestData.remove("role");
        assertEquals(HttpStatus.BAD_REQUEST, studentController.createStudent(requestData).getStatusCode());
    }

    @Test
    void createStudent_EmptyRole() {
        requestData.put("role", "  ");
        assertEquals(HttpStatus.BAD_REQUEST, studentController.createStudent(requestData).getStatusCode());
    }

    @Test
    void createStudent_InvalidRole() {
        requestData.put("role", "ADMIN");
        assertEquals(HttpStatus.BAD_REQUEST, studentController.createStudent(requestData).getStatusCode());
    }

    @Test
    void createStudent_EmptyFields() {
        requestData.put("email", "  ");
        assertEquals(HttpStatus.BAD_REQUEST, studentController.createStudent(requestData).getStatusCode());
        
        setUp();
        requestData.put("fullName", "");
        assertEquals(HttpStatus.BAD_REQUEST, studentController.createStudent(requestData).getStatusCode());

        setUp();
        requestData.put("password", "  ");
        assertEquals(HttpStatus.BAD_REQUEST, studentController.createStudent(requestData).getStatusCode());

        setUp();
        requestData.put("code", " ");
        assertEquals(HttpStatus.BAD_REQUEST, studentController.createStudent(requestData).getStatusCode());

        setUp();
        requestData.put("studentClass", "");
        assertEquals(HttpStatus.BAD_REQUEST, studentController.createStudent(requestData).getStatusCode());
    }

    @Test
    void createStudent_ParentNotFound() {
        requestData.put("parentId", 100L);
        when(studentService.parentExists(100L)).thenReturn(false);
        assertEquals(HttpStatus.BAD_REQUEST, studentController.createStudent(requestData).getStatusCode());
    }

    @Test
    void createStudent_Exception() {
        when(studentService.createStudent(any(), any(), any(), any(), any(), any(), any(), any(), any(), any())).thenThrow(new RuntimeException("DB Error"));
        ResponseEntity<Object> response = studentController.createStudent(requestData);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    // getAllStudents

    @Test
    void getAllStudents_Success() {
        when(studentService.getAllStudents()).thenReturn(Arrays.asList(student));
        ResponseEntity<Object> response = studentController.getAllStudents();
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void getAllStudents_Exception() {
        when(studentService.getAllStudents()).thenThrow(new RuntimeException("Error"));
        ResponseEntity<Object> response = studentController.getAllStudents();
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    // getStudent

    @Test
    void getStudent_Success() {
        when(studentService.getStudentById(1L)).thenReturn(student);
        ResponseEntity<Object> response = studentController.getStudent(1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void getStudent_NotFound() {
        when(studentService.getStudentById(1L)).thenThrow(new StudentNotFoundException(1L));
        ResponseEntity<Object> response = studentController.getStudent(1L);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void getStudent_Exception() {
        when(studentService.getStudentById(1L)).thenThrow(new RuntimeException("Error"));
        ResponseEntity<Object> response = studentController.getStudent(1L);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    // updateStudent

    @Test
    void updateStudent_Success() {
        requestData.put("parentId", "100");
        requestData.put("gender", "FEMALE");
        when(studentService.getStudentById(1L)).thenReturn(student);
        when(studentService.updateStudent(any(Student.class))).thenReturn(student);
        when(studentService.parentExists(100L)).thenReturn(true);

        ResponseEntity<Object> response = studentController.updateStudent(1L, requestData);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(Gender.FEMALE, student.getGender());
    }

    @Test
    void updateStudent_InvalidGenderFormat() {
        requestData.put("gender", "UNKNOWN");
        when(studentService.getStudentById(1L)).thenReturn(student);
        when(studentService.updateStudent(any(Student.class))).thenReturn(student);

        ResponseEntity<Object> response = studentController.updateStudent(1L, requestData);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void updateStudent_ParentIdFormatError() {
        requestData.put("parentId", "not_a_number");
        when(studentService.getStudentById(1L)).thenReturn(student);
        when(studentService.updateStudent(any(Student.class))).thenReturn(student);

        ResponseEntity<Object> response = studentController.updateStudent(1L, requestData);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void updateStudent_AlternateDob() {
        requestData.remove("dateOfBirth");
        requestData.put("date_of_birth", "2010-01-01");
        when(studentService.getStudentById(1L)).thenReturn(student);
        when(studentService.updateStudent(any(Student.class))).thenReturn(student);

        ResponseEntity<Object> response = studentController.updateStudent(1L, requestData);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void updateStudent_InvalidDob() {
        requestData.put("dateOfBirth", "invalid");
        when(studentService.getStudentById(1L)).thenReturn(student);
        
        ResponseEntity<Object> response = studentController.updateStudent(1L, requestData);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void updateStudent_FutureDob() {
        requestData.put("dateOfBirth", LocalDate.now().plusDays(1).toString());
        when(studentService.getStudentById(1L)).thenReturn(student);
        
        ResponseEntity<Object> response = studentController.updateStudent(1L, requestData);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void updateStudent_NotFound() {
        when(studentService.getStudentById(1L)).thenThrow(new StudentNotFoundException(1L));
        ResponseEntity<Object> response = studentController.updateStudent(1L, requestData);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void updateStudent_ParentNotFound() {
        when(studentService.getStudentById(1L)).thenReturn(student);
        when(studentService.updateStudent(any(Student.class))).thenReturn(student);
        requestData.put("parentId", 100);
        when(studentService.parentExists(100L)).thenReturn(false);
        
        ResponseEntity<Object> response = studentController.updateStudent(1L, requestData);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void updateStudent_Exception() {
        when(studentService.getStudentById(1L)).thenThrow(new RuntimeException("Error"));
        ResponseEntity<Object> response = studentController.updateStudent(1L, requestData);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    // deleteStudent

    @Test
    void deleteStudent_Success() {
        doNothing().when(studentService).deleteStudent(1L);
        ResponseEntity<Object> response = studentController.deleteStudent(1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void deleteStudent_NotFound() {
        doThrow(new StudentNotFoundException(1L)).when(studentService).deleteStudent(1L);
        ResponseEntity<Object> response = studentController.deleteStudent(1L);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void deleteStudent_Exception() {
        doThrow(new RuntimeException("Error")).when(studentService).deleteStudent(1L);
        ResponseEntity<Object> response = studentController.deleteStudent(1L);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    // getStudentsByParent

    @Test
    void getStudentsByParent_Success() {
        when(studentService.getStudentsByParent(1L)).thenReturn(Arrays.asList(student));
        ResponseEntity<Object> response = studentController.getStudentsByParent(1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void getStudentsByParent_ParentNotFound() {
        when(studentService.getStudentsByParent(1L)).thenThrow(new ParentNotFoundException(1L));
        ResponseEntity<Object> response = studentController.getStudentsByParent(1L);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void getStudentsByParent_Exception() {
        when(studentService.getStudentsByParent(1L)).thenThrow(new RuntimeException("Error"));
        ResponseEntity<Object> response = studentController.getStudentsByParent(1L);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }
}
