package com.java.backend.controller;

import com.java.backend.entity.Nurse;
import com.java.backend.service.NurseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/nurses")
@CrossOrigin(origins = "http://localhost:3000")
public class NurseController {

    @Autowired
    private NurseService nurseService;

    @PostMapping
    public ResponseEntity<Nurse> createNurse(@RequestBody Nurse nurse) {
        Nurse saved = nurseService.saveNurse(nurse);
        return ResponseEntity.ok(saved);
    }

    @GetMapping
    public List<Nurse> getAllNurses() {
        return nurseService.getAllNurses();
    }

    @GetMapping("/{id}")
    public Nurse getNurse(@PathVariable Long id) {
        return nurseService.getNurseById(id);
    }

    @PutMapping("/{id}")
    public Nurse updateNurse(@PathVariable Long id, @RequestBody Nurse nurse) {
        nurse.setId(id);
        return nurseService.updateNurse(nurse);
    }

    @DeleteMapping("/{id}")
    public void deleteNurse(@PathVariable Long id) {
        nurseService.deleteNurse(id);
    }
}