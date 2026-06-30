package com.java.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.java.backend.entity.Medical;
import com.java.backend.service.MedicalService;
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
import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class MedicalControllerTest {

    private MockMvc mockMvc;

    @Mock
    private MedicalService medicalService;

    @InjectMocks
    private MedicalController medicalController;

    private ObjectMapper objectMapper = new ObjectMapper();
    private Medical medical;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(medicalController).build();

        medical = new Medical();
        medical.setId(1L);
        medical.setMedicalName("Panadol");
    }

    @Test
    void createMedicalRequest_Success() throws Exception {
        when(medicalService.createMedicalRequest(1L, 1L, "Panadol", "2 pills", "Morning")).thenReturn(medical);

        Map<String, Object> request = new HashMap<>();
        request.put("studentId", 1L);
        request.put("parentId", 1L);
        request.put("medicalName", "Panadol");
        request.put("dosage", "2 pills");
        request.put("note", "Morning");

        mockMvc.perform(post("/api/medical")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.medicalName").value("Panadol"));
    }

    @Test
    void createMedicalRequest_BadRequest() throws Exception {
        when(medicalService.createMedicalRequest(any(), any(), any(), any(), any())).thenThrow(new RuntimeException("DB Error"));

        Map<String, Object> request = new HashMap<>();
        request.put("studentId", 1L);
        request.put("parentId", 1L);

        mockMvc.perform(post("/api/medical")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    void getAllMedicalRequests_Success() throws Exception {
        when(medicalService.getAllMedicalRequests()).thenReturn(Arrays.asList(medical));
        mockMvc.perform(get("/api/medical"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void getMedicalRequestById_Found() throws Exception {
        when(medicalService.getMedicalRequestById(1L)).thenReturn(medical);
        mockMvc.perform(get("/api/medical/1"))
                .andExpect(status().isOk());
    }

    @Test
    void getMedicalRequestById_NotFound() throws Exception {
        when(medicalService.getMedicalRequestById(1L)).thenThrow(new RuntimeException("Not found"));
        mockMvc.perform(get("/api/medical/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getMedicalRequestsByStudentId_Success() throws Exception {
        when(medicalService.getMedicalRequestsByStudentId(1L)).thenReturn(Arrays.asList(medical));
        mockMvc.perform(get("/api/medical/student/1"))
                .andExpect(status().isOk());
    }

    @Test
    void getMedicalRequestsByParentId_Success() throws Exception {
        when(medicalService.getMedicalRequestsByParentId(1L)).thenReturn(Arrays.asList(medical));
        mockMvc.perform(get("/api/medical/parent/1"))
                .andExpect(status().isOk());
    }

    @Test
    void getMedicalRequestsByStatus_Success() throws Exception {
        when(medicalService.getMedicalRequestsByStatus("PENDING")).thenReturn(Arrays.asList(medical));
        mockMvc.perform(get("/api/medical/status/PENDING"))
                .andExpect(status().isOk());
    }

    @Test
    void updateMedicalRequestStatus_Success() throws Exception {
        when(medicalService.updateMedicalRequestStatus(eq(1L), eq("APPROVED"), eq("Nurse"))).thenReturn(medical);
        
        Map<String, String> req = new HashMap<>();
        req.put("status", "APPROVED");
        req.put("approvedBy", "Nurse");
        
        mockMvc.perform(put("/api/medical/1/status")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk());
    }

    @Test
    void updateMedicalRequestStatus_BadRequest() throws Exception {
        when(medicalService.updateMedicalRequestStatus(anyLong(), any(), any())).thenThrow(new RuntimeException("Error"));
        
        mockMvc.perform(put("/api/medical/1/status")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateMedicalRequest_Success() throws Exception {
        when(medicalService.updateMedicalRequest(eq(1L), any(), any(), any())).thenReturn(medical);
        
        Map<String, String> req = new HashMap<>();
        req.put("medicalName", "Panadol");
        req.put("dosage", "1");
        req.put("note", "n");
        
        mockMvc.perform(put("/api/medical/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk());
    }

    @Test
    void updateMedicalRequest_BadRequest() throws Exception {
        when(medicalService.updateMedicalRequest(anyLong(), any(), any(), any())).thenThrow(new RuntimeException("Error"));
        mockMvc.perform(put("/api/medical/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deleteMedicalRequest_Success() throws Exception {
        doNothing().when(medicalService).deleteMedicalRequest(1L);
        mockMvc.perform(delete("/api/medical/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    void deleteMedicalRequest_BadRequest() throws Exception {
        doThrow(new RuntimeException("Error")).when(medicalService).deleteMedicalRequest(1L);
        mockMvc.perform(delete("/api/medical/1"))
                .andExpect(status().isBadRequest());
    }
}
