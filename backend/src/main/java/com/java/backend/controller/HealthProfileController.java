package com.java.backend.controller;

import com.java.backend.entity.HealthProfile;
import com.java.backend.service.HealthProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/health-profiles")
@CrossOrigin(origins = "http://localhost:3000") // Cho phép frontend React truy cập
public class HealthProfileController {

    @Autowired
    private HealthProfileService healthProfileService;

    @PostMapping
    public HealthProfile createHealthProfile(@RequestBody HealthProfile healthProfile) {
        return healthProfileService.saveHealthProfile(healthProfile);
    }

    @GetMapping
    public List<HealthProfile> getAllHealthProfiles() {
        return healthProfileService.getAllHealthProfiles();
    }

    @GetMapping("/{id}")
    public HealthProfile getHealthProfileById(@PathVariable Long id) {
        return healthProfileService.getHealthProfileById(id);
    }

    @PutMapping("/{id}")
    public HealthProfile updateHealthProfile(@PathVariable Long id, @RequestBody HealthProfile healthProfile) {
        return healthProfileService.updateHealthProfile(healthProfile);
    }

    @DeleteMapping("/{id}")
    public void deleteHealthProfile(@PathVariable Long id) {
        healthProfileService.deleteHealthProfile(id);
    }
} 