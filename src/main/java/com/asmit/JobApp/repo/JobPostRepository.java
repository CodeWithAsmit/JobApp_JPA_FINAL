package com.asmit.JobApp.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.asmit.JobApp.model.JobPost;

import java.util.List;

@Repository
public interface JobPostRepository extends JpaRepository<JobPost, Integer>
{
    @Query("SELECT j FROM JobPost j WHERE " +
           "LOWER(j.postDesc) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(j.postProfile) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<JobPost> findByKeyword(@Param("keyword") String keyword);

    @Query(value = "SELECT DISTINCT jp.* FROM job_post jp " +
    "JOIN job_tech_stack jts ON jp.post_id = jts.post_id " +
    "WHERE jts.tech_stack IN (:skills)", nativeQuery = true)
    List<JobPost> findBySkillsIn(@Param("skills") List<String> skills);
}