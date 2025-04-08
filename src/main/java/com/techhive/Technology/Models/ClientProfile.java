package com.techhive.Technology.Models;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class ClientProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_profile_id")
    private UserProfile userProfile;

    private double budget;
    private String companyDetails;
}
