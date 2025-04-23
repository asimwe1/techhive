package com.techhive.Technology;

import com.techhive.Technology.Models.*;
import com.techhive.Technology.Repository.JobCategoryRepository;
import com.techhive.Technology.Repository.JobRepository;
import com.techhive.Technology.Repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SpringBootApplication
public class TechnologyApplication {


	public static void main(String[] args) {
		SpringApplication.run(TechnologyApplication.class, args);
	}

	@Bean
	CommandLineRunner commandLineRunner(UserRepository userRepository, JobRepository jobRepository, JobCategoryRepository jobCategoryRepository) {
		List<String> clientSkills = new ArrayList<>();
		List<String> freelancerSkills = new ArrayList<>();
		clientSkills.add("Investment");
		clientSkills.add("Development");
		clientSkills.add("Production");
		freelancerSkills.add("Java");
		freelancerSkills.add("NextJS");

		return args -> {
			// List of users to insert
			User[] users = new User[] {
					new User("Dawidi", "Polo", "dawidi2@gmail.com", "david", Role.CLIENT, clientSkills ),  // Changed email since dawidi@gmail.com exists
					new User("Alice", "Smith", "alice.smith@gmail.com", "smith", Role.CLIENT, clientSkills),
					new User("Bob", "Johnson", "bob.johnson@gmail.com", "bob", Role.FREELANCER, freelancerSkills),
					new User("Clara", "Lee", "clara.lee@gmail.com", "clara", Role.FREELANCER, freelancerSkills),
					new User("David", "Brown", "david.brown@gmail.com", "david", Role.FREELANCER, freelancerSkills),
			};

			Job[] jobs = new Job[] {
					new Job("Website Development", 1,1, 6000.00, 8000.00 ),
			};

			JobCategory[] categories = new JobCategory[]{
					new JobCategory("Web Development"),
					new JobCategory("Mobile Development"),
					new JobCategory("Blockchain"),
			};

			for (JobCategory category : categories) {
				String name = category.getCategory();
				if (category != null && jobCategoryRepository.findByCategory(name).isEmpty() ) {
					jobCategoryRepository.saveAndFlush(category);
					System.out.println("Saved: " + category.getCategory() + "As category");
				}
			}

			// Insert each user, checking for duplicates
			for (User user : users) {
				String email = user.getEmail();
				String username = user.getUsername();
				if (userRepository.findByEmail(email).isEmpty() && userRepository.findByUsername(username).isEmpty() ) { // Requires findByEmail in UserRepository
					userRepository.save(user);
					System.out.println("User saved with email: " + email);
					System.out.println(user + "\n" + "\n");
				} else {
					System.out.println("User with email " + email + " already exists, skipping save.");
				}
			}

			for (Job job : jobs) {
				Optional<User> client = userRepository.findById(job.getClientId());
				if (client.isPresent() && client.get().getRole() == Role.CLIENT) {
					jobRepository.save(job);
					System.out.println("Job saved: " + job + ";");
				} else {
					System.out.println("The job client with id " + job.getClientId() + " does not exist or is a developer, skipping save;");
				}
			}
		};
	}
}