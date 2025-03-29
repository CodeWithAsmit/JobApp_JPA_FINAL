package com.asmit.JobApp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.asmit.JobApp.model.JobPost;
import com.asmit.JobApp.service.JobPostDTO;
import com.asmit.JobApp.service.JobService;

@RestController
@CrossOrigin(origins = "http://localhost:3000")

public class JobController
{
	@Autowired
	private JobService service;

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
	public JobPost addJobPost(@RequestBody JobPost job)
	{
		service.addJobPost(job);
		return service.getJobById(job.getPostId());
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
}
