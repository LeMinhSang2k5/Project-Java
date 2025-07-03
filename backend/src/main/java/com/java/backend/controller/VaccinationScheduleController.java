package com.java.backend.controller;

import com.java.backend.entity.VaccinationSchedule;
import com.java.backend.service.VaccinationScheduleService;
import com.java.backend.service.StudentService;
import com.java.backend.enums.ConsentStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/vaccination-schedules")
@CrossOrigin(origins = "http://localhost:3000")
public class VaccinationScheduleController {

    @Autowired
    private VaccinationScheduleService service;

    @Autowired
    private StudentService studentService;

    // ✅ Get all
    @GetMapping
    public List<VaccinationSchedule> getAllSchedules() {
        return service.getAll();
    }

    // ✅ Get by ID
    @GetMapping("/{id}")
    public ResponseEntity<VaccinationSchedule> getSchedule(@PathVariable Long id) {
        VaccinationSchedule schedule = service.getById(id);
        if (schedule != null) {
            return ResponseEntity.ok(schedule);
        }
        return ResponseEntity.notFound().build();
    }

    // ✅ Create
    @PostMapping
    public VaccinationSchedule createSchedule(@RequestBody VaccinationSchedule schedule) {
        return service.save(schedule);
    }

    // ✅ Update
    @PutMapping("/{id}")
    public ResponseEntity<VaccinationSchedule> updateSchedule(@PathVariable Long id,
            @RequestBody VaccinationSchedule schedule) {
        VaccinationSchedule existingSchedule = service.getById(id);
        if (existingSchedule != null) {
            schedule.setId(id);
            return ResponseEntity.ok(service.save(schedule));
        }
        return ResponseEntity.notFound().build();
    }

    // ✅ Delete
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSchedule(@PathVariable Long id) {
        VaccinationSchedule schedule = service.getById(id);
        if (schedule != null) {
            service.delete(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    // Lấy lịch tiêm chủng theo học sinh
    @GetMapping("/student/{studentId}")
    public List<VaccinationSchedule> getByStudentId(@PathVariable Long studentId) {
        return service.getByStudentId(studentId);
    }

    // Lấy lịch tiêm chủng cần xác nhận từ phụ huynh
    @GetMapping("/pending-parent-consent")
    public List<VaccinationSchedule> getPendingParentConsent() {
        return service.getPendingParentConsent();
    }

    // Lấy lịch tiêm chủng cần xác nhận từ học sinh
    @GetMapping("/pending-student-confirmation")
    public List<VaccinationSchedule> getPendingStudentConfirmation() {
        return service.getPendingStudentConfirmation();
    }

    // Lấy lịch tiêm chủng theo trạng thái xác nhận của phụ huynh
    @GetMapping("/parent-consent/{consent}")
    public List<VaccinationSchedule> getByParentConsent(@PathVariable ConsentStatus consent) {
        return service.getByParentConsent(consent);
    }

    // Lấy lịch tiêm chủng chưa được tiêm
    @GetMapping("/not-vaccinated")
    public List<VaccinationSchedule> getNotVaccinated() {
        return service.getNotVaccinated();
    }

    // Lấy lịch tiêm chủng đã được tiêm
    @GetMapping("/vaccinated")
    public List<VaccinationSchedule> getVaccinated() {
        return service.getVaccinated();
    }

    // Xác nhận từ phụ huynh
    @PutMapping("/{id}/parent-consent")
    public ResponseEntity<VaccinationSchedule> updateParentConsent(
            @PathVariable Long id,
            @RequestBody Map<String, String> request) {
        try {
            ConsentStatus consent = ConsentStatus.valueOf(request.get("consent"));
            VaccinationSchedule schedule = service.updateParentConsent(id, consent);
            if (schedule != null) {
                return ResponseEntity.ok(schedule);
            }
            return ResponseEntity.notFound().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Xác nhận từ học sinh
    @PutMapping("/{id}/student-confirmation")
    public ResponseEntity<VaccinationSchedule> updateStudentConfirmation(
            @PathVariable Long id,
            @RequestBody Map<String, Boolean> request) {
        VaccinationSchedule schedule = service.updateStudentConfirmation(id, request.get("confirmed"));
        if (schedule != null) {
            return ResponseEntity.ok(schedule);
        }
        return ResponseEntity.notFound().build();
    }

    // Cập nhật trạng thái tiêm chủng
    @PutMapping("/{id}/vaccination-status")
    public ResponseEntity<VaccinationSchedule> updateVaccinationStatus(
            @PathVariable Long id,
            @RequestBody Map<String, Boolean> request) {
        VaccinationSchedule schedule = service.updateVaccinationStatus(id, request.get("isVaccinated"));
        if (schedule != null) {
            return ResponseEntity.ok(schedule);
        }
        return ResponseEntity.notFound().build();
    }

    // Lấy lịch tiêm chủng theo khoảng thời gian
    @GetMapping("/date-range")
    public List<VaccinationSchedule> getByDateRange(
            @RequestParam String startDate,
            @RequestParam String endDate) {
        LocalDateTime start = LocalDateTime.parse(startDate);
        LocalDateTime end = LocalDateTime.parse(endDate);
        return service.getByDateRange(start, end);
    }

    // Tạo nhiều lịch tiêm chủng cho nhiều học sinh
    @PostMapping("/bulk")
    public ResponseEntity<?> createBulkSchedules(@RequestBody Map<String, Object> payload) {
        try {
            // Lấy danh sách studentIds
            List<Integer> studentIds = (List<Integer>) payload.get("studentIds");
            String vaccineName = (String) payload.get("vaccineName");
            String scheduledDateTime = (String) payload.get("scheduledDateTime");
            String notes = payload.get("notes") != null ? (String) payload.get("notes") : null;
            String location = payload.get("location") != null ? (String) payload.get("location") : null;

            if (studentIds == null || studentIds.isEmpty() || vaccineName == null || scheduledDateTime == null) {
                return ResponseEntity.badRequest().body("Thiếu thông tin bắt buộc");
            }

            List<VaccinationSchedule> createdSchedules = new java.util.ArrayList<>();
            for (Integer id : studentIds) {
                Long studentId = id.longValue();
                var student = studentService.getStudentById(studentId);
                VaccinationSchedule schedule = new VaccinationSchedule();
                schedule.setStudentId(studentId);
                schedule.setStudentName(student.getFullName());
                schedule.setStudentCode(student.getCode());
                schedule.setVaccineName(vaccineName);
                schedule.setScheduledDateTime(java.time.LocalDateTime.parse(scheduledDateTime));
                schedule.setNotes(notes);
                schedule.setLocation(location);
                schedule.setVaccinated(false);
                schedule.setParentConsent(com.java.backend.enums.ConsentStatus.PENDING);
                schedule.setStudentConfirmation(false);
                createdSchedules.add(service.save(schedule));
            }
            return ResponseEntity.ok(createdSchedules);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Lỗi khi tạo lịch tiêm chủng hàng loạt: " + e.getMessage());
        }
    }

    // Lấy lịch tiêm chủng cần xác nhận từ phụ huynh theo parentId
    @GetMapping("/pending-parent-consent/{parentId}")
    public List<VaccinationSchedule> getPendingParentConsentByParentId(@PathVariable Long parentId) {
        return service.getPendingParentConsentByParentId(parentId);
    }
}