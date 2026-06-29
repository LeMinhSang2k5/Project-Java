package com.java.backend.controller;

import com.java.backend.entity.VaccinationSchedule;
import com.java.backend.enums.ConsentStatus;
import com.java.backend.service.VaccinationScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/vaccination-schedules")
@CrossOrigin(origins = "http://localhost:3000")
public class VaccinationScheduleController {

    @Autowired
    private VaccinationScheduleService service;

    @GetMapping
    public List<VaccinationSchedule> getAllSchedules() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<VaccinationSchedule> getSchedule(@PathVariable Long id) {
        return ResponseEntity.ok(service.getByIdOrThrow(id));
    }

    @PostMapping
    public ResponseEntity<VaccinationSchedule> createSchedule(@RequestBody VaccinationSchedule schedule) {
        if (schedule.getParentConsent() == null) {
            schedule.setParentConsent(ConsentStatus.PENDING);
        }
        VaccinationSchedule saved = service.save(schedule);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<VaccinationSchedule> updateSchedule(
            @PathVariable Long id,
            @RequestBody VaccinationSchedule schedule) {
        service.getByIdOrThrow(id);
        schedule.setId(id);
        return ResponseEntity.ok(service.save(schedule));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteSchedule(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok(Map.of("message", "Xóa lịch tiêm chủng thành công"));
    }

    @GetMapping("/student/{studentId}")
    public List<VaccinationSchedule> getByStudentId(@PathVariable Long studentId) {
        return service.getByStudentId(studentId);
    }

    @GetMapping("/pending-parent-consent")
    public List<VaccinationSchedule> getPendingParentConsent() {
        return service.getPendingParentConsent();
    }

    @GetMapping("/pending-student-confirmation")
    public List<VaccinationSchedule> getPendingStudentConfirmation() {
        return service.getPendingStudentConfirmation();
    }

    @GetMapping("/parent-consent/{consent}")
    public List<VaccinationSchedule> getByParentConsent(@PathVariable ConsentStatus consent) {
        return service.getByParentConsent(consent);
    }

    @GetMapping("/not-vaccinated")
    public List<VaccinationSchedule> getNotVaccinated() {
        return service.getNotVaccinated();
    }

    @GetMapping("/vaccinated")
    public List<VaccinationSchedule> getVaccinated() {
        return service.getVaccinated();
    }

    @PutMapping("/{id}/parent-consent")
    public ResponseEntity<VaccinationSchedule> updateParentConsent(
            @PathVariable Long id,
            @RequestBody Map<String, String> request) {
        ConsentStatus consent = service.parseParentConsent(request.get("consent"));
        return ResponseEntity.ok(service.updateParentConsent(id, consent));
    }

    @PutMapping("/{id}/student-confirmation")
    public ResponseEntity<VaccinationSchedule> updateStudentConfirmation(
            @PathVariable Long id,
            @RequestBody Map<String, Boolean> request) {
        Boolean confirmed = request.get("confirmed");
        service.validateBooleanField(confirmed, "Giá trị xác nhận");
        return ResponseEntity.ok(service.updateStudentConfirmation(id, confirmed));
    }

    @PutMapping("/{id}/vaccination-status")
    public ResponseEntity<VaccinationSchedule> updateVaccinationStatus(
            @PathVariable Long id,
            @RequestBody Map<String, Boolean> request) {
        Boolean isVaccinated = request.get("isVaccinated");
        service.validateBooleanField(isVaccinated, "Trạng thái tiêm chủng");
        return ResponseEntity.ok(service.updateVaccinationStatus(id, isVaccinated));
    }

    @GetMapping("/date-range")
    public List<VaccinationSchedule> getByDateRange(
            @RequestParam String startDate,
            @RequestParam String endDate) {
        LocalDateTime start = LocalDateTime.parse(startDate);
        LocalDateTime end = LocalDateTime.parse(endDate);
        return service.getByDateRange(start, end);
    }

    @PostMapping("/bulk")
    public ResponseEntity<?> createBulkSchedules(@RequestBody Map<String, Object> payload) {
        Object studentIdsObj = payload.get("studentIds");
        if (!(studentIdsObj instanceof List<?> studentIds) || studentIds.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "Danh sách studentIds không được để trống"));
        }

        String vaccineName = (String) payload.get("vaccineName");
        String scheduledDateTime = (String) payload.get("scheduledDateTime");
        if (vaccineName == null || vaccineName.trim().isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "Tên vaccine không được để trống"));
        }
        if (scheduledDateTime == null || scheduledDateTime.trim().isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "Ngày giờ tiêm không được để trống"));
        }

        String notes = payload.get("notes") != null ? (String) payload.get("notes") : null;
        String location = payload.get("location") != null ? (String) payload.get("location") : null;
        LocalDateTime parsedDateTime = LocalDateTime.parse(scheduledDateTime);

        List<VaccinationSchedule> createdSchedules = new ArrayList<>();
        for (Object studentIdObj : studentIds) {
            Long studentId = Long.valueOf(studentIdObj.toString());
            createdSchedules.add(service.createFromStudent(
                    studentId, vaccineName, parsedDateTime, notes, location));
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(createdSchedules);
    }

    @GetMapping("/pending-parent-consent/{parentId}")
    public List<VaccinationSchedule> getPendingParentConsentByParentId(@PathVariable Long parentId) {
        return service.getPendingParentConsentByParentId(parentId);
    }
}
