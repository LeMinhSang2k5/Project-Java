package com.java.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.java.backend.entity.MedicalCheckupNotification;
import com.java.backend.entity.MedicalCheckupResult;
import com.java.backend.entity.Parent;
import com.java.backend.entity.Student;
import com.java.backend.service.MedicalCheckupService;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class MedicalCheckupControllerTest {

    private MockMvc mockMvc;

    @Mock
    private MedicalCheckupService service;

    @Mock
    private StudentService studentService;

    @InjectMocks
    private MedicalCheckupController controller;

    private ObjectMapper objectMapper;
    private MedicalCheckupNotification notification;
    private MedicalCheckupResult result;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        notification = new MedicalCheckupNotification();
        notification.setStudentId(1L);
        notification.setParentId(1L);

        result = new MedicalCheckupResult();
        result.setId(1L);
        result.setStudentId(1L);
    }

    @Test
    void createNotification_Success() throws Exception {
        when(service.saveNotification(any())).thenReturn(notification);
        mockMvc.perform(post("/api/medical-checkup/notify")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(notification)))
                .andExpect(status().isOk());
    }

    @Test
    void getNotificationsByParent_Success() throws Exception {
        when(service.getNotificationsByParent(1L)).thenReturn(Arrays.asList(notification));
        mockMvc.perform(get("/api/medical-checkup/notifications?parentId=1"))
                .andExpect(status().isOk());
    }

    @Test
    void getNotificationsByStudent_Success() throws Exception {
        when(service.getNotificationsByStudent(1L)).thenReturn(Arrays.asList(notification));
        mockMvc.perform(get("/api/medical-checkup/notifications?studentId=1"))
                .andExpect(status().isOk());
    }

    @Test
    void getNotificationsByStatus_Success() throws Exception {
        when(service.getNotificationsByStatus("PENDING")).thenReturn(Arrays.asList(notification));
        mockMvc.perform(get("/api/medical-checkup/notifications?status=PENDING"))
                .andExpect(status().isOk());
    }

    @Test
    void getNotifications_All() throws Exception {
        when(service.getAllNotifications()).thenReturn(Arrays.asList(notification));
        mockMvc.perform(get("/api/medical-checkup/notifications"))
                .andExpect(status().isOk());
    }

    @Test
    void updateNotificationConsent_Found() throws Exception {
        when(service.getNotification(1L)).thenReturn(notification);
        when(service.saveNotification(any())).thenReturn(notification);

        mockMvc.perform(put("/api/medical-checkup/notification/1/consent")
                .contentType(MediaType.APPLICATION_JSON)
                .content("\"APPROVED\""))
                .andExpect(status().isOk());
    }

    @Test
    void updateNotificationConsent_NotFound() throws Exception {
        when(service.getNotification(1L)).thenReturn(null);

        mockMvc.perform(put("/api/medical-checkup/notification/1/consent")
                .contentType(MediaType.APPLICATION_JSON)
                .content("\"APPROVED\""))
                .andExpect(status().isOk())
                .andExpect(content().string(""));
    }

    @Test
    void createBulkNotifications_Success() throws Exception {
        Student student = new Student();
        Parent parent = new Parent();
        parent.setId(1L);
        student.setParent(parent);
        
        when(studentService.getStudentById(1L)).thenReturn(student);
        when(service.saveNotification(any())).thenReturn(notification);

        Map<String, Object> payload = new HashMap<>();
        payload.put("studentIds", Arrays.asList(1));
        payload.put("content", "Checkup");
        payload.put("scheduledDate", "2023-10-10T10:00:00");

        mockMvc.perform(post("/api/medical-checkup/notify/bulk")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isOk());
    }

    @Test
    void createBulkNotifications_StudentHasNoParent() throws Exception {
        Student student = new Student();
        
        when(studentService.getStudentById(1L)).thenReturn(student);

        Map<String, Object> payload = new HashMap<>();
        payload.put("studentIds", Arrays.asList(1));
        payload.put("content", "Checkup");
        payload.put("scheduledDate", "2023-10-10T10:00:00");

        mockMvc.perform(post("/api/medical-checkup/notify/bulk")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void createResult_Success() throws Exception {
        when(service.saveResult(any())).thenReturn(result);
        mockMvc.perform(post("/api/medical-checkup/result")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(result)))
                .andExpect(status().isOk());
    }

    @Test
    void getResultsByParent_Success() throws Exception {
        when(service.getResultsByParent(1L)).thenReturn(Arrays.asList(result));
        when(studentService.getStudentById(1L)).thenReturn(new Student());
        mockMvc.perform(get("/api/medical-checkup/results?parentId=1"))
                .andExpect(status().isOk());
    }

    @Test
    void getResultsByStudent_Success() throws Exception {
        when(service.getResultsByStudent(1L)).thenReturn(Arrays.asList(result));
        when(studentService.getStudentById(1L)).thenReturn(null);
        mockMvc.perform(get("/api/medical-checkup/results?studentId=1"))
                .andExpect(status().isOk());
    }
    
    @Test
    void getResults_Empty() throws Exception {
        mockMvc.perform(get("/api/medical-checkup/results"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void updateResult_Found() throws Exception {
        when(service.getResult(1L)).thenReturn(result);
        when(service.saveResult(any())).thenReturn(result);

        mockMvc.perform(put("/api/medical-checkup/result/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(result)))
                .andExpect(status().isOk());
    }

    @Test
    void updateResult_NotFound() throws Exception {
        when(service.getResult(1L)).thenReturn(null);

        mockMvc.perform(put("/api/medical-checkup/result/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(result)))
                .andExpect(status().isOk())
                .andExpect(content().string(""));
    }

    @Test
    void studentConfirmResult_Found() throws Exception {
        when(service.getResult(1L)).thenReturn(result);
        when(service.saveResult(any())).thenReturn(result);

        mockMvc.perform(put("/api/medical-checkup/result/1/student-confirm"))
                .andExpect(status().isOk());
    }

    @Test
    void studentConfirmResult_NotFound() throws Exception {
        when(service.getResult(1L)).thenReturn(null);

        mockMvc.perform(put("/api/medical-checkup/result/1/student-confirm"))
                .andExpect(status().isOk())
                .andExpect(content().string(""));
    }
}
