package com.library.library_system.Controllers;



import com.library.library_system.model.Role;
import com.library.library_system.model.User;
import com.library.library_system.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // REGISTER new user (open endpoint)


    @PostMapping("/createUsers")

    public ResponseEntity<?> createUser(@RequestBody User newUser) {
        if (newUser.getUsername() == null || newUser.getUsername().isEmpty() ||
                newUser.getPassword() == null || newUser.getPassword().isEmpty() ||
                newUser.getRole() == null) {
            return ResponseEntity.badRequest().body("Username, password, and role are required.");
        }
        if (userRepository.findByUsername(newUser.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body("Username already exists");
        }

        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
        User savedUser = userRepository.save(newUser);
        System.out.println(" Created user: " + savedUser.getUsername() + " | Role: " + savedUser.getRole());
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
    }
   
    // GET all users (ADMIN only)
    @GetMapping
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // GET one user by ID (ADMIN only)
    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id) {
        return userRepository.findById(id).orElseThrow();
    }

    // UPDATE user (ADMIN only)
    @PutMapping("/{id}")
    public User updateUser(@PathVariable Long id, @RequestBody User userDetails) {
        User user = userRepository.findById(id).orElseThrow();
        user.setUsername(userDetails.getUsername());
        user.setRole(userDetails.getRole());

        if (userDetails.getPassword() != null && !userDetails.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(userDetails.getPassword()));
        }
        return userRepository.save(user);
    }

    // DELETE user (ADMIN only)
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userRepository.deleteById(id);
    }
}
