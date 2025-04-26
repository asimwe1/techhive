package com.techhive.Technology.Controllers;

import com.techhive.Technology.Models.User;
import com.techhive.Technology.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
@CrossOrigin(origins = "http://localhost:8095")
@RequestMapping("/api/users") // Base URL for this controller
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/hello")
    public String hello() {
        return "hello world";
    }

    @PostMapping("/register")
    public void saveUser(@RequestBody User user) {
        userRepository.save(user);
    }


    @GetMapping
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @GetMapping("/{id}")
    public Optional<User> getUserById(@PathVariable Integer id) {
        return userRepository.findById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Integer id) {
        userRepository.deleteById(id);
    }

    @GetMapping("/me")
    public ResponseEntity<User> getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username)
                .map(user -> {
                    System.out.println("Found User: " + username + ", Role: " + user.getRole());
                    return ResponseEntity.ok(user);
                })
                .orElseGet(() -> {
                    System.out.println("User not found: " + username);
                    return ResponseEntity.status(404).body(null);
                });
    }
}
