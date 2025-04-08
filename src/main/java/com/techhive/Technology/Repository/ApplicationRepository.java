package com.techhive.Technology.Repository;

import com.techhive.Technology.Models.Application;
import com.techhive.Technology.Models.Job;
import com.techhive.Technology.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ApplicationRepository extends JpaRepository<Application, Long> {
    Optional<Application> findByJobAndFreelancer(Job job, User freelancer);
}