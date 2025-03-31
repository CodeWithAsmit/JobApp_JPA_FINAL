package com.asmit.JobApp.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.asmit.JobApp.model.JobPost;
import com.asmit.JobApp.model.UserProfile;
import com.asmit.JobApp.repo.JobPostRepository;
import com.asmit.JobApp.repo.UserProfileRepository;

@Service

public class WebSocketNotificationService
{   
    @Autowired
    private UserProfileRepository userProfileRepository;

    @Autowired
    public JobPostRepository jobPostRepository;

    private final SimpMessagingTemplate messagingTemplate;

    public WebSocketNotificationService(SimpMessagingTemplate messagingTemplate)
    {
        this.messagingTemplate = messagingTemplate;
    }

    public void notifySubscribers(JobPost job)
    {
        List<UserProfile> matchingUsers = userProfileRepository.findByPreferredLocationOrPrefersRemote(job.getLocation(), job.isRemote());

        List<JobPost> relevantJobs = jobPostRepository.findByLocationOrRemote(job.getLocation(), job.isRemote());

        String jobMessage = relevantJobs.stream().map(JobPost::getPostProfile).collect(Collectors.joining(", "));

        for (UserProfile user : matchingUsers)
        {   
            String notificationMessage = "Hey " +  user.getName() + " New jobs posted for role: " + jobMessage + " at your preferred location " + user.getPreferredLocation() + " grab it";
            System.out.println(notificationMessage);
            messagingTemplate.convertAndSendToUser(user.getEmail(),"/queue/job-alerts",notificationMessage);
        }
    }
}