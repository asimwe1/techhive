package com.techhive.Technology.DTO;

import lombok.Data;

import java.util.List;

@Data
public class ProfileDTO {
    private String firstName;
    private String lastName;
    private String email;
    private String username;
    private String bio;
    private String location;
    private String website;
    private List<String> skills;
    private String avatar;
    private String role;
    private String memberSince;
    private int completedJobs;
    private double rating;
    private double hourlyRate;
}
