package com.techhive.tech.Controllers;

import com.techhive.tech.Models.User;
import com.techhive.tech.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
}
