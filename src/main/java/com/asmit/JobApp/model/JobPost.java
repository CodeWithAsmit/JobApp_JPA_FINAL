package com.asmit.JobApp.model;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.List;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Component
@Entity

/*@Document(collection = "SpringBootDB") --> Replace entity annotation with this if using MongoDB*/

public class JobPost
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "post_id")
	private int postId;
	
	private String postProfile;
	private String postDesc;
	private int reqExperience;

	@ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "job_tech_stack", joinColumns = @JoinColumn(name = "post_id"))
    @Column(name = "tech_stack")
    private List<String> postTechStack;

	@Transient
    private double similarityScore;

	private String location;  
    private boolean remote;
}