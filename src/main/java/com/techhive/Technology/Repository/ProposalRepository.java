package com.techhive.Technology.Repository;

import com.techhive.Technology.Models.Proposal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProposalRepository extends JpaRepository<Proposal, Long> {
    List<Proposal> findByJobId(Long jobId);
}
