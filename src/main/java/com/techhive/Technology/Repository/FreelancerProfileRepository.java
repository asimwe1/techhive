package com.techhive.Technology.Repository;

import com.techhive.Technology.Models.FreelancerProfile;
import com.techhive.Technology.Models.User;
import com.techhive.Technology.Models.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface FreelancerProfileRepository extends JpaRepository<FreelancerProfile, Long> {
    Optional<FreelancerProfile> findByUserProfile(UserProfile userProfile);

    @Query("SELECT fp FROM FreelancerProfile fp JOIN FETCH fp.skills WHERE fp.userProfile = :userProfile")
    Optional<FreelancerProfile> findByUserProfileWithSkills(@Param("userProfile") UserProfile userProfile);
}
