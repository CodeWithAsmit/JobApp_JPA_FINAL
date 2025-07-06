package com.asmit.JobApp.service;

import java.util.List;
import java.util.Set;
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
        List<UserProfile> allUsers = userProfileRepository.findByPreferredLocationOrPrefersRemote(job.getLocation(), job.isRemote());

        List<UserProfile> matchingUsers = allUsers.stream()
            .filter(user -> {
                Set<String> userSkills = user.getSkills().stream()
                    .map(String::toLowerCase)
                    .collect(Collectors.toSet());

                List<String> jobSkills = job.getPostTechStack().stream()
                    .map(String::toLowerCase)
                    .collect(Collectors.toList());

                long matchCount = jobSkills.stream()
                    .filter(userSkills::contains)
                    .count();

                double matchRatio = (double) matchCount / jobSkills.size();

                return matchRatio >= 0.5 && user.getExperience() >= job.getReqExperience();
            })
            .collect(Collectors.toList());

        if (matchingUsers.isEmpty())
        {
            System.out.println("No matching users found.");
            return;
        }

        String subject = "New Job Alert!";

        for (UserProfile user : matchingUsers)
        {
            System.out.println("Initiating mail send to: " + user.getEmail());
            String notificationMessage = getJobNotificationHtml(user.getName(), job);
            sendEmailHanlder.sendEmail(user.getEmail(), subject, notificationMessage);
        }
    }

    private String getJobNotificationHtml(String userName, JobPost job)
    {
        String jobLink = "http://localhost:5137/jobs/" + job.getPostId();

        String skills = job.getPostTechStack().stream()
            .map(skill -> "<li>" + skill + "</li>")
            .collect(Collectors.joining());

        return String.format("""
            <html>
            <body style="font-family: Arial, sans-serif; background-color: #f4f4f4; padding: 20px;">
                <div style="max-width: 600px; margin: auto; background: #fff; padding: 20px; border-radius: 10px;">
                    <h2 style="color: #2E86C1;">Hello %s,</h2>
                    <p>We found a new job that matches your skills!</p>

                    <h3 style="color: #1A5276;">%s</h3>
                    <p><strong>Location:</strong> %s (%s)</p>
                    <p><strong>Experience Required:</strong> %d+ years</p>

                    <p><strong>Required Skills:</strong></p>
                    <ul>%s</ul>

                    <div style="text-align: center; margin-top: 30px;">
                        <a href="%s" style="background-color: #28a745; color: white; padding: 12px 24px; text-decoration: none; border-radius: 5px;">
                            View Job Details
                        </a>
                    </div>

                    <p style="margin-top: 40px; font-size: 0.9em; color: #888;">
                        You're receiving this email because you opted in for job alerts.
                    </p>
                </div>
            </body>
            </html>
            """,
            userName,
            job.getPostProfile(),
            job.getLocation(),
            job.isRemote() ? "Remote available" : "On-site",
            job.getReqExperience(),
            skills,
            jobLink
        );
    }
}