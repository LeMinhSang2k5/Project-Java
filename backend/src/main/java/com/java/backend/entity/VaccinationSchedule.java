package com.java.backend.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "vaccination_schedules")
public class VaccinationSchedule {

    @Id
    private String id;

    private String personName;
    private String vaccineName;
    private LocalDateTime scheduledDateTime;
    private boolean isVaccinated;
    private String notes;

    // Getters, Setters, Constructors (hoặc dùng Lombok)

    public VaccinationSchedule() {
    }

    public VaccinationSchedule(String id, String personName, String vaccineName, LocalDateTime scheduledDateTime,
            boolean isVaccinated, String notes) {
        this.id = id;
        this.personName = personName;
        this.vaccineName = vaccineName;
        this.scheduledDateTime = scheduledDateTime;
        this.isVaccinated = isVaccinated;
        this.notes = notes;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public String getVaccineName() {
        return vaccineName;
    }

    public void setVaccineName(String vaccineName) {
        this.vaccineName = vaccineName;
    }

    public LocalDateTime getScheduledDateTime() {
        return scheduledDateTime;
    }

    public void setScheduledDateTime(LocalDateTime scheduledDateTime) {
        this.scheduledDateTime = scheduledDateTime;
    }

    public boolean isVaccinated() {
        return isVaccinated;
    }

    public void setVaccinated(boolean vaccinated) {
        isVaccinated = vaccinated;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
