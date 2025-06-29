package com.asmit.JobApp.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.asmit.JobApp.model.JobApplication;
import com.asmit.JobApp.model.JobPost;
import com.asmit.JobApp.repo.JobPostRepository;
import com.asmit.JobApp.service.JobPostDTO;
import com.asmit.JobApp.service.JobService;
import com.asmit.JobApp.service.EmailNotificationService;

@RestController
@CrossOrigin

public class JobController
{
	@Autowired
	private JobService service;

	@Autowired
    public JobPostRepository repo;

	@Autowired
    private EmailNotificationService EmailNotificationService;

	@GetMapping(path="/jobPosts")
	public List<JobPost> getAllJobs()
	{
		return service.returnAllJobPosts();
	}

	@GetMapping("/jobPost/{id}")
	public JobPost getJobById(@PathVariable int id)
	{
		return service.getJobById(id);
	}

	@PostMapping(path="/addJobPost")
	public String addJobPost(@RequestBody JobPost job)
	{
		JobPost savedJob = service.addJobPost(job);
		EmailNotificationService.notifySubscribers(savedJob);
		return "Job posted successfully!";
	}

	@PutMapping("/updateJobPost")
	public JobPost updateJobPost(@RequestBody JobPost job)
	{
		System.out.println(job);
		service.updateJobPost(job);
		return service.getJobById(job.getPostId());
	}

	@DeleteMapping("/deleteJobPost/{id}")
	public String deleteJobPostById(@PathVariable int id)
	{
		JobPost job = service.getJobById(id);
		if (job == null)
		{
			return "Job Post not found!";
		}
		service.deleteJobPost(id);
		return "Job Post deleted successfully!";
	}

	@GetMapping("/jobPost/keyword/{keyword}")
	public List<JobPost> getJobByKeyword(@PathVariable String keyword)
	{
		return service.getJobByKeyword(keyword);
	}

	@GetMapping("/recommend/{userId}")
    public List<JobPostDTO> getRecommendedJobs(@PathVariable int userId)
	{
        return service.recommendJobs(userId);
    }

	@PostMapping("/apply/{jobId}")
    public String applyForJob(@PathVariable int jobId, @RequestParam(name = "ref", required = false) Integer referralId, @RequestParam(name = "userId") int userId)
	{
        boolean isApplied = service.applyForJob(jobId, userId, referralId);
		if (!isApplied)
		{
			return "Job application failed! Invalid job ID or referral.";
		}
		else
		{
			return "Job application successful!";
		}
    }

	@GetMapping("/applications/{jobId}")
	public List<JobApplication> getApplicationsForJob(@PathVariable int jobId)
	{
		return service.getApplicationsForJob(jobId);
	}
}
