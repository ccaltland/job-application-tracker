package com.chayse.jobtracker.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.chayse.jobtracker.model.JobApplication;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface JobApplicationRepository
        extends JpaRepository<JobApplication, Long>,
                JpaSpecificationExecutor<JobApplication> {
}