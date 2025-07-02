package com.asmit.JobApp.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.asmit.JobApp.model.JobApplication;
import com.asmit.JobApp.model.JobPost;
import com.asmit.JobApp.model.UserProfile;
import java.util.List;

@Repository
public interface JobApplicationRepository extends JpaRepository<JobApplication , Long>
{
    List<JobApplication> findByJob(JobPost job);
    List<JobApplication> findByUser(UserProfile user);
    boolean existsByJobAndUser(JobPost job, UserProfile user);
}
