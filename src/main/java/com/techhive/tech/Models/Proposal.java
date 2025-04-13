package com.techhive.tech.Models;

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

    @Column(nullable = false)
    private String coverLetter;

    @Column(nullable = false)
    private Double proposalRate;

    @Column(nullable = false)
    private  Integer estimateDays;

    public Proposal(Job job, User freelancer, String coverLetter, Double proposalRate, Integer estimateDays) {
        this.job = job;
        this.freelancer = freelancer;
        this.coverLetter = coverLetter;
        this.proposalRate = proposalRate;
        this.estimateDays = estimateDays;
    }

}
