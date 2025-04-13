package com.techhive.tech.Repository;

import com.techhive.tech.Models.Job;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JobRepository extends JpaRepository<Job, Integer> {

    Optional<Job> findById(Long id);
    Optional<Job> findByClientId(Integer clientId);
    boolean existsById(Long id);
}
