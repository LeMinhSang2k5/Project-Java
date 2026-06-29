package com.java.backend.service;

import com.java.backend.entity.Student;
import com.java.backend.entity.VaccinationSchedule;
import com.java.backend.enums.ConsentStatus;
import com.java.backend.exception.StudentNotFoundException;
import com.java.backend.exception.VaccinationScheduleNotFoundException;
import com.java.backend.repository.StudentRepository;
import com.java.backend.repository.VaccinationScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class VaccinationScheduleService {

    @Autowired
    private VaccinationScheduleRepository repository;

    @Autowired
    private StudentRepository studentRepository;

    public List<VaccinationSchedule> getAll() {
        return repository.findAll();
    }

    public VaccinationSchedule getById(Long id) {
        return repository.findById(id).orElse(null);
    }

    public VaccinationSchedule getByIdOrThrow(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new VaccinationScheduleNotFoundException(id));
    }

    public void validateSchedule(VaccinationSchedule schedule) {
        if (schedule.getStudentId() == null) {
            throw new IllegalArgumentException("Phải cung cấp ID học sinh");
        }
        if (!studentRepository.existsById(schedule.getStudentId())) {
            throw new StudentNotFoundException(schedule.getStudentId());
        }
        if (schedule.getVaccineName() == null || schedule.getVaccineName().trim().isEmpty()) {
            throw new IllegalArgumentException("Tên vaccine không được để trống");
        }
        if (schedule.getScheduledDateTime() == null) {
            throw new IllegalArgumentException("Ngày giờ tiêm không được để trống");
        }
        if (schedule.getStudentName() == null || schedule.getStudentName().trim().isEmpty()) {
            throw new IllegalArgumentException("Tên học sinh không được để trống");
        }
    }

    public void validateDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException("Ngày bắt đầu và ngày kết thúc không được để trống");
        }
        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Ngày bắt đầu không được sau ngày kết thúc");
        }
    }

    public ConsentStatus parseParentConsent(String consent) {
        if (consent == null || consent.trim().isEmpty()) {
            throw new IllegalArgumentException("Giá trị đồng ý không được để trống");
        }
        try {
            return ConsentStatus.valueOf(consent.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Giá trị đồng ý không hợp lệ");
        }
    }

    public void validateBooleanField(Boolean value, String fieldName) {
        if (value == null) {
            throw new IllegalArgumentException(fieldName + " không hợp lệ");
        }
    }

    public VaccinationSchedule save(VaccinationSchedule schedule) {
        validateSchedule(schedule);
        schedule.setUpdatedDate(LocalDateTime.now());
        if (schedule.getCreatedDate() == null) {
            schedule.setCreatedDate(LocalDateTime.now());
        }
        return repository.save(schedule);
    }

    public VaccinationSchedule saveWithoutValidation(VaccinationSchedule schedule) {
        schedule.setUpdatedDate(LocalDateTime.now());
        return repository.save(schedule);
    }

    public void delete(Long id) {
        getByIdOrThrow(id);
        repository.deleteById(id);
    }

    public List<VaccinationSchedule> getByStudentId(Long studentId) {
        return repository.findByStudentId(studentId);
    }

    public List<VaccinationSchedule> getPendingParentConsent() {
        return repository.findPendingParentConsent();
    }

    public List<VaccinationSchedule> getPendingStudentConfirmation() {
        return repository.findPendingStudentConfirmation();
    }

    public List<VaccinationSchedule> getByParentConsent(ConsentStatus parentConsent) {
        return repository.findByParentConsent(parentConsent);
    }

    public List<VaccinationSchedule> getNotVaccinated() {
        return repository.findByIsVaccinatedFalse();
    }

    public List<VaccinationSchedule> getVaccinated() {
        return repository.findByIsVaccinatedTrue();
    }

    public VaccinationSchedule updateParentConsent(Long id, ConsentStatus consent) {
        VaccinationSchedule schedule = getByIdOrThrow(id);
        schedule.setParentConsent(consent);
        schedule.setUpdatedDate(LocalDateTime.now());
        return repository.save(schedule);
    }

    public VaccinationSchedule updateStudentConfirmation(Long id, boolean confirmed) {
        VaccinationSchedule schedule = getByIdOrThrow(id);
        schedule.setStudentConfirmation(confirmed);
        schedule.setUpdatedDate(LocalDateTime.now());
        return repository.save(schedule);
    }

    public VaccinationSchedule updateVaccinationStatus(Long id, boolean isVaccinated) {
        VaccinationSchedule schedule = getByIdOrThrow(id);
        schedule.setVaccinated(isVaccinated);
        schedule.setUpdatedDate(LocalDateTime.now());
        return repository.save(schedule);
    }

    public List<VaccinationSchedule> getByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        validateDateRange(startDate, endDate);
        return repository.findByScheduledDateTimeBetween(startDate, endDate);
    }

    public List<VaccinationSchedule> getPendingParentConsentByParentId(Long parentId) {
        return repository.findPendingParentConsentByParentId(parentId);
    }

    public VaccinationSchedule createFromStudent(Long studentId, String vaccineName,
            LocalDateTime scheduledDateTime, String notes, String location) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new StudentNotFoundException(studentId));

        VaccinationSchedule schedule = new VaccinationSchedule();
        schedule.setStudentId(studentId);
        schedule.setStudentName(student.getFullName());
        schedule.setStudentCode(student.getCode());
        schedule.setVaccineName(vaccineName);
        schedule.setScheduledDateTime(scheduledDateTime);
        schedule.setNotes(notes);
        schedule.setLocation(location);
        schedule.setVaccinated(false);
        schedule.setParentConsent(ConsentStatus.PENDING);
        schedule.setStudentConfirmation(false);
        return save(schedule);
    }
}
