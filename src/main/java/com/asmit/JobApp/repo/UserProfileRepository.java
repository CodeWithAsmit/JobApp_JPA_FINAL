package com.asmit.JobApp.repo;

import com.asmit.JobApp.model.UserProfile;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, Integer>
{
    @Query(value = "SELECT skill FROM user_skills WHERE id = :userId", nativeQuery = true)
    List<String> findSkillsByUserId(@Param("userId") int userId);

    @Query("SELECT u FROM UserProfile u WHERE u.preferredLocation = :location OR u.prefersRemote = :remote")
    List<UserProfile> findByPreferredLocationOrPrefersRemote(@Param("location") String location, @Param("remote") boolean remote);
}
