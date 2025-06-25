package com.java.backend.repository;

import com.java.backend.entity.VaccinationSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VaccinationScheduleRepository extends JpaRepository<VaccinationSchedule, String> {
}
