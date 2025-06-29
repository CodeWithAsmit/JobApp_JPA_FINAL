package com.asmit.JobApp.service;

import org.apache.tika.Tika;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.asmit.JobApp.exception.InvalidFileException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service

public class ResumeParserService
{
    private final Tika tika;

    public ResumeParserService()
    {
        System.out.println("ResumeParserService constructor called");
        this.tika = new Tika();
    }

    public String parseResume(MultipartFile file) throws Exception
    {
        try
        {
            return tika.parseToString(file.getInputStream());
        }
        catch (Exception e)
        {
            throw new InvalidFileException("Failed to parse resume: " + e.getMessage());
        }
    }

    public String extractSkills(String parsedText)
    {
        List<String> technologies = List.of(
            "Java", "Spring Boot", "Hibernate", "React", "Redux", "JavaScript",
            "Angular", "TypeScript", "RxJS", "Python", "Django", "Flask",
            "Node.js", "PHP", "HTML", "SQL", "CSS", "Bootstrap", "Express.js",
            "Git", "GitHub", "Rest API", "Data Structure", "Algorithm");
        
        String regex = "\\b(" + technologies.stream().map(Pattern::quote).collect(Collectors.joining("|")) + ")\\b";
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(parsedText);
        StringBuilder skills = new StringBuilder();

        while (matcher.find())
        {
            if (skills.length() > 0)
            {
                skills.append(",");
            }
            skills.append(matcher.group());
        }
        return skills.toString();
    }

    public int extractExperience(String parsedText)
    {
        Pattern pattern = Pattern.compile("(\\d+(\\.\\d+)?)\\s*(years?|yrs?|y)\\s*(of)?\\s*(experience)?", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(parsedText);

        int experience = 0;
        while (matcher.find())
        {
            experience = (int) Double.parseDouble(matcher.group(1));
            break;
        }
        return experience;
    }
}