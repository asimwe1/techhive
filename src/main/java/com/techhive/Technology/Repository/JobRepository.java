package com.techhive.Technology.Repository;

import com.techhive.Technology.Models.Job;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JobRepository extends JpaRepository<Job, Integer> {
    @Override
    Optional<Job> findById(Integer id);
    Optional<Job> findByClientId(Integer clientId);
}
