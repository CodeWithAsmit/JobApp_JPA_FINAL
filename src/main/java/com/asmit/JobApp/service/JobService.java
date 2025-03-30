package com.asmit.JobApp.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import com.asmit.JobApp.model.JobApplication;
import com.asmit.JobApp.model.JobPost;
import com.asmit.JobApp.model.Referral;
import com.asmit.JobApp.model.UserProfile;
import com.asmit.JobApp.repo.JobApplicationRepository;
import com.asmit.JobApp.repo.JobPostRepository;
import com.asmit.JobApp.repo.UserProfileRepository;

@Service
public class JobService
{
    @Autowired
    public JobPostRepository repo;

    @Autowired
    UserProfileRepository userProfileRepository;

    @Autowired
    private JobApplicationRepository jobApplicationRepository;

    @Autowired
    private ReferralService referralService;

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

    public boolean applyForJob(int jobId, int userId, Integer referralId)
    {
        JobPost job = repo.findById(jobId).orElse(null);
        UserProfile user = userProfileRepository.findById(userId).orElse(null);
        boolean alreadyApplied = jobApplicationRepository.existsByJobAndUser(job, user);
        Referral referral = null;

        if (job == null || user == null || alreadyApplied)
        {
            return false; 
        }

        if (referralId != null)
        {
            referral = referralService.getReferralById(referralId);
            if (referral == null || referral.getJobId() != jobId)
            {
                return false;
            }

            if (referral != null && referral.getJobId() == jobId)
            {
                referralService.trackJobApplication(referralId,userId);
            }
        }

        JobApplication application = new JobApplication();
        application.setUser(user);
        application.setJob(job);
        application.setReferral(referral);
        application.setAppliedAt(LocalDateTime.now());

        jobApplicationRepository.save(application);
        return true;
    }

    public List<JobApplication> getApplicationsForJob(@PathVariable int jobId)
	{
		JobPost job = repo.findById(jobId).orElse(null);
		if (job == null)
		{
			return new ArrayList<>();
		}
		return jobApplicationRepository.findByJob(job);
	}
}
