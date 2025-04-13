package com.techhive.tech.Repository;

import com.techhive.tech.Models.Proposal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProposalRepository extends JpaRepository<Proposal, Long> {
    List<Proposal> findByJobId(Long jobId);
}
