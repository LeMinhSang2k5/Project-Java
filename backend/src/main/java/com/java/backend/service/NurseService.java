package com.java.backend.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.java.backend.entity.Nurse;
import com.java.backend.exception.NurseNotFoundException;
import com.java.backend.repository.NurseRepository;
import com.java.backend.enums.Role;

@Service
public class NurseService {
    private static final Logger logger = LoggerFactory.getLogger(NurseService.class);


    @Autowired
    private NurseRepository nurseRepository;

    public Nurse saveNurse(Nurse nurse) {
        try {
            nurse.setRole(Role.SCHOOL_NURSE);

            // Đảm bảo isActive được set
            if (!nurse.isActive()) {
                nurse.setActive(true);
            }

            logger.info("Saving nurse: {}", nurse.getEmail() + ", Role: " + nurse.getRole());
            return nurseRepository.save(nurse);
        } catch (Exception e) {
            logger.error("Error saving nurse: {}", e.getMessage());
            logger.error(e.getMessage(), e);
            throw e;
        }
    }

    public List<Nurse> getAllNurses() {
        try {
            return nurseRepository.findAll();
        } catch (Exception e) {
            logger.error("Error getting all nurses: {}", e.getMessage());
            logger.error(e.getMessage(), e);
            throw e;
        }
    }

    public Nurse getNurseById(Long id) {
        return nurseRepository.findById(id)
                .orElseThrow(() -> new NurseNotFoundException(id));
    }

    public Nurse updateNurse(Nurse nurse) {
        return nurseRepository.save(nurse);
    }

    public void deleteNurse(Long id) {
        if (!nurseRepository.existsById(id)) {
            throw new NurseNotFoundException(id);
        }
        nurseRepository.deleteById(id);
    }
}