package com.asmit.JobApp.service;

import com.asmit.JobApp.model.Referral;
import com.asmit.JobApp.repo.JobPostRepository;
import com.asmit.JobApp.repo.ReferralRepository;
import com.asmit.JobApp.repo.UserProfileRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReferralService
{
    @Autowired
    private ReferralRepository referralRepository;

    @Autowired
    public JobPostRepository repo;

    @Autowired
    UserProfileRepository userProfileRepository;

    public boolean markReferralAsSuccessful(int referralId)
    {
        Referral referral = referralRepository.findById(referralId).orElse(null);
        if (referral != null && !referral.isSuccessful()) 
        {
            referral.setSuccessful(true);
            referralRepository.save(referral);
            return true;
        }
        return false;
    }

    public String generateReferralLink(int userId, int jobId)
    {
        boolean jobExists = repo.existsById(jobId);
        boolean userExists = userProfileRepository.existsById(userId);
    
        if (!jobExists)
        {
            return "Error: Job ID " + jobId + " does not exist.";
        }
        if (!userExists)
        {
            return "Error: User ID " + userId + " does not exist.";
        }

        Referral referral = new Referral();
        referral.setReferrerUserId(userId);
        referral.setJobId(jobId);
        referral.setReferredUserId(0);
        Referral savedReferral = referralRepository.save(referral);
        
        return "Your Referral ID is " + savedReferral.getId() + ", save it for future reference";
    }

    public Referral getReferralById(Integer referralId)
    {
        return referralRepository.findById(referralId).orElse(null);
    }

    public void trackJobApplication(int referralId, int referredUserId)
    {
        Referral referral = referralRepository.findById(referralId).orElse(null);
        
        if (referral != null && referral.getReferredUserId() == 0)
        {
            referral.setReferredUserId(referredUserId);
            referralRepository.save(referral);
        }
    }
}