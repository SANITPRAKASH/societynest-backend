package com.societynest.backend.service;

import com.societynest.backend.dto.AuthResponse;
import com.societynest.backend.dto.LoginRequest;
import com.societynest.backend.dto.SignupRequest;
import com.societynest.backend.entity.Role;
import com.societynest.backend.entity.User;
import com.societynest.backend.entity.Flat;
import com.societynest.backend.repository.UserRepository;
import com.societynest.backend.repository.RoleRepository;
import com.societynest.backend.repository.FlatRepository;
import com.societynest.backend.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private FlatRepository flatRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    public AuthResponse signup(SignupRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setFullName(request.getFullName());
        user.setPhoneNumber(request.getPhoneNumber());

        // Assign RESIDENT role by default
        Role residentRole = roleRepository.findByName("RESIDENT")
                .orElseThrow(() -> new RuntimeException("Role not found"));
        user.setRole(residentRole);

        // Assign flat if provided
        if (request.getFlatId() != null) {
            Flat flat = flatRepository.findById(request.getFlatId())
                    .orElseThrow(() -> new RuntimeException("Flat not found"));
            user.setFlat(flat);
        }

        user.setIsApproved(false); // Needs admin approval

        userRepository.save(user);

        String token = jwtUtil.generateToken(userDetailsService.loadUserByUsername(user.getEmail()));

        return new AuthResponse(token, user.getEmail(), user.getFullName(), 
                                user.getRole().getName(), user.getIsApproved());
    }

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());
        String token = jwtUtil.generateToken(userDetails);

        return new AuthResponse(token, user.getEmail(), user.getFullName(), 
                                user.getRole().getName(), user.getIsApproved());
    }
}