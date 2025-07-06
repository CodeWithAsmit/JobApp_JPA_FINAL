package com.asmit.JobApp.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_profiles")

public class UserProfile
{
    @Id
    @Column(name = "user_id")
    private int id;

    @OneToOne
    @MapsId 
    @JoinColumn(name = "user_id")
    private User user;

    private String email;
    private String name;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "user_skills", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "skill")
    private List<String> skills;

    private int experience;
    private int referralPoints;

    public void addReferralPoints(int points)
    {
        this.referralPoints += points;
    }

    private String preferredLocation;
    private boolean prefersRemote;
}