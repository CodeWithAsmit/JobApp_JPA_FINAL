package com.asmit.JobApp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.asmit.JobApp.repo.UserProfileRepository;
import com.asmit.JobApp.repo.UserRepo;

import jakarta.persistence.EntityNotFoundException;

import com.asmit.JobApp.model.User;
import com.asmit.JobApp.model.UserPrincipal;
import com.asmit.JobApp.model.UserProfile;

@Service
public class MyUserDetailsService implements UserDetailsService
{
    @Autowired
	private UserRepo repo;

    @Autowired
    private UserProfileRepository userRepo;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
    {
        User user= repo.findByUsername(username);
        
        if (user==null)
        {
            throw new UsernameNotFoundException("User 404");
        }
        else
        {
            return new UserPrincipal(user);
        }
	}

    public UserProfile getUserById(int id)
    {
        return userRepo.findById(id).orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));
    }
}
