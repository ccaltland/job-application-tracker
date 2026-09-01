package com.chayse.jobtracker.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
public class JobApplication {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Company is required")
    private String company;
    @NotBlank(message = "Position is required")
    private String position;
    @NotNull(message = "Status is required")
    @Enumerated(EnumType.STRING)
    private JobStatus status;
    private LocalDate dateApplied;
    private String notes;

    public JobApplication() {
    }

    public Long getId() {
        return this.id;
    }

    public String getCompany() {
        return this.company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getPosition() {
        return this.position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public JobStatus getStatus() {
        return this.status;
    }

    public void setStatus(JobStatus status) {
        this.status = status;
    }

    public LocalDate getDateApplied() {
        return this.dateApplied;
    }

    public void setDateApplied(LocalDate dateApplied) {
        this.dateApplied = dateApplied;
    }

    public String getNotes() {
        return this.notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
