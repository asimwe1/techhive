package com.techhive.Technology.Controllers;

import com.techhive.Technology.Models.Proposal;
import com.techhive.Technology.Models.Role;
import com.techhive.Technology.Models.User;
import com.techhive.Technology.Repository.JobRepository;
import com.techhive.Technology.Repository.ProposalRepository;
import com.techhive.Technology.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/jobs")
public class ProposalController {

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private ProposalRepository proposalRepository;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/{jobId}/proposals")
    public ResponseEntity<?> submitProposal(@PathVariable Integer jobId, @RequestBody Proposal proposal) {
        System.out.println("Submitting proposal for jobId: " + jobId);
        System.out.println("Proposal data: coverLetter=" + proposal.getCoverLetter() +
                ", proposedRate=" + proposal.getProposalRate() +
                ", estimatedDays=" + proposal.getEstimatedDays());

        return jobRepository.findById(jobId).map(job -> {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            User freelancer = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User not found: " + username));
            System.out.println("Freelancer: " + username + ", Role: " + freelancer.getRole());

            if (freelancer.getRole() != Role.FREELANCER) {
                System.out.println("User " + username + " is not a FREELANCER");
                return ResponseEntity.status(403).body(Map.of("error", "Only freelancers can submit proposals"));
            }

            if (proposal.getEstimatedDays() == null) {
                System.out.println("Estimated days is null");
                return ResponseEntity.badRequest().body(Map.of("error", "Estimated days is required"));
            }

            proposal.setJob(job);
            proposal.setFreelancer(freelancer);
            proposalRepository.save(proposal);
            System.out.println("Proposal saved for jobId: " + jobId + ", freelancer: " + username);
            return ResponseEntity.ok(Map.of(
                    "message", "Proposal submitted successfully",
                    "proposalId", proposal.getId(),
                    "jobId", jobId,
                    "freelancerUsername", username,
                    "proposedRate", proposal.getProposalRate()
            ));
        }).orElseGet(() -> {
            System.out.println("Job not found: " + jobId);
            return ResponseEntity.badRequest().body(Map.of("error", "Job not found"));
        });
    }
}