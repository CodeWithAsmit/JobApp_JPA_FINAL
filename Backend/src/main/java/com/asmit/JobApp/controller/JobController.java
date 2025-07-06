package com.asmit.JobApp.controller;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Value;
import com.asmit.JobApp.model.JobApplication;
import com.asmit.JobApp.model.JobPost;
import com.asmit.JobApp.repo.JobPostRepository;
import com.asmit.JobApp.service.JobPostDTO;
import com.asmit.JobApp.service.JobService;
import com.asmit.JobApp.service.EmailNotificationService;
import org.springframework.web.bind.annotation.CrossOrigin;

@RestController
@CrossOrigin(origins = "http://localhost:5173" , allowCredentials = "true")

public class JobController
{
	@Autowired
	private JobService service;

	@Autowired
    public JobPostRepository repo;

	@Autowired
    private EmailNotificationService EmailNotificationService;

	@Value("${ENABLE_DEBUG_LOGS:false}")
    private boolean enableDebugLogs;

	private static final Logger logger = LoggerFactory.getLogger(JobController.class);

	@GetMapping(path="/jobPosts")
	public List<JobPost> getAllJobs()
	{
		return service.returnAllJobPosts();
	}

	@GetMapping("/jobPost/{id}")
	public JobPost getJobById(@PathVariable int id)
	{
		if(enableDebugLogs)
        {
            logger.debug("job is received from user is {}", id);
        }
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
		if(enableDebugLogs)
		{
			logger.debug("job post to be deleted is {}", id);
		}

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
		if(enableDebugLogs)
		{
			logger.debug("keyword received from user is {}", keyword);
		}
		return service.getJobByKeyword(keyword);
	}

	@GetMapping("/recommend/{userId}")
    public List<JobPostDTO> getRecommendedJobs(@PathVariable int userId)
	{
		if(enableDebugLogs)
		{
			logger.debug("User ID for job recommendations is {}", userId);
		}
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
		if(enableDebugLogs)
		{
			logger.debug("Fetching applications for job ID: {}", jobId);
		}
		return service.getApplicationsForJob(jobId);
	}
}