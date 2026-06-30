package com.java.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.java.backend.entity.Parent;
import com.java.backend.entity.Student;
import com.java.backend.enums.Role;
import com.java.backend.repository.MedicalCheckupNotificationRepository;
import com.java.backend.repository.VaccinationScheduleRepository;
import com.java.backend.service.ParentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class ParentControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ParentService parentService;

    @Mock
    private MedicalCheckupNotificationRepository medicalCheckupNotificationRepository;

    @Mock
    private VaccinationScheduleRepository vaccinationScheduleRepository;

    @InjectMocks
    private ParentController parentController;

    private ObjectMapper objectMapper = new ObjectMapper();
    private Parent parent;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(parentController).build();
        parent = new Parent();
        parent.setId(1L);
        parent.setRole(Role.PARENT);
        parent.setPhoneNumber("0123456789");
    }

    @Test
    void createParent_Success() throws Exception {
        when(parentService.saveParent(any(Parent.class))).thenReturn(parent);
        
        mockMvc.perform(post("/api/parent")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(parent)))
                .andExpect(status().isOk());
    }

    @Test
    void createParent_NullRole() throws Exception {
        parent.setRole(null);
        mockMvc.perform(post("/api/parent")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(parent)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Trường role là bắt buộc và phải là PARENT"));
    }

    @Test
    void createParent_WrongRole() throws Exception {
        parent.setRole(Role.ADMIN);
        mockMvc.perform(post("/api/parent")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(parent)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Khi tạo phụ huynh, role chỉ được phép là PARENT"));
    }

    @Test
    void createParent_EmptyPhone() throws Exception {
        parent.setPhoneNumber("");
        mockMvc.perform(post("/api/parent")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(parent)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Số điện thoại không được để trống"));
    }

    @Test
    void createParent_InvalidPhone() throws Exception {
        parent.setPhoneNumber("123");
        mockMvc.perform(post("/api/parent")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(parent)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    void getStudentsOfParent_WithStudents() throws Exception {
        when(parentService.getStudentsOfParent(1L)).thenReturn(Arrays.asList(new Student()));
        mockMvc.perform(get("/api/parent/1/students"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void getStudentsOfParent_NoStudents() throws Exception {
        when(parentService.getStudentsOfParent(1L)).thenReturn(Collections.emptyList());
        mockMvc.perform(get("/api/parent/1/students"))
                .andExpect(status().isOk());
    }

    @Test
    void getStudentsOfParent_Exception() throws Exception {
        when(parentService.getStudentsOfParent(1L)).thenThrow(new RuntimeException("Error"));
        mockMvc.perform(get("/api/parent/1/students"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getParent_Found() throws Exception {
        when(parentService.getParentById(1L)).thenReturn(parent);
        mockMvc.perform(get("/api/parent/1"))
                .andExpect(status().isOk());
    }

    @Test
    void getParent_NotFound() throws Exception {
        when(parentService.getParentById(1L)).thenThrow(new RuntimeException("Not found"));
        mockMvc.perform(get("/api/parent/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void linkStudentToParent_Success() throws Exception {
        doNothing().when(parentService).linkStudentToParent(1L, 1L);
        mockMvc.perform(put("/api/parent/link-student?parentId=1&studentId=1"))
                .andExpect(status().isOk());
    }

    @Test
    void linkStudentToParent_Exception() throws Exception {
        doThrow(new RuntimeException("Error")).when(parentService).linkStudentToParent(1L, 1L);
        mockMvc.perform(put("/api/parent/link-student?parentId=1&studentId=1"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deleteParent_Success() throws Exception {
        doNothing().when(parentService).deleteParent(1L);
        mockMvc.perform(delete("/api/parent/1"))
                .andExpect(status().isOk());
    }

    @Test
    void deleteParent_Exception() throws Exception {
        doThrow(new RuntimeException("Error")).when(parentService).deleteParent(1L);
        mockMvc.perform(delete("/api/parent/1"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void confirmForm_MedicalExists() throws Exception {
        when(medicalCheckupNotificationRepository.existsById(1L)).thenReturn(true);
        mockMvc.perform(post("/api/parent/confirm/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    void confirmForm_VaccinationExists() throws Exception {
        when(medicalCheckupNotificationRepository.existsById(1L)).thenReturn(false);
        when(vaccinationScheduleRepository.existsById(1L)).thenReturn(true);
        mockMvc.perform(post("/api/parent/confirm/1"))
                .andExpect(status().isOk());
    }

    @Test
    void confirmForm_NotFound() throws Exception {
        when(medicalCheckupNotificationRepository.existsById(1L)).thenReturn(false);
        when(vaccinationScheduleRepository.existsById(1L)).thenReturn(false);
        mockMvc.perform(post("/api/parent/confirm/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void createParent_NullPhone() throws Exception {
        parent.setPhoneNumber(null);
        mockMvc.perform(post("/api/parent")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(parent)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Số điện thoại không được để trống"));
    }

    @Test
    void confirmForm_Exception() throws Exception {
        when(medicalCheckupNotificationRepository.existsById(1L)).thenThrow(new RuntimeException("DB Error"));
        mockMvc.perform(post("/api/parent/confirm/1"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Xác nhận thất bại"));
    }
}
