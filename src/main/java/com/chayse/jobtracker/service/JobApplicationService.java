package com.chayse.jobtracker.service;
import java.util.List;
import org.springframework.stereotype.Service;
import com.chayse.jobtracker.model.JobApplication;
import com.chayse.jobtracker.repository.JobApplicationRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.bind.annotation.RequestParam;
import com.chayse.jobtracker.model.JobStatus;
import org.springframework.data.domain.Sort;

@Service
public class JobApplicationService {

    private final JobApplicationRepository repository;

    public JobApplicationService(JobApplicationRepository repository) {
        this.repository = repository;
    }

    public List<JobApplication> getAllApplications() {
        return repository.findAll();
    }
    public List<JobApplication> getAllApplicationsSorted(String direction) {
        if (direction.equalsIgnoreCase("ASC")) {
            return repository.findAll(Sort.by(Sort.Direction.ASC, "dateApplied"));
        } else {
            return repository.findAll(Sort.by(Sort.Direction.DESC, "dateApplied"));
        }
    }
    public List<JobApplication> findByStatusSorted(
        JobStatus status, String direction) {
         if (direction.equalsIgnoreCase("ASC")) {
            return repository.findByStatus(status, Sort.by(Sort.Direction.ASC, "dateApplied"));
        } else {
            return repository.findByStatus(status, Sort.by(Sort.Direction.DESC, "dateApplied"));
        }
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

    public List<JobApplication> findByCompany(String company) {
        return repository.findByCompanyContainingIgnoreCase(company);
    }

    public List<JobApplication> findByCompanyAndStatus(String company, JobStatus status) {
        return repository.findByCompanyContainingIgnoreCaseAndStatus(company, status);
    }
}