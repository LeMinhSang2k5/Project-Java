package com.java.backend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.java.backend.entity.Nurse;
import com.java.backend.repository.NurseRepository;

@Service
public class NurseService {

    @Autowired
    private NurseRepository nurseRepository;

    public Nurse saveNurse(Nurse nurse) {
        return nurseRepository.save(nurse);
    }

    public List<Nurse> getAllNurses() {
        return nurseRepository.findAll();
    }

    public Nurse getNurseById(Long id) {
        return nurseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Nurse not found with id: " + id));
    }

    public Nurse updateNurse(Nurse nurse) {
        return nurseRepository.save(nurse);
    }

    public void deleteNurse(Long id) {
        nurseRepository.deleteById(id);
    }
}