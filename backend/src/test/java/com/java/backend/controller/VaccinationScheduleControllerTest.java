package com.java.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.java.backend.entity.Student;
import com.java.backend.entity.VaccinationSchedule;
import com.java.backend.enums.ConsentStatus;
import com.java.backend.service.StudentService;
import com.java.backend.service.VaccinationScheduleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class VaccinationScheduleControllerTest {

    private MockMvc mockMvc;

    @Mock
    private VaccinationScheduleService service;

    @Mock
    private StudentService studentService;

    @InjectMocks
    private VaccinationScheduleController controller;

    private ObjectMapper objectMapper;
    private VaccinationSchedule schedule;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        schedule = new VaccinationSchedule();
        schedule.setId(1L);
        schedule.setStudentId(10L);
        schedule.setVaccineName("COVID-19");
    }

    @Test
    void getAllSchedules_Success() throws Exception {
        when(service.getAll()).thenReturn(Arrays.asList(schedule));

        mockMvc.perform(get("/api/vaccination-schedules"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].vaccineName", is("COVID-19")));
    }

    @Test
    void getSchedule_Success() throws Exception {
        when(service.getById(1L)).thenReturn(schedule);

        mockMvc.perform(get("/api/vaccination-schedules/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.vaccineName", is("COVID-19")));
    }

    @Test
    void getSchedule_NotFound() throws Exception {
        when(service.getById(1L)).thenReturn(null);

        mockMvc.perform(get("/api/vaccination-schedules/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void createSchedule_Success() throws Exception {
        when(service.save(any(VaccinationSchedule.class))).thenReturn(schedule);

        mockMvc.perform(post("/api/vaccination-schedules")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(schedule)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.vaccineName", is("COVID-19")));
    }

    @Test
    void updateSchedule_Success() throws Exception {
        when(service.getById(1L)).thenReturn(schedule);
        when(service.save(any(VaccinationSchedule.class))).thenReturn(schedule);

        mockMvc.perform(put("/api/vaccination-schedules/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(schedule)))
                .andExpect(status().isOk());
    }

    @Test
    void updateSchedule_NotFound() throws Exception {
        when(service.getById(1L)).thenReturn(null);

        mockMvc.perform(put("/api/vaccination-schedules/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(schedule)))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteSchedule_Success() throws Exception {
        when(service.getById(1L)).thenReturn(schedule);
        doNothing().when(service).delete(1L);

        mockMvc.perform(delete("/api/vaccination-schedules/1"))
                .andExpect(status().isOk());
    }

    @Test
    void deleteSchedule_NotFound() throws Exception {
        when(service.getById(1L)).thenReturn(null);

        mockMvc.perform(delete("/api/vaccination-schedules/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getByStudentId_Success() throws Exception {
        when(service.getByStudentId(10L)).thenReturn(Arrays.asList(schedule));

        mockMvc.perform(get("/api/vaccination-schedules/student/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void getPendingParentConsent_Success() throws Exception {
        when(service.getPendingParentConsent()).thenReturn(Arrays.asList(schedule));

        mockMvc.perform(get("/api/vaccination-schedules/pending-parent-consent"))
                .andExpect(status().isOk());
    }

    @Test
    void getPendingStudentConfirmation_Success() throws Exception {
        when(service.getPendingStudentConfirmation()).thenReturn(Arrays.asList(schedule));

        mockMvc.perform(get("/api/vaccination-schedules/pending-student-confirmation"))
                .andExpect(status().isOk());
    }

    @Test
    void getByParentConsent_Success() throws Exception {
        when(service.getByParentConsent(ConsentStatus.APPROVED)).thenReturn(Arrays.asList(schedule));

        mockMvc.perform(get("/api/vaccination-schedules/parent-consent/APPROVED"))
                .andExpect(status().isOk());
    }

    @Test
    void getNotVaccinated_Success() throws Exception {
        when(service.getNotVaccinated()).thenReturn(Arrays.asList(schedule));

        mockMvc.perform(get("/api/vaccination-schedules/not-vaccinated"))
                .andExpect(status().isOk());
    }

    @Test
    void getVaccinated_Success() throws Exception {
        when(service.getVaccinated()).thenReturn(Arrays.asList(schedule));

        mockMvc.perform(get("/api/vaccination-schedules/vaccinated"))
                .andExpect(status().isOk());
    }

    @Test
    void updateParentConsent_Success() throws Exception {
        when(service.updateParentConsent(eq(1L), eq(ConsentStatus.APPROVED))).thenReturn(schedule);

        Map<String, String> request = new HashMap<>();
        request.put("consent", "APPROVED");

        mockMvc.perform(put("/api/vaccination-schedules/1/parent-consent")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    void updateParentConsent_NotFound() throws Exception {
        when(service.updateParentConsent(eq(1L), eq(ConsentStatus.APPROVED))).thenReturn(null);

        Map<String, String> request = new HashMap<>();
        request.put("consent", "APPROVED");

        mockMvc.perform(put("/api/vaccination-schedules/1/parent-consent")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateParentConsent_BadRequest() throws Exception {
        Map<String, String> request = new HashMap<>();
        request.put("consent", "INVALID_ENUM"); // This will cause IllegalArgumentException

        mockMvc.perform(put("/api/vaccination-schedules/1/parent-consent")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateStudentConfirmation_Success() throws Exception {
        when(service.updateStudentConfirmation(1L, true)).thenReturn(schedule);

        Map<String, Boolean> request = new HashMap<>();
        request.put("confirmed", true);

        mockMvc.perform(put("/api/vaccination-schedules/1/student-confirmation")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    void updateStudentConfirmation_NotFound() throws Exception {
        when(service.updateStudentConfirmation(1L, true)).thenReturn(null);

        Map<String, Boolean> request = new HashMap<>();
        request.put("confirmed", true);

        mockMvc.perform(put("/api/vaccination-schedules/1/student-confirmation")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateVaccinationStatus_Success() throws Exception {
        when(service.updateVaccinationStatus(1L, true)).thenReturn(schedule);

        Map<String, Boolean> request = new HashMap<>();
        request.put("isVaccinated", true);

        mockMvc.perform(put("/api/vaccination-schedules/1/vaccination-status")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    void updateVaccinationStatus_NotFound() throws Exception {
        when(service.updateVaccinationStatus(1L, true)).thenReturn(null);

        Map<String, Boolean> request = new HashMap<>();
        request.put("isVaccinated", true);

        mockMvc.perform(put("/api/vaccination-schedules/1/vaccination-status")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    @Test
    void getByDateRange_Success() throws Exception {
        when(service.getByDateRange(any(), any())).thenReturn(Arrays.asList(schedule));

        mockMvc.perform(get("/api/vaccination-schedules/date-range")
                .param("startDate", "2023-01-01T00:00:00")
                .param("endDate", "2023-12-31T23:59:59"))
                .andExpect(status().isOk());
    }

    @Test
    void createBulkSchedules_Success() throws Exception {
        Student mockStudent = new Student();
        mockStudent.setFullName("Nguyen Van A");
        mockStudent.setCode("STU001");
        
        when(studentService.getStudentById(10L)).thenReturn(mockStudent);
        when(service.save(any(VaccinationSchedule.class))).thenReturn(schedule);

        Map<String, Object> payload = new HashMap<>();
        payload.put("studentIds", Arrays.asList(10));
        payload.put("vaccineName", "COVID-19");
        payload.put("scheduledDateTime", "2023-10-10T10:00:00");
        payload.put("notes", "test notes");
        payload.put("location", "Room 1");

        mockMvc.perform(post("/api/vaccination-schedules/bulk")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void createBulkSchedules_MissingParams() throws Exception {
        Map<String, Object> payload = new HashMap<>();
        // Missing required fields
        payload.put("studentIds", Arrays.asList());

        mockMvc.perform(post("/api/vaccination-schedules/bulk")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createBulkSchedules_Exception() throws Exception {
        when(studentService.getStudentById(10L)).thenThrow(new RuntimeException("DB Error"));

        Map<String, Object> payload = new HashMap<>();
        payload.put("studentIds", Arrays.asList(10));
        payload.put("vaccineName", "COVID-19");
        payload.put("scheduledDateTime", "2023-10-10T10:00:00");

        mockMvc.perform(post("/api/vaccination-schedules/bulk")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string(containsString("DB Error")));
    }

    @Test
    void getPendingParentConsentByParentId_Success() throws Exception {
        when(service.getPendingParentConsentByParentId(100L)).thenReturn(Arrays.asList(schedule));

        mockMvc.perform(get("/api/vaccination-schedules/pending-parent-consent/100"))
                .andExpect(status().isOk());
    }
}
