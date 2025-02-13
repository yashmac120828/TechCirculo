package org.yug.backend.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yug.backend.dto.AuthResponse;
import org.yug.backend.dto.LoginRequest;
import org.yug.backend.dto.RegisterRequest;
import org.yug.backend.model.User;
import org.yug.backend.model.UserRole;
import org.yug.backend.repository.UserRepository;
import org.yug.backend.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@RequiredArgsConstructor
public class AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthResponse register(RegisterRequest request) {
        logger.info("Registering user with email: {}", request.getEmail());

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already in use!");
        }

        User user = new User();
        user.setId(UUID.randomUUID());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(UserRole.STUDENT); // Default role
        user.setFullName(request.getFullName());
        user.setCreatedAt(LocalDateTime.now());

        userRepository.save(user);

      String token = jwtTokenProvider.generateToken(user.getId().toString(), user.getRole().name());
       return new AuthResponse(token);

    }

    public AuthResponse login(LoginRequest request) {
        logger.info("Logging in user with email: {}", request.getEmail());
Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        logger.info("After Auth: {}");
//        SecurityContextHolder.getContext().setAuthentication(authentication);
if(authentication.isAuthenticated()) {
    logger.info("In auth if");
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found!"));
    logger.info("Befor token");
        String token = jwtTokenProvider.generateToken(user.getId().toString(), user.getRole().name());
    logger.info("After token");

        return new AuthResponse(token);

    }

    throw new RuntimeException("Login failed: Invalid credentials");
    }
}