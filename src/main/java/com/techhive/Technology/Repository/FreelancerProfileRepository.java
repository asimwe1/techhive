package com.techhive.Technology.Repository;

import com.techhive.Technology.Models.FreelancerProfile;
import com.techhive.Technology.Models.User;
import com.techhive.Technology.Models.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FreelancerProfileRepository extends JpaRepository<FreelancerProfile, Long> {
    Optional<FreelancerProfile> findByUserProfile(UserProfile userProfile);
}
