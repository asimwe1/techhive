package com.techhive.Technology.Models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;
import lombok.ToString;

@Entity
@Table(name = "Users")
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class User  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "firstName")
    private String firstName;

    @Column(name = "lastName")
    private String lastName;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    @ElementCollection
    private List<String> skills;

    public User(String firstName, String lastName, String email, String username, Role role, List<String> skills) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.username = username;
        this.role = role;
        this.skills = skills;
    }

}