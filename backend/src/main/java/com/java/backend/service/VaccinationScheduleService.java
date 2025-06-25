package com.java.backend.service;

import com.java.backend.entity.VaccinationSchedule;
import com.java.backend.repository.VaccinationScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VaccinationScheduleService {

    @Autowired
    private VaccinationScheduleRepository repository;

    public List<VaccinationSchedule> getAll() {
        return repository.findAll();
    }

    public VaccinationSchedule getById(String id) {
        return repository.findById(id).orElse(null);
    }

    public VaccinationSchedule save(VaccinationSchedule schedule) {
        return repository.save(schedule);
    }

    public void delete(String id) {
        repository.deleteById(id);
    }
}
