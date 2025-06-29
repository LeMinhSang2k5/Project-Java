package com.java.backend.service;

import com.java.backend.entity.VaccinationSchedule;
import com.java.backend.repository.VaccinationScheduleRepository;
import com.java.backend.enums.ConsentStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class VaccinationScheduleService {

    @Autowired
    private VaccinationScheduleRepository repository;

    public List<VaccinationSchedule> getAll() {
        return repository.findAll();
    }

    public VaccinationSchedule getById(Long id) {
        return repository.findById(id).orElse(null);
    }

    public VaccinationSchedule save(VaccinationSchedule schedule) {
        schedule.setUpdatedDate(LocalDateTime.now());
        return repository.save(schedule);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    // Lấy lịch tiêm chủng theo học sinh
    public List<VaccinationSchedule> getByStudentId(Long studentId) {
        return repository.findByStudentId(studentId);
    }

    // Lấy lịch tiêm chủng cần xác nhận từ phụ huynh
    public List<VaccinationSchedule> getPendingParentConsent() {
        return repository.findPendingParentConsent();
    }

    // Lấy lịch tiêm chủng cần xác nhận từ học sinh
    public List<VaccinationSchedule> getPendingStudentConfirmation() {
        return repository.findPendingStudentConfirmation();
    }

    // Lấy lịch tiêm chủng theo trạng thái xác nhận của phụ huynh
    public List<VaccinationSchedule> getByParentConsent(ConsentStatus parentConsent) {
        return repository.findByParentConsent(parentConsent);
    }

    // Lấy lịch tiêm chủng chưa được tiêm
    public List<VaccinationSchedule> getNotVaccinated() {
        return repository.findByIsVaccinatedFalse();
    }

    // Lấy lịch tiêm chủng đã được tiêm
    public List<VaccinationSchedule> getVaccinated() {
        return repository.findByIsVaccinatedTrue();
    }

    // Xác nhận từ phụ huynh
    public VaccinationSchedule updateParentConsent(Long id, ConsentStatus consent) {
        VaccinationSchedule schedule = repository.findById(id).orElse(null);
        if (schedule != null) {
            schedule.setParentConsent(consent);
            schedule.setUpdatedDate(LocalDateTime.now());
            return repository.save(schedule);
        }
        return null;
    }

    // Xác nhận từ học sinh
    public VaccinationSchedule updateStudentConfirmation(Long id, boolean confirmed) {
        VaccinationSchedule schedule = repository.findById(id).orElse(null);
        if (schedule != null) {
            schedule.setStudentConfirmation(confirmed);
            schedule.setUpdatedDate(LocalDateTime.now());
            return repository.save(schedule);
        }
        return null;
    }

    // Cập nhật trạng thái tiêm chủng
    public VaccinationSchedule updateVaccinationStatus(Long id, boolean isVaccinated) {
        VaccinationSchedule schedule = repository.findById(id).orElse(null);
        if (schedule != null) {
            schedule.setVaccinated(isVaccinated);
            schedule.setUpdatedDate(LocalDateTime.now());
            return repository.save(schedule);
        }
        return null;
    }

    // Lấy lịch tiêm chủng theo khoảng thời gian
    public List<VaccinationSchedule> getByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return repository.findByScheduledDateTimeBetween(startDate, endDate);
    }
}
