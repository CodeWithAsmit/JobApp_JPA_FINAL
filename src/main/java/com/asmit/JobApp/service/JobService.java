package com.asmit.JobApp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.asmit.JobApp.model.JobPost;
import com.asmit.JobApp.repo.JobRepo;

@Service
public class JobService
{
    @Autowired
    public JobRepo repo;

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

    public void loadJobData()
    {
        List<JobPost> jobPosts = List.of(
                new JobPost(1, "Java Developer", "Java Developer with 2-3 years of experience", 2, List.of("Java", "Spring Boot", "Hibernate")),
                new JobPost(2, "React Developer", "React Developer with 2-3 years of experience", 2, List.of("React", "Redux", "JavaScript")),
                new JobPost(3, "Angular Developer", "Angular Developer with 2-3 years of experience", 2, List.of("Angular", "TypeScript", "RxJS")),
                new JobPost(4, "Python Developer", "Python Developer with 2-3 years of experience", 2, List.of("Python", "Django", "Flask")),
                new JobPost(5, "Node.js Developer", "Node.js Developer with 2-3 years of experience", 2, List.of("Node.js", "Express.js", "MongoDB"))
        );
        repo.saveAll(jobPosts);
    }

    public List<JobPost> getJobByKeyword(String keyword)
    {
        return repo.findByPostProfileContainingOrPostDescContaining(keyword,keyword);
    }
}
