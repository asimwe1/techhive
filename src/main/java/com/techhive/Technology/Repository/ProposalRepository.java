package com.techhive.Technology.Repository;

import com.techhive.Technology.Models.Proposal;
import com.techhive.Technology.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProposalRepository extends JpaRepository<Proposal, Long> {
    List<Proposal> findByJobId(Long jobId);
    List<Proposal> findByFreelancer(User freelancer);
    List<Proposal> findByJobClient(User client);
}
