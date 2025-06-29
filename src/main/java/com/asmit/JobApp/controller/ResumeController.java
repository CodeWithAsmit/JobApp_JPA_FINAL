package com.asmit.JobApp.controller;

import com.asmit.JobApp.service.ResumeService;
import com.asmit.JobApp.exception.InvalidFileException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@CrossOrigin

public class ResumeController 
{
    @Autowired
    private ResumeService resumeService;
    
    @PostMapping("/upload/{userId}")
    public ResponseEntity<String> uploadResume(@PathVariable int userId, @RequestParam("File") MultipartFile file)
    {
        try
        {
            resumeService.processResume(userId, file);
            return ResponseEntity.ok("Resume uploaded and processed successfully.");
        }
        catch (InvalidFileException e)
        {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        catch (Exception e)
        {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to process resume: " + e.getMessage());
        }
    }
}