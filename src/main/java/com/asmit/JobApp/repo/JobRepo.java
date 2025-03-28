package com.asmit.JobApp.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.asmit.JobApp.model.JobPost;

import java.util.List;

@Repository
public interface JobRepo extends JpaRepository<JobPost, Integer>
{
    List<JobPost> findByPostProfileContainingOrPostDescContaining(String Profile, String PostDesc);
}