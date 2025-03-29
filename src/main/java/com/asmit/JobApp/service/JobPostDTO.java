package com.asmit.JobApp.service;

import lombok.Getter;
import lombok.Setter;
import java.util.List;
import com.asmit.JobApp.model.JobPost;

@Getter
@Setter

public class JobPostDTO
{
    private int postId;
    private String postProfile;
    private String postDesc;
    private int reqExperience;
    private List<String> postTechStack;
    private double similarityScore;

    public JobPostDTO(JobPost jobPost, double similarityScore)
    {
        this.postId = jobPost.getPostId();
        this.postProfile = jobPost.getPostProfile();
        this.postDesc = jobPost.getPostDesc();
        this.reqExperience = jobPost.getReqExperience();
        this.postTechStack = jobPost.getPostTechStack();
        this.similarityScore = similarityScore;
    }
}
