package com.techhive.Technology.Repository;

import com.techhive.Technology.Models.Job;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface JobRepository extends JpaRepository<Job, Integer> {

    Optional<Job> findById(Long id);
    Optional<Job> findByClientId(Integer clientId);
    List<Job> findByStatus(String status);
    boolean existsById(Long id);
}
