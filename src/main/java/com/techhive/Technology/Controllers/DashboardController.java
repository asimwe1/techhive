package com.techhive.Technology.Controllers;

import com.techhive.Technology.Models.Job;
import com.techhive.Technology.Models.Proposal;
import com.techhive.Technology.Models.User;
import com.techhive.Technology.Repository.JobRepository;
import com.techhive.Technology.Repository.ProposalRepository;
import com.techhive.Technology.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class DashboardController {

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private ProposalRepository proposalRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/stats")
    public ResponseEntity<?> getStats(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).body(Map.of("error", "Not authenticated"));
        }

        String username = authentication.getName();
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(404).body(Map.of("error", "User not found"));
        }

        User user = userOptional.get();
        Map<String, Object> stats = new HashMap<>();

        if (user.getRole().equals("ROLE_FREELANCER")) {
            List<Proposal> proposals = proposalRepository.findByFreelancer(user);
            List<Job> appliedJobs = jobRepository.findByProposalsFreelancer(user);
            stats.put("appliedOrPostedJobs", proposals.size());
            stats.put("completedOrActiveContracts", appliedJobs.stream().filter(j -> j.getStatus().equals("COMPLETED")).count());
            stats.put("totalEarnings", proposals.stream()
                    .filter(p -> p.getJob().getStatus().equals("COMPLETED"))
                    .mapToDouble(Proposal::getProposalRate)
                    .sum());
            stats.put("profileCompletionOrResponseRate", calculateProfileCompletion(user));
        } else {
            List<Job> postedJobs = jobRepository.findByClient(user);
            stats.put("appliedOrPostedJobs", postedJobs.size());
            stats.put("completedOrActiveContracts", postedJobs.stream().filter(j -> j.getStatus().equals("ACTIVE")).count());
            stats.put("totalEarnings", postedJobs.stream()
                    .filter(j -> j.getStatus().equals("COMPLETED"))
                    .mapToDouble(j -> j.getSalaryRangeUpper())
                    .sum());
            stats.put("profileCompletionOrResponseRate", calculateResponseRate(user));
        }

        return ResponseEntity.ok(stats);
    }

    @GetMapping("/activities")
    public ResponseEntity<?> getRecentActivities(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).body(Map.of("error", "Not authenticated"));
        }

        String username = authentication.getName();
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(404).body(Map.of("error", "User not found"));
        }

        User user = userOptional.get();
        List<Map<String, Object>> activities;
        if (user.getRole().equals("ROLE_FREELANCER")) {
            activities = proposalRepository.findByFreelancer(user).stream()
                    .map(p -> {
                        Map<String, Object> map = new HashMap<>();
                        map.put("id", p.getId());
                        map.put("type", "PROPOSAL_SUBMITTED");
                        map.put("description", p.getJob().getName());
                        map.put("timestamp", p.getJob().getCreatedAt().toString());
                        return map;
                    })
                    .limit(3)
                    .collect(Collectors.toList());
        } else {
            activities = jobRepository.findByClient(user).stream()
                    .map(j -> {
                        Map<String, Object> map = new HashMap<>();
                        map.put("id", j.getId());
                        map.put("type", "JOB_POSTED");
                        map.put("description", j.getName());
                        map.put("timestamp", j.getCreatedAt().toString());
                        return map;
                    })
                    .limit(3)
                    .collect(Collectors.toList());
        }

        return ResponseEntity.ok(activities);
    }

    @GetMapping("/proposals/freelancer")
    public ResponseEntity<?> getFreelancerProposals(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).body(Map.of("error", "Not authenticated"));
        }

        String username = authentication.getName();
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(404).body(Map.of("error", "User not found"));
        }

        User user = userOptional.get();
        List<Proposal> proposals = proposalRepository.findByFreelancer(user);
        return ResponseEntity.ok(proposals);
    }

    @GetMapping("/proposals/client")
    public ResponseEntity<?> getClientProposals(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).body(Map.of("error", "Not authenticated"));
        }

        String username = authentication.getName();
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(404).body(Map.of("error", "User not found"));
        }

        User user = userOptional.get();
        List<Proposal> proposals = proposalRepository.findByJobClient(user);
        return ResponseEntity.ok(proposals);
    }

    private int calculateProfileCompletion(User user) {
        int completion = 0;
        if (user.getUsername() != null) completion += 25;
        if (user.getEmail() != null) completion += 25;
//        if (user.getSkills() != null && !user.getSkills().isEmpty()) completion += 50;
        return completion;
    }

    private int calculateResponseRate(User user) {
        return 85; // Replace with actual logic
    }
}