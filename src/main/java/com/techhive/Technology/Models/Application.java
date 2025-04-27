package com.techhive.Technology.Models;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
public class Application {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "job_id")
    private Job job;

    @ManyToOne
    @JoinColumn(name = "freelancer_id")
    private User freelancer;

    private String status; // e.g., "PENDING", "ACCEPTED", "REJECTED"
    private Double bidAmount;
    private LocalDateTime appliedAt;
}