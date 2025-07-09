package com.java.backend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.java.backend.entity.Nurse;
import com.java.backend.repository.NurseRepository;
import com.java.backend.enums.Role;

@Service
public class NurseService {

    @Autowired
    private NurseRepository nurseRepository;

    public Nurse saveNurse(Nurse nurse) {
        try {
            // Đảm bảo role được set
            if (nurse.getRole() == null) {
                nurse.setRole(Role.SCHOOL_NURSE);
            }

            // Đảm bảo isActive được set
            if (!nurse.isActive()) {
                nurse.setActive(true);
            }

            System.out.println("Saving nurse: " + nurse.getEmail() + ", Role: " + nurse.getRole());
            return nurseRepository.save(nurse);
        } catch (Exception e) {
            System.err.println("Error saving nurse: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    public List<Nurse> getAllNurses() {
        try {
            return nurseRepository.findAll();
        } catch (Exception e) {
            System.err.println("Error getting all nurses: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
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