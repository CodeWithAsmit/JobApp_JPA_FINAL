package com.asmit.JobApp.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Entity
@Getter
@Setter

public class UserProfile
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    private String email;
    private String name;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "user_skills", joinColumns = @JoinColumn(name = "id"))
    @Column(name = "skill")
    private List<String> skills;

    private int experience;
}
