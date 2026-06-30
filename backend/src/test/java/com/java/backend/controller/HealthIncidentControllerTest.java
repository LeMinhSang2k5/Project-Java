package com.java.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.java.backend.entity.HealthIncident;
import com.java.backend.service.HealthIncidentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class HealthIncidentControllerTest {

    private MockMvc mockMvc;

    @Mock
    private HealthIncidentService healthIncidentService;

    @InjectMocks
    private HealthIncidentController healthIncidentController;

    private ObjectMapper objectMapper;
    private HealthIncident incident;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(healthIncidentController).build();
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        incident = new HealthIncident();
        incident.setId(1L);
        incident.setIncidentTime(LocalDateTime.of(2023, 10, 10, 10, 0));
        incident.setStatus(HealthIncident.IncidentStatus.REPORTED);
        incident.setIncidentType("OTHER");
    }

    @Test
    void createHealthIncident_Success() throws Exception {
        when(healthIncidentService.createHealthIncident(any(HealthIncident.class))).thenReturn(incident);
        mockMvc.perform(post("/api/health-incidents")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(incident)))
                .andExpect(status().isCreated());
    }

    @Test
    void getHealthIncidentById_Success() throws Exception {
        when(healthIncidentService.getHealthIncidentById(1L)).thenReturn(incident);
        mockMvc.perform(get("/api/health-incidents/1"))
                .andExpect(status().isOk());
    }

    @Test
    void getAllHealthIncidents_Success() throws Exception {
        when(healthIncidentService.getAllHealthIncidents()).thenReturn(Arrays.asList(incident));
        mockMvc.perform(get("/api/health-incidents"))
                .andExpect(status().isOk());
    }

    @Test
    void updateHealthIncident_Success() throws Exception {
        when(healthIncidentService.updateHealthIncident(eq(1L), any(HealthIncident.class))).thenReturn(incident);
        mockMvc.perform(put("/api/health-incidents/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(incident)))
                .andExpect(status().isOk());
    }

    @Test
    void deleteHealthIncident_Success() throws Exception {
        doNothing().when(healthIncidentService).deleteHealthIncident(1L);
        mockMvc.perform(delete("/api/health-incidents/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void getHealthIncidentsByStudentId_Success() throws Exception {
        when(healthIncidentService.getHealthIncidentsByStudentId(1L)).thenReturn(Arrays.asList(incident));
        mockMvc.perform(get("/api/health-incidents/student/1"))
                .andExpect(status().isOk());
    }

    @Test
    void getHealthIncidentsByReportedById_Success() throws Exception {
        when(healthIncidentService.getHealthIncidentsByReportedById(1L)).thenReturn(Arrays.asList(incident));
        mockMvc.perform(get("/api/health-incidents/reported-by/1"))
                .andExpect(status().isOk());
    }

    @Test
    void getHealthIncidentsByIncidentTime_Success() throws Exception {
        when(healthIncidentService.getHealthIncidentsByIncidentTime(any())).thenReturn(Arrays.asList(incident));
        mockMvc.perform(get("/api/health-incidents/incident-time/2023-10-10T10:00:00"))
                .andExpect(status().isOk());
    }

    @Test
    void getHealthIncidentsByStatus_Success() throws Exception {
        when(healthIncidentService.getHealthIncidentsByStatus(HealthIncident.IncidentStatus.REPORTED)).thenReturn(Arrays.asList(incident));
        mockMvc.perform(get("/api/health-incidents/status/REPORTED"))
                .andExpect(status().isOk());
    }

    @Test
    void getHealthIncidentsByIncidentType_Success() throws Exception {
        when(healthIncidentService.getHealthIncidentsByIncidentType(HealthIncident.IncidentType.OTHER)).thenReturn(Arrays.asList(incident));
        mockMvc.perform(get("/api/health-incidents/incident-type/OTHER"))
                .andExpect(status().isOk());
    }

    @Test
    void getHealthIncidentsByIncidentTimeAndStatus_Success() throws Exception {
        when(healthIncidentService.getHealthIncidentsByIncidentTimeAndStatus(any(), any())).thenReturn(Arrays.asList(incident));
        mockMvc.perform(get("/api/health-incidents/incident-time-and-status/2023-10-10T10:00:00/REPORTED"))
                .andExpect(status().isOk());
    }

    @Test
    void getHealthIncidentsByStudentIdAndIncidentTime_Success() throws Exception {
        when(healthIncidentService.getHealthIncidentsByStudentIdAndIncidentTime(anyLong(), any())).thenReturn(Arrays.asList(incident));
        mockMvc.perform(get("/api/health-incidents/student-and-incident-time/1/2023-10-10T10:00:00"))
                .andExpect(status().isOk());
    }
}
