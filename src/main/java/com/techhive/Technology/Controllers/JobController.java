package com.techhive.Technology.Controllers;

import com.techhive.Technology.Models.Job;
import com.techhive.Technology.Models.JobCategory;
import com.techhive.Technology.DTO.JobDTO;
import com.techhive.Technology.Models.User;
import com.techhive.Technology.Repository.JobCategoryRepository;
import com.techhive.Technology.Repository.JobRepository;
import com.techhive.Technology.Repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/jobs")
public class JobController {

    private static final Logger logger = LoggerFactory.getLogger(JobController.class);

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private JobCategoryRepository jobCategoryRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public ResponseEntity<List<JobDTO>> getJobs() {
        List<JobDTO> jobs = jobRepository.findAll().stream()
                .map(job -> {
                    JobDTO dto = new JobDTO();
                    dto.setId(job.getId());
                    dto.setName(job.getName());
                    dto.setClientId(job.getClient().getId());
                    dto.setClientUsername(job.getClient().getUsername());
                    dto.setCategoryId(job.getCategory().getCategoryId());
                    dto.setCategoryName(job.getCategory().getCategory());
                    dto.setSalaryRangeLower(job.getSalaryRangeLower());
                    dto.setSalaryRangeUpper(job.getSalaryRangeUpper());
                    dto.setStatus(job.getStatus());
                    dto.setSkills(job.getSkills());
                    dto.setCreatedAt(job.getCreatedAt());
                    return dto;
                })
                .collect(Collectors.toList());
        System.out.println("Fetched jobs: " + jobs.size());
        return ResponseEntity.ok(jobs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getJobById(@PathVariable Integer id) {
        Optional<Job> jobOptional = jobRepository.findById(id);
        if (jobOptional.isEmpty()) {
            return ResponseEntity.status(404).body("Job not found");
        }
        JobDTO jobDTO = mapToJobDTO(jobOptional.get());
        System.out.println("Fetched job: " + jobDTO.getName());
        return ResponseEntity.ok(jobDTO);
    }

    private JobDTO mapToJobDTO(Job job) {
        JobDTO dto = new JobDTO();
        dto.setId(job.getId());
        dto.setName(job.getName());
        dto.setDescription(job.getDescription());
        dto.setClientId(job.getClient().getId());
        dto.setClientUsername(job.getClient().getUsername());
        dto.setCategoryId(job.getCategory().getCategoryId());
        dto.setCategoryName(job.getCategory().getCategory());
        dto.setSalaryRangeLower(job.getSalaryRangeLower());
        dto.setSalaryRangeUpper(job.getSalaryRangeUpper());
        dto.setStatus(job.getStatus());
        dto.setSkills(job.getSkills());
        dto.setCreatedAt(job.getCreatedAt());
        return dto;
    }

        @PostMapping
        @Transactional
        public ResponseEntity<Map<String, String>> createJob(@Valid @RequestBody JobDTO jobDTO) {
            logger.info("Received POST /api/jobs with JobDTO: {}", jobDTO);

            try {
                // Get authenticated user
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal().equals("anonymousUser")) {
                    logger.error("Unauthorized: No authenticated user");
                    Map<String, String> response = new HashMap<>();
                    response.put("message", "Unauthorized: Please log in as a client");
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
                }

                String username = authentication.getName();
                logger.debug("Authenticated username: {}", username);

                User client = userRepository.findByUsername(username)
                        .orElseThrow(() -> {
                            logger.error("Client not found: {}", username);
                            return new RuntimeException("Client not found: " + username);
                        });
                logger.debug("Found client: {}", client);

                // Verify user is a client
                if (!client.getRole().toString().equals("CLIENT")) {
                    logger.error("Forbidden: User {} is not a client, role: {}", username, client.getRole());
                    Map<String, String> response = new HashMap<>();
                    response.put("message", "Forbidden: Only clients can post jobs");
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
                }

                // Validate category
                logger.debug("Validating categoryId: {}", jobDTO.getCategoryId());
                JobCategory category = jobCategoryRepository.findById(jobDTO.getCategoryId())
                        .orElseThrow(() -> {
                            logger.error("Invalid category ID: {}", jobDTO.getCategoryId());
                            return new RuntimeException("Invalid category ID: " + jobDTO.getCategoryId());
                        });
                logger.debug("Found category: {}", category);

                // Validate salary range
                if (jobDTO.getSalaryRangeLower() >= jobDTO.getSalaryRangeUpper()) {
                    logger.error("Invalid salary range: lower={} >= upper={}", jobDTO.getSalaryRangeLower(), jobDTO.getSalaryRangeUpper());
                    Map<String, String> response = new HashMap<>();
                    response.put("message", "Maximum salary must be greater than minimum salary");
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
                }

                // Validate skills
                if (jobDTO.getSkills() == null || jobDTO.getSkills().isEmpty()) {
                    logger.error("No skills provided");
                    Map<String, String> response = new HashMap<>();
                    response.put("message", "At least one skill is required");
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
                }

                // Create Job entity
                logger.debug("Building Job entity");
                Job job = Job.builder()
                        .name(jobDTO.getName())
                        .description(jobDTO.getDescription())
                        .client(client)
                        .category(category)
                        .salaryRangeLower(jobDTO.getSalaryRangeLower())
                        .salaryRangeUpper(jobDTO.getSalaryRangeUpper())
                        .status("OPEN")
                        .skills(jobDTO.getSkills())
                        .createdAt(LocalDateTime.now())
                        .build();

                // Save job
                logger.debug("Saving job: {}", job);
                Job savedJob = jobRepository.save(job);
                logger.info("Job saved successfully with ID: {}", savedJob.getId());

                // Return success response
                Map<String, String> response = new HashMap<>();
                response.put("message", "Job created successfully");
                return ResponseEntity.ok(response);

            } catch (RuntimeException e) {
                logger.error("Error creating job: {}", e.getMessage(), e);
                Map<String, String> response = new HashMap<>();
                response.put("message", e.getMessage());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            } catch (Exception e) {
                logger.error("Unexpected error creating job: {}", e.getMessage(), e);
                Map<String, String> response = new HashMap<>();
                response.put("message", "An unexpected error occurred");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
            }
        }
}