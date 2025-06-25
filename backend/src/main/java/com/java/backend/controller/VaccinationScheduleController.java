package com.java.backend.controller;

import com.java.backend.entity.VaccinationSchedule;
import com.java.backend.service.VaccinationScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vaccinations")
@CrossOrigin(origins = "http://localhost:3000")
public class VaccinationScheduleController {

    @Autowired
    private VaccinationScheduleService service;

    // ✅ Get all
    @GetMapping
    public List<VaccinationSchedule> getAllSchedules() {
        return service.getAll();
    }

    // ✅ Get by ID
    @GetMapping("/{id}")
    public VaccinationSchedule getSchedule(@PathVariable String id) {
        return service.getById(id);
    }

    // ✅ Create or Update
    @PostMapping
    public VaccinationSchedule addSchedule(@RequestBody VaccinationSchedule schedule) {
        return service.save(schedule);
    }

    // ✅ Update
    @PutMapping("/{id}")
    public VaccinationSchedule updateSchedule(@PathVariable String id, @RequestBody VaccinationSchedule schedule) {
        schedule.setId(id);
        return service.save(schedule);
    }

    // ✅ Delete
    @DeleteMapping("/{id}")
    public void deleteSchedule(@PathVariable String id) {
        service.delete(id);
    }
}