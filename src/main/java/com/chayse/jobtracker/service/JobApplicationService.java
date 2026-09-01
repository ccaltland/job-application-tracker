package com.chayse.jobtracker.service;

import java.util.List;
import org.springframework.stereotype.Service;
import com.chayse.jobtracker.model.JobApplication;
import com.chayse.jobtracker.repository.JobApplicationRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import com.chayse.jobtracker.model.JobStatus;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

@Service
public class JobApplicationService {

    private final JobApplicationRepository repository;

    public JobApplicationService(JobApplicationRepository repository) {
        this.repository = repository;
    }

    public List<JobApplication> getAllApplications() {
        return repository.findAll();
    }

    public List<JobApplication> getApplications(
            JobStatus status,
            String company,
            String sort) {

        Specification<JobApplication> spec = Specification.unrestricted();
        if (status != null) {
            spec = spec.and(hasStatus(status));
        }
        if (company != null) {
            spec = spec.and(companyContains(company));
        }
        Sort sorting = Sort.unsorted();

        if (sort != null) {
            if (sort.equalsIgnoreCase("ASC")) {
                sorting = Sort.by(Sort.Direction.ASC, "dateApplied");
            } else {
                sorting = Sort.by(Sort.Direction.DESC, "dateApplied");
            }
        }

        return repository.findAll(spec, sorting);
    }

    public JobApplication save(JobApplication jobApplication) {
        return repository.save(jobApplication);
    }

    public JobApplication findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Application not found"));
    }

    public void delete(Long id) {
        JobApplication existing = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Application not found"));
        repository.delete(existing);
    }

    public JobApplication update(Long id, JobApplication jobApplication) {
        JobApplication existing = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Application not found"));

        existing.setCompany(jobApplication.getCompany());
        existing.setPosition(jobApplication.getPosition());
        existing.setStatus(jobApplication.getStatus());
        existing.setDateApplied(jobApplication.getDateApplied());
        existing.setNotes(jobApplication.getNotes());

        return repository.save(existing);
    }

    private Specification<JobApplication> hasStatus(JobStatus status) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("status"), status);
    }

    private Specification<JobApplication> companyContains(String company) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(
                criteriaBuilder.lower(root.get("company")),
                "%" + company.toLowerCase() + "%");
    }
}