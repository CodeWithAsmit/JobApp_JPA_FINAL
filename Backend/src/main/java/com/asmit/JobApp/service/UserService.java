package com.asmit.JobApp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.asmit.JobApp.model.User;
import com.asmit.JobApp.repo.UserRepo;
import com.asmit.JobApp.model.UserProfile;
import com.asmit.JobApp.repo.UserProfileRepository;

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
	    return repo.save(user) ;
	}

	public UserProfile addUserProfile(UserProfile userProfile)
	{
        return userProfileRepository.save(userProfile);
    }

	public User loadOrCreateOAuthUser(String email)
	{
		return repo.findByUsername(email).orElseGet(() -> {
			User newUser = new User();
			newUser.setUsername(email);
			newUser.setPassword("OAUTH_USER");
			return repo.save(newUser);
		});
	}
}
