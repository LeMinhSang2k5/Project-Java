package com.java.backend.service;

import com.java.backend.entity.HealthProfile;
import com.java.backend.entity.Parent;
import com.java.backend.entity.Student;
import com.java.backend.enums.Role;
import com.java.backend.repository.HealthProfileRepository;
import com.java.backend.repository.ParentRepository;
import com.java.backend.repository.StudentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ParentServiceTest {

    @Mock
    private ParentRepository parentRepository;

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private HealthProfileRepository healthProfileRepository;

    @InjectMocks
    private ParentService parentService;

    @Test
    void saveParent_shouldSetRoleAndSave() {
        Parent parent = new Parent();
        parent.setFullName("Bob");
        when(parentRepository.save(any(Parent.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Parent result = parentService.saveParent(parent);

        assertEquals(Role.PARENT, result.getRole());
        verify(parentRepository).save(parent);
    }

    @Test
    void getAllParents_shouldReturnList() {
        when(parentRepository.findAll()).thenReturn(java.util.List.of(new Parent()));
        var list = parentService.getAllParents();
        assertFalse(list.isEmpty());
        verify(parentRepository).findAll();
    }

    @Test
    void getParentById_shouldReturnParent() {
        Parent p = new Parent();
        when(parentRepository.findById(1L)).thenReturn(Optional.of(p));
        Parent result = parentService.getParentById(1L);
        assertNotNull(result);
    }

    @Test
    void getParentById_shouldThrowWhenMissing() {
        when(parentRepository.findById(10L)).thenReturn(Optional.empty());
        RuntimeException exception = assertThrows(RuntimeException.class, () -> parentService.getParentById(10L));
        assertTrue(exception.getMessage().contains("Parent not found"));
    }

    @Test
    void getParentByEmail_shouldReturnParent() {
        Parent p = new Parent();
        when(parentRepository.findByEmail("test@test.com")).thenReturn(Optional.of(p));
        Parent result = parentService.getParentByEmail("test@test.com");
        assertNotNull(result);
    }

    @Test
    void getParentByEmail_shouldThrowWhenMissing() {
        when(parentRepository.findByEmail("x")).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> parentService.getParentByEmail("x"));
    }

    @Test
    void updateParent_shouldSaveParent() {
        Parent p = new Parent();
        when(parentRepository.save(any())).thenReturn(p);
        Parent result = parentService.updateParent(p);
        assertNotNull(result);
    }

    @Test
    void deleteParent_shouldCallDelete() {
        doNothing().when(parentRepository).deleteById(1L);
        parentService.deleteParent(1L);
        verify(parentRepository).deleteById(1L);
    }

    @Test
    void getStudentsOfParent_shouldReturnStudents() {
        Parent p = new Parent();
        p.setStudents(java.util.List.of(new Student()));
        when(parentRepository.findById(1L)).thenReturn(Optional.of(p));
        var result = parentService.getStudentsOfParent(1L);
        assertFalse(result.isEmpty());
    }

    @Test
    void getStudentsOfParent_shouldThrowWhenMissing() {
        when(parentRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> parentService.getStudentsOfParent(1L));
    }

    @Test
    void linkStudentToParent_shouldAssociateStudentAndParent() {
        Parent parent = new Parent();
        parent.setId(1L);
        Student student = new Student();
        student.setId(2L);

        when(parentRepository.findById(1L)).thenReturn(Optional.of(parent));
        when(studentRepository.findById(2L)).thenReturn(Optional.of(student));
        when(studentRepository.save(any(Student.class))).thenAnswer(invocation -> invocation.getArgument(0));

        parentService.linkStudentToParent(1L, 2L);

        assertSame(parent, student.getParent());
        verify(studentRepository).save(student);
    }

    @Test
    void linkStudentToParent_shouldThrowWhenParentMissing() {
        when(parentRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> parentService.linkStudentToParent(1L, 2L));
    }

    @Test
    void linkStudentToParent_shouldThrowWhenStudentMissing() {
        when(parentRepository.findById(1L)).thenReturn(Optional.of(new Parent()));
        when(studentRepository.findById(2L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> parentService.linkStudentToParent(1L, 2L));
    }

    @Test
    void findByStudentId_shouldReturnProfile() {
        HealthProfile hp = new HealthProfile();
        when(healthProfileRepository.findByStudent_Id(3L)).thenReturn(Optional.of(hp));
        HealthProfile result = parentService.findByStudentId(3L);
        assertNotNull(result);
    }

    @Test
    void findByStudentId_shouldThrowWhenProfileMissing() {
        when(healthProfileRepository.findByStudent_Id(3L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> parentService.findByStudentId(3L));

        assertTrue(exception.getMessage().contains("Health profile not found"));
    }
}
