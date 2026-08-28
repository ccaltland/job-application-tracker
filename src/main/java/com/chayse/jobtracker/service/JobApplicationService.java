package com.chayse.jobtracker.service;
import java.util.List;
import org.springframework.stereotype.Service;
import com.chayse.jobtracker.model.JobApplication;
import com.chayse.jobtracker.repository.JobApplicationRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.bind.annotation.RequestParam;
import com.chayse.jobtracker.model.JobStatus;

@Service
public class JobApplicationService {

    private final JobApplicationRepository repository;

    public JobApplicationService(JobApplicationRepository repository) {
        this.repository = repository;
    }

    public List<JobApplication> getAllApplications() {
        return repository.findAll();
    }

    public JobApplication save(JobApplication jobApplication) {
        return repository.save(jobApplication);
    }

    public JobApplication findById(Long id){
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Application not found"
                ));
    }

    public void delete(Long id) {
        JobApplication existing = repository.findById(id)
         .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Application not found"
         ));
         repository.delete(existing);
    }

    public JobApplication update(Long id, JobApplication jobApplication) {
        JobApplication existing = repository.findById(id)
         .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Application not found"
         ));
    


    existing.setCompany(jobApplication.getCompany());
    existing.setPosition(jobApplication.getPosition());
    existing.setStatus(jobApplication.getStatus());
    existing.setDateApplied(jobApplication.getDateApplied());
    existing.setNotes(jobApplication.getNotes());

    return repository.save(existing);
    }
    public List<JobApplication> findByStatus(JobStatus status) {
        return repository.findByStatus(status);
    }
}