package com.chayse.jobtracker.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.chayse.jobtracker.model.JobApplication;
import java.util.List;
import com.chayse.jobtracker.model.JobStatus;

public interface JobApplicationRepository extends JpaRepository<JobApplication, Long> {
    List<JobApplication> findByStatus(JobStatus status);
}