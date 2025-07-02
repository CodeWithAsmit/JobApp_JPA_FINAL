package com.asmit.JobApp.repo;

import com.asmit.JobApp.model.Referral;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ReferralRepository extends JpaRepository<Referral, Integer>
{
    List<Referral> findByReferrerUserId(int referrerUserId);
    List<Referral> findByJobId(int jobId);
    List<Referral> findByReferrerUserIdAndIsSuccessfulTrue(int referrerUserId);
}