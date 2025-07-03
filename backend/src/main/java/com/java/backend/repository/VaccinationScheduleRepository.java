package com.java.backend.repository;

import com.java.backend.entity.VaccinationSchedule;
import com.java.backend.enums.ConsentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface VaccinationScheduleRepository extends JpaRepository<VaccinationSchedule, Long> {

    // Tìm lịch tiêm chủng theo học sinh
    List<VaccinationSchedule> findByStudentId(Long studentId);

    // Tìm lịch tiêm chủng theo trạng thái xác nhận của phụ huynh
    List<VaccinationSchedule> findByParentConsent(ConsentStatus parentConsent);

    // Tìm lịch tiêm chủng theo học sinh và trạng thái xác nhận
    List<VaccinationSchedule> findByStudentIdAndParentConsent(Long studentId, ConsentStatus parentConsent);

    // Tìm lịch tiêm chủng chưa được tiêm
    List<VaccinationSchedule> findByIsVaccinatedFalse();

    // Tìm lịch tiêm chủng đã được tiêm
    List<VaccinationSchedule> findByIsVaccinatedTrue();

    // Tìm lịch tiêm chủng theo học sinh và trạng thái tiêm
    List<VaccinationSchedule> findByStudentIdAndIsVaccinated(Long studentId, boolean isVaccinated);

    // Tìm lịch tiêm chủng cần xác nhận từ phụ huynh
    @Query("SELECT v FROM VaccinationSchedule v WHERE v.parentConsent = 'PENDING'")
    List<VaccinationSchedule> findPendingParentConsent();

    // Tìm lịch tiêm chủng cần xác nhận từ học sinh
    @Query("SELECT v FROM VaccinationSchedule v WHERE v.parentConsent = 'APPROVED' AND v.studentConfirmation = false")
    List<VaccinationSchedule> findPendingStudentConfirmation();

    // Tìm lịch tiêm chủng theo khoảng thời gian
    @Query("SELECT v FROM VaccinationSchedule v WHERE v.scheduledDateTime BETWEEN :startDate AND :endDate")
    List<VaccinationSchedule> findByScheduledDateTimeBetween(@Param("startDate") java.time.LocalDateTime startDate,
            @Param("endDate") java.time.LocalDateTime endDate);

    // Tìm lịch tiêm chủng cần xác nhận theo parent_id của học sinh
    @Query("SELECT v FROM VaccinationSchedule v WHERE v.parentConsent = 'PENDING' AND v.studentId IN (SELECT s.id FROM Student s WHERE s.parent.id = :parentId)")
    List<VaccinationSchedule> findPendingParentConsentByParentId(@Param("parentId") Long parentId);
}
