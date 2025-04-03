package com.techhive.Technology.Services;

import com.techhive.Technology.Models.FreelancerProfile;
import com.techhive.Technology.DTO.ProfileDTO;
import com.techhive.Technology.Models.User;
import com.techhive.Technology.Models.UserProfile;
import com.techhive.Technology.Repository.FreelancerProfileRepository;
import com.techhive.Technology.Repository.UserProfileRepository;
import com.techhive.Technology.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import java.time.format.DateTimeFormatter;
import java.util.Collections;

@Service
public class ProfileService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserProfileRepository userProfileRepository;

    @Autowired
    private FreelancerProfileRepository freelancerProfileRepository;

    public ProfileDTO getProfile() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        UserProfile userProfile = userProfileRepository.findByUser(user)
                .orElse(new UserProfile());

        ProfileDTO profileDTO = new ProfileDTO();
        profileDTO.setFirstName(user.getFirstName());
        profileDTO.setLastName(user.getLastName());
        profileDTO.setEmail(user.getEmail());
        profileDTO.setUsername(user.getUsername());
        profileDTO.setRole(user.getRole().name());
        profileDTO.setMemberSince(user.getCreated_at().format(DateTimeFormatter.ofPattern("MMMM yyyy")));
        profileDTO.setBio(userProfile.getBio());
        profileDTO.setLocation(userProfile.getLocation());
        profileDTO.setWebsite(userProfile.getWebsite());
        profileDTO.setAvatar(userProfile.getAvatar());

        if (user.getRole().toString().equals("FREELANCER")) {
            FreelancerProfile freelancerProfile = freelancerProfileRepository.findByUserProfileWithSkills(userProfile)
                    .orElse(new FreelancerProfile());
            profileDTO.setSkills(freelancerProfile.getSkills() != null ? freelancerProfile.getSkills() : Collections.emptyList());
            profileDTO.setCompletedJobs(freelancerProfile.getCompletedJobs());
            profileDTO.setRating(freelancerProfile.getRating());
            profileDTO.setHourlyRate(freelancerProfile.getHourlyRate());
        }

        return profileDTO;
    }

    public ProfileDTO updateProfile(ProfileDTO profileDTO) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        user.setFirstName(profileDTO.getFirstName());
        user.setLastName(profileDTO.getLastName());
        user.setEmail(profileDTO.getEmail());
        userRepository.save(user);

        UserProfile userProfile = userProfileRepository.findByUser(user)
                .orElse(new UserProfile());
        userProfile.setUser(user);
        userProfile.setBio(profileDTO.getBio());
        userProfile.setLocation(profileDTO.getLocation());
        userProfile.setWebsite(profileDTO.getWebsite());
        userProfileRepository.save(userProfile);

        if (user.getRole().toString().equals("FREELANCER")) {
            FreelancerProfile freelancerProfile = freelancerProfileRepository.findByUserProfile(userProfile)
                    .orElse(new FreelancerProfile());
            freelancerProfile.setUserProfile(userProfile);
            freelancerProfile.setSkills(profileDTO.getSkills());
            freelancerProfileRepository.save(freelancerProfile);
        }

        return getProfile();
    }
}