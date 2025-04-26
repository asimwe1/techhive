package com.techhive.Technology;

import com.techhive.Technology.Models.*;
import com.techhive.Technology.Repository.JobCategoryRepository;
import com.techhive.Technology.Repository.JobRepository;
import com.techhive.Technology.Repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@SpringBootApplication
public class TechnologyApplication {

	public static void main(String[] args) {
		SpringApplication.run(TechnologyApplication.class, args);
	}

	@Bean
	CommandLineRunner commandLineRunner(UserRepository userRepository, JobRepository jobRepository,
										JobCategoryRepository jobCategoryRepository, PasswordEncoder passwordEncoder) {
		return args -> {
			// Job categories
			JobCategory[] categories = new JobCategory[]{
					new JobCategory("Web Development"),
					new JobCategory("Mobile Development"),
					new JobCategory("Blockchain"),
			};
			for (JobCategory category : categories) {
				String name = category.getCategory();
				if (jobCategoryRepository.findByCategory(name).isEmpty()) {
					jobCategoryRepository.saveAndFlush(category);
					System.out.println("Saved category: " + name);
				}
			}

			// Users
			List<String> clientSkills = Arrays.asList("Investment", "Development", "Production");
			List<String> freelancerSkills = Arrays.asList("Java", "NextJS");
			User[] users = new User[]{
					new User("Dawidi", "Polo", "dawidi2@gmail.com", "david2", Role.CLIENT, clientSkills),
					new User("Alice", "Smith", "alice.smith@gmail.com", "alice", Role.CLIENT, clientSkills),
					new User("Bob", "Johnson", "bob.johnson@gmail.com", "bob", Role.FREELANCER, freelancerSkills),
					new User("Clara", "Lee", "clara.lee@gmail.com", "clara", Role.FREELANCER, freelancerSkills),
					new User("David", "Brown", "david.brown@gmail.com", "david", Role.FREELANCER, freelancerSkills),
			};
			for (User user : users) {
				String email = user.getEmail();
				String username = user.getUsername();
				if (userRepository.findByEmail(email).isEmpty() && userRepository.findByUsername(username).isEmpty()) {
					user.setPassword(passwordEncoder.encode("password123")); // Encode default password
					userRepository.save(user);
					System.out.println("User saved: " + email);
				} else {
					System.out.println("User with email " + email + " or username " + username + " already exists");
				}
			}

			// Jobs
			Job job = new Job();
			job.setName("Website Development");
			Optional<JobCategory> category = jobCategoryRepository.findById(1);
			if (category.isEmpty()) {
				throw new RuntimeException("Category with ID 1 not found");
			}
			job.setCategory(category.get());
			Optional<User> client = userRepository.findById(1);
			if (client.isEmpty() || client.get().getRole() != Role.CLIENT) {
				throw new RuntimeException("Client with ID 1 not found or is not a client");
			}
			job.setClient(client.get());
			job.setSalaryRangeLower(6000.0);
			job.setSalaryRangeUpper(8000.0);
			job.setStatus("OPEN");
			job.setSkills(Arrays.asList("Java", "Spring Boot"));
			jobRepository.save(job);
			System.out.println("Job saved: " + job.getName());
		};
	}
}