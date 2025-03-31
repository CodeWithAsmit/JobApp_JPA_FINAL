package com.asmit.JobApp.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.asmit.JobApp.model.JobPost;
import com.asmit.JobApp.model.UserProfile;
import com.asmit.JobApp.repo.JobPostRepository;
import com.asmit.JobApp.repo.UserProfileRepository;
import com.asmit.JobApp.utility.SendEmailHanlder;

@Service

public class EmailNotificationService
{   
    @Autowired
    private UserProfileRepository userProfileRepository;

    @Autowired
    public JobPostRepository jobPostRepository;

    @Autowired
    private SendEmailHanlder sendEmailHanlder;

    public void notifySubscribers(JobPost job)
    {
        List<UserProfile> matchingUsers = userProfileRepository.findByPreferredLocationOrPrefersRemote(job.getLocation(), job.isRemote());

        if (matchingUsers.isEmpty())
        {
            System.out.println("No matching users found.");
            return;
        }

        List<JobPost> relevantJobs = jobPostRepository.findByLocationOrRemote(job.getLocation(), job.isRemote());

        if (relevantJobs.isEmpty())
        {
            System.out.println("No relevant jobs found.");
            return;
        }

        String jobMessage = relevantJobs.stream().map(JobPost::getPostProfile).collect(Collectors.joining(", "));

        String subject = "New Job Alert!";

        for (UserProfile user : matchingUsers)
        {   
            System.out.println("Initiating mail send");
            String notificationMessage = "Hey " +  user.getName() + " New jobs posted for role: " + jobMessage + " at your preferred location " + user.getPreferredLocation() + " grab it";
            sendEmailHanlder.sendEmail(user.getEmail(), subject, notificationMessage);
        }
    }
}