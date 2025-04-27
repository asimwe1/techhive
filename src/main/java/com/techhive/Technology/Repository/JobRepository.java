package com.techhive.Technology.Repository;

import com.techhive.Technology.Models.Job;
import com.techhive.Technology.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface JobRepository extends JpaRepository<Job, Integer> {
    List<Job> findByClient(User client);
    List<Job> findByProposalsFreelancer(User freelancer);
    Optional<Job> findById(Long id);
    Optional<Job> findByClientId(Integer clientId);
    List<Job> findByStatus(String status);
    boolean existsById(Long id);
    Optional<Job> findByNameAndClient(String name, User client);


}
