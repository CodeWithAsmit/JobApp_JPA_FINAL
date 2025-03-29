package com.asmit.JobApp.service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.asmit.JobApp.model.JobPost;
import com.asmit.JobApp.repo.JobPostRepository;
import com.asmit.JobApp.repo.UserProfileRepository;

@Service
public class JobService
{
    @Autowired
    public JobPostRepository repo;

    @Autowired
    UserProfileRepository userProfileRepository;

    public void addJobPost(JobPost jobPost)
    {
        repo.save(jobPost);
    }

    public List<JobPost> returnAllJobPosts()
	{
        return repo.findAll();
    }

    public JobPost getJobById(int id)
    {
        return repo.findById(id).orElse(new JobPost());
    }

    public void updateJobPost(JobPost jobPost)
    {
        repo.save(jobPost);
    }

    public void deleteJobPost(int id)
    {
        repo.deleteById(id);
    }

    public List<JobPost> getJobByKeyword(String keyword)
    {
        return repo.findByKeyword(keyword);
    }

    public List<JobPostDTO> recommendJobs(int userId)
    {
        List<String> skills = userProfileRepository.findSkillsByUserId(userId);

        if (!skills.isEmpty())
        {
            List<JobPost> jobPosts = repo.findBySkillsIn(skills);

            return jobPosts.stream()
                    .map(jobPost -> {
                        double similarityScore = calculateSimilarityScore(skills, jobPost.getPostTechStack());
                        return new JobPostDTO(jobPost, similarityScore);
                    })
                    .collect(Collectors.toList());
        }
        return List.of();
    }

    private double calculateSimilarityScore(List<String> userSkills, List<String> jobSkills)
    {
        Set<String> uniqueJobSkills = jobSkills.stream().collect(Collectors.toSet());
        long matchedSkills = userSkills.stream()
                .filter(uniqueJobSkills::contains)
                .count();

        if (uniqueJobSkills.size() == 0)
        {
            return 0.0;
        }
        return (double) matchedSkills / uniqueJobSkills.size();
    }
}
