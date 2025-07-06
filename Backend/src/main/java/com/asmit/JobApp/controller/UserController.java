package com.asmit.JobApp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import java.util.Optional;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.asmit.JobApp.model.User;
import com.asmit.JobApp.model.UserProfile;
import com.asmit.JobApp.service.UserService;
import com.asmit.JobApp.service.JwtService;
import com.asmit.JobApp.service.MyUserDetailsService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.Data;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.security.core.userdetails.UserDetails;

@RestController
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")

public class UserController
{
	@Autowired
	private UserService service;

	@Autowired
	private JwtService jwtService;

	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	MyUserDetailsService userProfile;

	@Data
	@AllArgsConstructor
	public static class UserInfoResponse
	{
		private String username;
		private Integer userId;
	}
	
	@PostMapping("register")
	public User register(@RequestBody User user)
    {
	  return service.saveUser(user);
	}

	@PostMapping("login")
	public ResponseEntity<?> login(@RequestBody User user)
    {
		Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(),user.getPassword()));

		if(authentication.isAuthenticated())
		{
			String token = jwtService.generateToken(user.getUsername());
        	
			ResponseCookie cookie = ResponseCookie.from("token", token)
                .httpOnly(true) 
                .secure(false)   
                .path("/")
                .maxAge(3600)
                .build();

        	return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .build();
        }
		else
		{
            return ResponseEntity.status(401).build();
        }
	}

	@PostMapping("logout")
	public ResponseEntity<?> logout(HttpServletResponse response)
	{
		ResponseCookie expiredCookie = ResponseCookie.from("token", "")
				.httpOnly(true)
				.secure(false)
				.path("/")
				.maxAge(0)
				.build();

		return ResponseEntity.ok()
				.header(HttpHeaders.SET_COOKIE, expiredCookie.toString())
				.body("Logged out successfully");
	}

	@PostMapping("addUserProfile")
    public UserProfile addUserProfile(@RequestBody UserProfile userProfile)
	{
        return service.addUserProfile(userProfile);
    }

	@GetMapping("/userProfile/{id}")
	public UserProfile getUserProfileById(@PathVariable int id)
	{
		return userProfile.getUserById(id);
	}

	@GetMapping("/auth/status")
    public ResponseEntity<?> getAuthStatus()
	{
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated() && authentication.getPrincipal() instanceof UserDetails && !((UserDetails) authentication.getPrincipal()).getUsername().equals("anonymousUser"))
		{
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String username = userDetails.getUsername();

            Integer userId = null;
            try
			{
                Optional<User> userOptional = service.findByUsername(username);
                
				if (userOptional.isPresent())
				{
                    User user = userOptional.get();
                    userId = user.getId();
                }
            }
			catch (Exception e)
			{
                System.err.println("Error retrieving userId for user " + username + ": " + e.getMessage());
            }

            return ResponseEntity.ok(new UserInfoResponse(username, userId));
        }
		else
		{
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated or session invalid.");
        }
    }
}