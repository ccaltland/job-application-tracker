// ./mvnw clean package
// ./mvnw spring-boot:run
package com.chayse.jobtracker.controller;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.chayse.jobtracker.model.JobApplication;
import com.chayse.jobtracker.service.JobApplicationService;
import org.springframework.web.bind.annotation.RequestParam;
import com.chayse.jobtracker.model.JobStatus;
import org.springframework.web.bind.annotation.CrossOrigin;

@RestController
@RequestMapping("/applications")
@CrossOrigin(origins = {
    "http://localhost:5173",
    "http://127.0.0.1:5173"
})
public class JobApplicationController {

    private final JobApplicationService service;

    public JobApplicationController(JobApplicationService service) {
        this.service = service;
    }

    @GetMapping
    public List<JobApplication> getApplications(
            @RequestParam(required = false) JobStatus status,
            @RequestParam(required = false) String company,
            @RequestParam(required = false) String sort) {

        return service.getApplications(status, company, sort);
    }

    @PostMapping
    public JobApplication createApplication(
            @Valid @RequestBody JobApplication jobApplication) {
        return service.save(jobApplication);
    }

    @GetMapping("/{id}")
    public JobApplication getApplication(@PathVariable Long id) {
        return service.findById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteApplication(@PathVariable Long id) {
        service.delete(id);
    }

    @PutMapping("/{id}")
    public JobApplication updateApplication(@PathVariable Long id, @Valid @RequestBody JobApplication jobApplication) {
        return service.update(id, jobApplication);
    }

}