package com.techhive.Technology.Controllers;

import com.techhive.Technology.Models.Job;
import com.techhive.Technology.Repository.JobRepository;
import com.techhive.Technology.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/recommendations")
public class RecommendationController {

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/jobs")
    public ResponseEntity<List<Job>> getRecommendedJobs() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        // Basic recommendation: return all open jobs (enhance later with AI logic)
        List<Job> jobs = jobRepository.findByStatus("OPEN");
        System.out.println("Returning " + jobs.size() + " recommended jobs for user: " + username);
        return ResponseEntity.ok(jobs);
    }
}