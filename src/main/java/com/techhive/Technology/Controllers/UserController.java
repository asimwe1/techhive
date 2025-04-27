package com.techhive.Technology.Controllers;

import com.techhive.Technology.Models.User;
import com.techhive.Technology.Repository.UserRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
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
    public ResponseEntity<UserDTO> getCurrentUser(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).body(null);
        }
        String username = authentication.getName();
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isEmpty()) {
            return ResponseEntity.status(404).body(null);
        }
        UserDTO dto = new UserDTO();
        dto.setId(user.get().getId());
        dto.setUsername(user.get().getUsername());
        dto.setRole(user.get().getRole().name()); // Ensure role is ROLE_FREELANCER
        return ResponseEntity.ok(dto);
    }
}

@Data
class UserDTO {
    private Integer id;
    private String username;
    private String role;
}