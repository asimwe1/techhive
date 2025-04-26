package com.techhive.Technology.Models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "proposals")
@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class Proposal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "job_id", nullable = false)
    private Job job;

    @ManyToOne
    @JoinColumn(name = "freelancer_id", nullable = false)
    private User freelancer;

    @Column(name = "cover_letter", nullable = false)
    private String coverLetter;

    @Column(name = "proposed_rate", nullable = false)
    private Double proposalRate;

    @Column(name = "estimated_days")
    private  Integer estimatedDays;

    public Proposal(Job job, User freelancer, String coverLetter, Double proposalRate, Integer estimatedDays) {
        this.job = job;
        this.freelancer = freelancer;
        this.coverLetter = coverLetter;
        this.proposalRate = proposalRate;
        this.estimatedDays = estimatedDays;
    }

}
