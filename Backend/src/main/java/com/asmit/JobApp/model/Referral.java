package com.asmit.JobApp.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "referrals")

public class Referral
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int referrerUserId;
    private Integer referredUserId;
    private int jobId;
    private boolean isSuccessful;
    private LocalDateTime referralDate;

    public Referral()
    {
        this.referralDate = LocalDateTime.now();
        this.isSuccessful = false;
    }
}
