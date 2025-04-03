package com.techhive.Technology;

import com.techhive.Technology.Models.*;
import com.techhive.Technology.Repository.*;
import org.hibernate.Hibernate;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@SpringBootApplication
public class TechnologyApplication {

	public static void main(String[] args) {
		SpringApplication.run(TechnologyApplication.class, args);
	}

	@Bean
	@Transactional
	CommandLineRunner commandLineRunner(
			UserRepository userRepository,
			JobRepository jobRepository,
			JobCategoryRepository jobCategoryRepository,
			UserProfileRepository userProfileRepository,
			ApplicationRepository applicationRepository,
			ProposalRepository proposalRepository,
			FreelancerProfileRepository freelancerProfileRepository,
			PasswordEncoder passwordEncoder) {
		return args -> {
			// Clear existing data to avoid duplicates
			freelancerProfileRepository.deleteAll();
			userProfileRepository.deleteAll();
			userRepository.deleteAll();
			jobRepository.deleteAll();
			jobCategoryRepository.deleteAll();
			applicationRepository.deleteAll();
			proposalRepository.deleteAll();
			System.out.println("Cleared existing data");

			// Job Categories
			JobCategory[] categories = new JobCategory[]{
					new JobCategory("Web Development"),
					new JobCategory("Mobile Development"),
					new JobCategory("Blockchain"),
					new JobCategory("Data Science"),
					new JobCategory("DevOps"),
					new JobCategory("UI/UX Design"),
					new JobCategory("Cybersecurity")
			};
			for (JobCategory category : categories) {
				String name = category.getCategory();
				if (jobCategoryRepository.findByCategory(name).isEmpty()) {
					jobCategoryRepository.saveAndFlush(category);
					System.out.println("Saved category: " + name);
				}
			}

			// Skills per category
			List<List<String>> categorySkills = Arrays.asList(
					Arrays.asList("Java", "Spring Boot", "React", "Node.js", "MongoDB", "MySQL"),
					Arrays.asList("Swift", "Kotlin", "Flutter", "Firebase"),
					Arrays.asList("Solidity", "Ethereum", "Web3.js", "Truffle"),
					Arrays.asList("Python", "Pandas", "Tableau", "TensorFlow", "NLP"),
					Arrays.asList("Jenkins", "Docker", "Kubernetes", "AWS"),
					Arrays.asList("Figma", "Adobe XD", "Sketch"),
					Arrays.asList("Penetration Testing", "Wireshark", "Metasploit")
			);

			// Users
			User[] users = new User[]{
					new User("Dawidi", "Polo", "dawidi2@gmail.com", "david2", Role.CLIENT),
					new User("Alice", "Smith", "alice.smith@gmail.com", "alice", Role.CLIENT),
					new User("Bob", "Johnson", "bob.johnson@gmail.com", "bob", Role.FREELANCER),
					new User("Clara", "Lee", "clara.lee@gmail.com", "clara", Role.FREELANCER),
					new User("David", "Brown", "david.brown@gmail.com", "david", Role.FREELANCER),
					new User("Emma", "Wilson", "emma.wilson@gmail.com", "emma", Role.CLIENT),
					new User("Frank", "Davis", "frank.davis@gmail.com", "frank", Role.CLIENT),
					new User("Grace", "Martinez", "grace.martinez@gmail.com", "grace", Role.CLIENT),
					new User("Henry", "Taylor", "henry.taylor@gmail.com", "henry", Role.CLIENT),
					new User("Isabella", "Anderson", "isabella.anderson@gmail.com", "isabella", Role.CLIENT),
					new User("Jack", "Thomas", "jack.thomas@gmail.com", "jack", Role.FREELANCER),
					new User("Kelly", "White", "kelly.white@gmail.com", "kelly", Role.FREELANCER),
					new User("Liam", "Harris", "liam.harris@gmail.com", "liam", Role.FREELANCER),
					new User("Mia", "Clark", "mia.clark@gmail.com", "mia", Role.FREELANCER),
					new User("Noah", "Lewis", "noah.lewis@gmail.com", "noah", Role.FREELANCER),
					new User("Olivia", "Walker", "olivia.walker@gmail.com", "olivia", Role.FREELANCER),
					new User("Peter", "Hall", "peter.hall@gmail.com", "peter", Role.FREELANCER),
					new User("Quinn", "Allen", "quinn.allen@gmail.com", "quinn", Role.FREELANCER),
					new User("Rachel", "Young", "rachel.young@gmail.com", "rachel", Role.FREELANCER),
					new User("Samuel", "King", "samuel.king@gmail.com", "samuel", Role.FREELANCER)
			};
			Random random = new Random();
			for (User user : users) {
				String email = user.getEmail();
				String username = user.getUsername();
				if (userRepository.findByEmail(email).isEmpty() && userRepository.findByUsername(username).isEmpty()) {
					user.setPassword(passwordEncoder.encode("password123"));
					userRepository.saveAndFlush(user);
					System.out.println("User saved: " + email + " with role: " + user.getRole());

					// Create UserProfile for each user
					UserProfile profile = new UserProfile();
					profile.setUser(user);
					profile.setBio("Experienced " + (user.getRole() == Role.FREELANCER ? "freelancer" : "client") + " in tech projects.");
					profile.setLocation(getRandomLocation());
					profile.setWebsite("https://" + username + ".example.com");
					profile.setAvatar("/avatars/" + username + ".png");
					userProfileRepository.saveAndFlush(profile);
					System.out.println("UserProfile saved for: " + email);

					// Create FreelancerProfile for freelancers
					if (user.getRole() == Role.FREELANCER) {
						FreelancerProfile freelancerProfile = new FreelancerProfile();
						freelancerProfile.setUserProfile(profile);
						List<String> selectedSkills = categorySkills.get(random.nextInt(categorySkills.size()));
						int numSkills = random.nextInt(3) + 2; // 2-4 skills
						List<String> freelancerSkills = selectedSkills.subList(0, Math.min(numSkills, selectedSkills.size()));
						System.out.println("Assigning skills to " + email + ": " + freelancerSkills);
						freelancerProfile.setSkills(freelancerSkills);
						freelancerProfile.setCompletedJobs(0);
						freelancerProfile.setRating(0.0);
						freelancerProfile.setHourlyRate(50.0 + random.nextDouble() * 50.0);
						freelancerProfileRepository.saveAndFlush(freelancerProfile);
						// Access skills within the transaction to initialize them
						Hibernate.initialize(freelancerProfile.getSkills());
						System.out.println("FreelancerProfile saved for: " + email + " with skills: " + freelancerProfile.getSkills());
					}
				} else {
					System.out.println("User with email " + email + " or username " + username + " already exists");
				}
			}

			// Jobs
			Job[] jobs = new Job[]{
					Job.builder()
							.name("Website Development")
							.description("Develop a responsive e-commerce website with payment integration.")
							.category(jobCategoryRepository.findById(1).orElseThrow(() -> new RuntimeException("Category 1 not found")))
							.client(userRepository.findById(1L).orElseThrow(() -> new RuntimeException("Client 1 not found")))
							.salaryRangeLower(6000.0)
							.salaryRangeUpper(8000.0)
							.status("OPEN")
							.skills(Arrays.asList("Java", "Spring Boot", "React"))
							.build(),
					Job.builder()
							.name("Mobile Banking App")
							.description("Build a secure mobile app for banking with real-time transactions.")
							.category(jobCategoryRepository.findById(2).orElseThrow(() -> new RuntimeException("Category 2 not found")))
							.client(userRepository.findById(2L).orElseThrow(() -> new RuntimeException("Client 2 not found")))
							.salaryRangeLower(10000.0)
							.salaryRangeUpper(12000.0)
							.status("OPEN")
							.skills(Arrays.asList("Swift", "Kotlin", "Firebase"))
							.build(),
					Job.builder()
							.name("Blockchain Payment System")
							.description("Implement a blockchain-based payment system for secure transactions.")
							.category(jobCategoryRepository.findById(3).orElseThrow(() -> new RuntimeException("Category 3 not found")))
							.client(userRepository.findById(6L).orElseThrow(() -> new RuntimeException("Client 6 not found")))
							.salaryRangeLower(15000.0)
							.salaryRangeUpper(20000.0)
							.status("IN_PROGRESS")
							.skills(Arrays.asList("Solidity", "Ethereum", "Web3.js"))
							.build(),
					Job.builder()
							.name("Data Analysis Dashboard")
							.description("Create a dashboard for visualizing business data with interactive charts.")
							.category(jobCategoryRepository.findById(4).orElseThrow(() -> new RuntimeException("Category 4 not found")))
							.client(userRepository.findById(7L).orElseThrow(() -> new RuntimeException("Client 7 not found")))
							.salaryRangeLower(5000.0)
							.salaryRangeUpper(7000.0)
							.status("OPEN")
							.skills(Arrays.asList("Python", "Pandas", "Tableau"))
							.build(),
					Job.builder()
							.name("CI/CD Pipeline Setup")
							.description("Set up a CI/CD pipeline for a microservices architecture.")
							.category(jobCategoryRepository.findById(5).orElseThrow(() -> new RuntimeException("Category 5 not found")))
							.client(userRepository.findById(8L).orElseThrow(() -> new RuntimeException("Client 8 not found")))
							.salaryRangeLower(8000.0)
							.salaryRangeUpper(10000.0)
							.status("COMPLETED")
							.skills(Arrays.asList("Jenkins", "Docker", "Kubernetes"))
							.build(),
					Job.builder()
							.name("Mobile App UI Design")
							.description("Design user-friendly UI for a fitness tracking mobile app.")
							.category(jobCategoryRepository.findById(6).orElseThrow(() -> new RuntimeException("Category 6 not found")))
							.client(userRepository.findById(9L).orElseThrow(() -> new RuntimeException("Client 9 not found")))
							.salaryRangeLower(4000.0)
							.salaryRangeUpper(6000.0)
							.status("OPEN")
							.skills(Arrays.asList("Figma", "Adobe XD"))
							.build(),
					Job.builder()
							.name("Network Security Audit")
							.description("Conduct a security audit for a corporate network.")
							.category(jobCategoryRepository.findById(7).orElseThrow(() -> new RuntimeException("Category 7 not found")))
							.client(userRepository.findById(10L).orElseThrow(() -> new RuntimeException("Client 10 not found")))
							.salaryRangeLower(12000.0)
							.salaryRangeUpper(15000.0)
							.status("OPEN")
							.skills(Arrays.asList("Penetration Testing", "Wireshark"))
							.build(),
					Job.builder()
							.name("E-Learning Platform")
							.description("Develop a web platform for online courses with video streaming.")
							.category(jobCategoryRepository.findById(1).orElseThrow(() -> new RuntimeException("Category 1 not found")))
							.client(userRepository.findById(1L).orElseThrow(() -> new RuntimeException("Client 1 not found")))
							.salaryRangeLower(9000.0)
							.salaryRangeUpper(11000.0)
							.status("IN_PROGRESS")
							.skills(Arrays.asList("React", "Node.js", "MongoDB"))
							.build(),
					Job.builder()
							.name("Inventory Management System")
							.description("Build a system to manage inventory for a retail business.")
							.category(jobCategoryRepository.findById(1).orElseThrow(() -> new RuntimeException("Category 1 not found")))
							.client(userRepository.findById(2L).orElseThrow(() -> new RuntimeException("Client 2 not found")))
							.salaryRangeLower(7000.0)
							.salaryRangeUpper(9000.0)
							.status("COMPLETED")
							.skills(Arrays.asList("Java", "Spring Boot", "MySQL"))
							.build(),
					Job.builder()
							.name("Chat Application")
							.description("Create a real-time chat app with group chat features.")
							.category(jobCategoryRepository.findById(2).orElseThrow(() -> new RuntimeException("Category 2 not found")))
							.client(userRepository.findById(6L).orElseThrow(() -> new RuntimeException("Client 6 not found")))
							.salaryRangeLower(6000.0)
							.salaryRangeUpper(8000.0)
							.status("OPEN")
							.skills(Arrays.asList("Flutter", "Firebase"))
							.build(),
					Job.builder()
							.name("AI Chatbot")
							.description("Develop an AI-powered chatbot for customer support.")
							.category(jobCategoryRepository.findById(4).orElseThrow(() -> new RuntimeException("Category 4 not found")))
							.client(userRepository.findById(7L).orElseThrow(() -> new RuntimeException("Client 7 not found")))
							.salaryRangeLower(10000.0)
							.salaryRangeUpper(13000.0)
							.status("OPEN")
							.skills(Arrays.asList("Python", "TensorFlow", "NLP"))
							.build()
			};
			for (Job job : jobs) {
				if (jobRepository.findByNameAndClient(job.getName(), job.getClient()).isEmpty()) {
					jobRepository.saveAndFlush(job);
					System.out.println("Job saved: " + job.getName());
				}
			}

			// Applications and Proposals
			Application[] applications = new Application[]{
					createApplication(jobRepository.findById(1).get(), userRepository.findById(3L).get(), "PENDING", 7000.0),
					createApplication(jobRepository.findById(1).get(), userRepository.findById(4L).get(), "ACCEPTED", 7500.0),
					createApplication(jobRepository.findById(2).get(), userRepository.findById(5L).get(), "PENDING", 11000.0),
					createApplication(jobRepository.findById(3).get(), userRepository.findById(11L).get(), "IN_PROGRESS", 18000.0),
					createApplication(jobRepository.findById(4).get(), userRepository.findById(12L).get(), "PENDING", 6000.0),
					createApplication(jobRepository.findById(5).get(), userRepository.findById(13L).get(), "COMPLETED", 9000.0),
					createApplication(jobRepository.findById(6).get(), userRepository.findById(14L).get(), "PENDING", 5000.0),
					createApplication(jobRepository.findById(7).get(), userRepository.findById(15L).get(), "PENDING", 13000.0),
					createApplication(jobRepository.findById(8).get(), userRepository.findById(16L).get(), "IN_PROGRESS", 10000.0),
					createApplication(jobRepository.findById(9).get(), userRepository.findById(17L).get(), "COMPLETED", 8000.0),
					createApplication(jobRepository.findById(10).get(), userRepository.findById(18L).get(), "PENDING", 7000.0)
			};
			for (Application application : applications) {
				if (applicationRepository.findByJobAndFreelancer(application.getJob(), application.getFreelancer()).isEmpty()) {
					application.setAppliedAt(LocalDateTime.now().minusDays((long) (Math.random() * 30)));
					applicationRepository.saveAndFlush(application);
					System.out.println("Application saved for job: " + application.getJob().getName() + " by " + application.getFreelancer().getUsername());

					Proposal proposal = new Proposal(
							application.getJob(),
							application.getFreelancer(),
							"I have extensive experience in " + String.join(", ", application.getJob().getSkills()) + " and can deliver high-quality work within the timeline.",
							application.getBidAmount(),
							(int) (Math.random() * 30 + 10)
					);
					// Pre-load job.client to avoid lazy loading issues
					proposal.getJob().getClient().getId(); // Force initialization
					proposalRepository.saveAndFlush(proposal);
					System.out.println("Proposal saved for job: " + application.getJob().getName() + " by " + application.getFreelancer().getUsername());
				}
			}
		};
	}

	private Application createApplication(Job job, User freelancer, String status, Double bidAmount) {
		Application application = new Application();
		application.setJob(job);
		application.setFreelancer(freelancer);
		application.setStatus(status);
		application.setBidAmount(bidAmount);
		return application;
	}

	private String getRandomLocation() {
		String[] locations = {
				"New York, NY", "San Francisco, CA", "London, UK", "Toronto, Canada",
				"Berlin, Germany", "Sydney, Australia", "Tokyo, Japan", "Mumbai, India"
		};
		return locations[(int) (Math.random() * locations.length)];
	}
}