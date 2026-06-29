package com.java.backend.exception;

public class VaccinationScheduleNotFoundException extends RuntimeException {
    public VaccinationScheduleNotFoundException(Long id) {
        super("Không tìm thấy lịch tiêm chủng với ID: " + id);
    }
}
