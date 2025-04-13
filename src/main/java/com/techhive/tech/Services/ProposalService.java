package com.techhive.tech.Services;

import com.techhive.tech.Models.Proposal;
import com.techhive.tech.Models.User;
import com.techhive.tech.Repository.JobRepository;
import com.techhive.tech.Repository.ProposalRepository;
import com.techhive.tech.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProposalService {

    @Autowired
    private ProposalRepository proposalRepository;

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private UserRepository userRepository;

    public Proposal submitProposal(Proposal proposal, Long jobId) {
        //get authenticated user
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User freelancer = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        //ensure user id freelancer
        if(!freelancer.getRole().equals("FREELANCER")) {
            throw new RuntimeException("Only Freelancers can submit a proposal");
        }

        //validate job exists
        proposal.setJob(jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not Found")));

        //set freelancer
        proposal.setFreelancer(freelancer);

        return proposalRepository.save(proposal);
    }

    public List<Proposal> getProposalsByJob(Long jobId) {
        if (!jobRepository.existsById(jobId)) {
            throw new RuntimeException("job not Found");
        }
        return proposalRepository.findByJobId(jobId);
    }
}
