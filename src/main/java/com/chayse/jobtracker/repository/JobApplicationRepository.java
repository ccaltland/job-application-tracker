package com.chayse.jobtracker.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.chayse.jobtracker.model.JobApplication;
import java.util.List;
import com.chayse.jobtracker.model.JobStatus;
import org.springframework.data.domain.Sort;

public interface JobApplicationRepository extends JpaRepository<JobApplication, Long> {

    List<JobApplication> findByStatus(JobStatus status);

    List<JobApplication> findByStatus(JobStatus status, Sort sort);

    List<JobApplication> findByCompanyContainingIgnoreCase(String company);

    List<JobApplication> findByCompanyContainingIgnoreCaseAndStatus(String company, JobStatus status);
}