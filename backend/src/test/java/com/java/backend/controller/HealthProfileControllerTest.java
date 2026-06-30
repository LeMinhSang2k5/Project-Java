package com.java.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.java.backend.entity.HealthProfile;
import com.java.backend.entity.Student;
import com.java.backend.service.HealthProfileService;
import com.java.backend.service.StudentService;
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
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class HealthProfileControllerTest {

    private MockMvc mockMvc;

    @Mock
    private HealthProfileService healthProfileService;

    @Mock
    private StudentService studentService;

    @InjectMocks
    private HealthProfileController healthProfileController;

    private ObjectMapper objectMapper = new ObjectMapper();
    private HealthProfile healthProfile;
    private Student student;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(healthProfileController).build();
        student = new Student();
        student.setId(1L);

        healthProfile = new HealthProfile();
        healthProfile.setId(1L);
        healthProfile.setStudent(student);
    }

    @Test
    void createHealthProfile_Success() throws Exception {
        when(healthProfileService.saveHealthProfile(any(HealthProfile.class))).thenReturn(healthProfile);
        mockMvc.perform(post("/api/health-profiles")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(healthProfile)))
                .andExpect(status().isOk());
    }

    @Test
    void getAllHealthProfiles_Success() throws Exception {
        when(healthProfileService.getAllHealthProfiles()).thenReturn(Arrays.asList(healthProfile));
        mockMvc.perform(get("/api/health-profiles"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void getByStudentId_Success() throws Exception {
        when(healthProfileService.findByStudentIdOrThrow(1L)).thenReturn(healthProfile);
        mockMvc.perform(get("/api/health-profiles/student/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void getByStudentId_NotFound() throws Exception {
        when(healthProfileService.findByStudentIdOrThrow(1L)).thenThrow(new com.java.backend.exception.HealthProfileNotFoundException(1L, true));
        mockMvc.perform(get("/api/health-profiles/student/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getStudentIdByParentId_Success() throws Exception {
        when(studentService.getStudentsByParent(1L)).thenReturn(Arrays.asList(student));
        mockMvc.perform(get("/api/health-profiles/by-parent/1/student"))
                .andExpect(status().isOk())
                .andExpect(content().string("1"));
    }

    @Test
    void getStudentIdByParentId_EmptyList() throws Exception {
        when(studentService.getStudentsByParent(1L)).thenReturn(Collections.emptyList());
        mockMvc.perform(get("/api/health-profiles/by-parent/1/student"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getStudentIdByParentId_NullList() throws Exception {
        when(studentService.getStudentsByParent(1L)).thenReturn(null);
        mockMvc.perform(get("/api/health-profiles/by-parent/1/student"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getStudentIdByParentId_Exception() throws Exception {
        when(studentService.getStudentsByParent(1L)).thenThrow(new RuntimeException("DB Error"));
        mockMvc.perform(get("/api/health-profiles/by-parent/1/student"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateHealthProfile_Success() throws Exception {
        when(healthProfileService.updateHealthProfile(any(HealthProfile.class))).thenReturn(healthProfile);
        mockMvc.perform(put("/api/health-profiles/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(healthProfile)))
                .andExpect(status().isOk());
    }

    @Test
    void updateHealthProfile_Exception() throws Exception {
        when(healthProfileService.updateHealthProfile(any(HealthProfile.class))).thenThrow(new RuntimeException("DB Error"));
        mockMvc.perform(put("/api/health-profiles/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(healthProfile)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("DB Error")));
    }

    @Test
    void deleteHealthProfile_Success() throws Exception {
        doNothing().when(healthProfileService).deleteHealthProfile(1L);
        mockMvc.perform(delete("/api/health-profiles/1"))
                .andExpect(status().isOk());
    }
}
