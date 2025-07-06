package com.asmit.JobApp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.asmit.JobApp.model.User;
import com.asmit.JobApp.repo.UserRepo;

import jakarta.persistence.EntityNotFoundException;

import com.asmit.JobApp.model.UserProfile;
import com.asmit.JobApp.repo.UserProfileRepository;
import java.util.ArrayList;
import java.util.Optional;

@Service
public class UserService
{
    @Autowired
	private UserRepo repo;

	@Autowired
    private UserProfileRepository userProfileRepository;

    private BCryptPasswordEncoder encoder=new BCryptPasswordEncoder(12);

	public User saveUser(User user)
    {
		user.setPassword(encoder.encode(user.getPassword()));
	    User savedUser = repo.save(user);

		UserProfile newProfile = new UserProfile();

        newProfile.setUser(savedUser);
        newProfile.setEmail(""); 
        newProfile.setName(savedUser.getUsername()); 
        newProfile.setSkills(new ArrayList<>()); 
        newProfile.setExperience(0); 
        newProfile.setReferralPoints(0); 
        newProfile.setPreferredLocation(""); 
        newProfile.setPrefersRemote(false); 
        userProfileRepository.save(newProfile);
        return savedUser;
	}

	public UserProfile addUserProfile(UserProfile userProfile)
    {
        User user = repo.findById(userProfile.getId()).orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + userProfile.getId()));
        userProfile.setUser(user);
        return userProfileRepository.save(userProfile);
    }

	public User loadOrCreateOAuthUser(String email)
	{
		return repo.findByUsername(email).orElseGet(() -> {
			User newUser = new User();
			newUser.setUsername(email);
			newUser.setPassword("OAUTH_USER");

			User savedOAuthUser = repo.save(newUser);
			UserProfile newProfile = new UserProfile();
            
            newProfile.setUser(savedOAuthUser);
            newProfile.setEmail(email);
            newProfile.setName(savedOAuthUser.getUsername());
            newProfile.setSkills(new ArrayList<>());
            newProfile.setExperience(0);
            newProfile.setReferralPoints(0);
            newProfile.setPreferredLocation("");
            newProfile.setPrefersRemote(false);
            userProfileRepository.save(newProfile);
            return savedOAuthUser;
		});
	}

	public Optional<User> findByUsername(String username)
	{
        return repo.findByUsername(username);
    }
}