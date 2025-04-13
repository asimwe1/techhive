package com.techhive.Technology.Services;

import com.techhive.Technology.Models.Proposal;
import com.techhive.Technology.Models.User;
import com.techhive.Technology.Repository.JobRepository;
import com.techhive.Technology.Repository.ProposalRepository;
import com.techhive.Technology.Repository.UserRepository;
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
