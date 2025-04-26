package com.asmit.JobApp.controller;

import com.asmit.JobApp.model.Referral;
import com.asmit.JobApp.service.ReferralService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin

public class ReferralController
{
    @Autowired
    private ReferralService referralService;

    @PutMapping("/markSuccessful/{referralId}")
    public String markReferralAsSuccessful(@PathVariable int referralId)
    {
        boolean valueReturned = referralService.markReferralAsSuccessful(referralId);
        if (valueReturned == true)
        {
            return "Referral is marked Successfully!";
        }
        else
        {
            return "Failed to mark referral as success";
        }
    }

    @PostMapping("/generate/{userId}/{jobId}")
    public String generateReferralLink(@PathVariable int userId, @PathVariable int jobId)
    {
        return referralService.generateReferralLink(userId, jobId);
    }

    @GetMapping("/track/{referralId}")
    public Referral trackReferral(@PathVariable int referralId)
    {
        return referralService.getReferralById(referralId);
    }
}