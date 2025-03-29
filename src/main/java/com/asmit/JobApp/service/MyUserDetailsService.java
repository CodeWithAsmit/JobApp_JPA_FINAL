package com.asmit.JobApp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.asmit.JobApp.repo.UserRepo;
import com.asmit.JobApp.model.User;
import com.asmit.JobApp.model.UserPrincipal;

@Service
public class MyUserDetailsService implements UserDetailsService
{
    @Autowired
	private UserRepo repo;

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
}
