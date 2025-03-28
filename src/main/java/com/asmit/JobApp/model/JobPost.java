package com.asmit.JobApp.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.List;
import jakarta.persistence.ElementCollection;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Component
@Entity

public class JobPost
{
	@Id
	private int postId;
	private String postProfile;
	private String postDesc;
	private int reqExperience;

	@ElementCollection
	private List<String> postTechStack;
}