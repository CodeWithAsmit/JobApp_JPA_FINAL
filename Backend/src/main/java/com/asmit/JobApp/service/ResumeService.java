package com.asmit.JobApp.service;

import com.asmit.JobApp.model.UserProfile;
import com.asmit.JobApp.exception.InvalidFileException;
import com.asmit.JobApp.repo.UserProfileRepository;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service

public class ResumeService
{
    @Autowired
    private ResumeParserService resumeParserService;

    @Autowired
    private UserProfileRepository userProfileRepository;

    public void processResume(int userId, MultipartFile file)
    {
        if (file.isEmpty())
        {
            throw new InvalidFileException("Uploaded file is empty.");
        }

        if (file.getSize() > 500 * 1024)
        { 
            throw new InvalidFileException("File size exceeds the 500KB limit.");
        }

        try
        {
            String parsedText = resumeParserService.parseResume(file);

            String extractedSkills = resumeParserService.extractSkills(parsedText);
            List<String> skills = (extractedSkills != null && !extractedSkills.isBlank()) ? Arrays.stream(extractedSkills.split(",\\s*")).filter(skill -> !skill.isBlank()).collect(Collectors.toList()): new ArrayList<>();
            int experience = resumeParserService.extractExperience(parsedText);

            UserProfile userProfile = userProfileRepository.findById(userId).orElseThrow(() -> new InvalidFileException("User profile not found."));

            userProfile.setSkills(skills);
            userProfile.setExperience(experience);
            userProfileRepository.save(userProfile);

        }
        catch (Exception e)
        {
            throw new InvalidFileException("Failed to process resume: " + e.getMessage());
        }
    }
}