package org.yug.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.yug.backend.dto.AuthResponse;
import org.yug.backend.dto.LoginRequest;
import org.yug.backend.dto.RegisterRequest;
import org.yug.backend.model.User;
import org.yug.backend.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")

public class AuthController {
@Autowired
 AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register( @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }
}