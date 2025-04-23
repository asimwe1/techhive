package com.techhive.Technology.Controllers;

import com.techhive.Technology.Models.Proposal;
import com.techhive.Technology.Services.ProposalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/jobs")
public class ProposalController {

    @Autowired
    private ProposalService proposalService;

    @PostMapping("/{jobId}/proposals")
    @PreAuthorize("hasRole('FREELANCER')")
    public ResponseEntity <Proposal> submitProposal(@PathVariable Long jobId, @RequestBody Proposal proposal) {
        Proposal savedProposal = proposalService.submitProposal(proposal, jobId);
        return ResponseEntity.ok(savedProposal);
    }

    @GetMapping("/{jobId}/proposals")
    public ResponseEntity<List<Proposal>> getProposals(@PathVariable Long jobId){
        List<Proposal> proposals = proposalService.getProposalsByJob(jobId);
        return ResponseEntity.ok(proposals);
    }
}
