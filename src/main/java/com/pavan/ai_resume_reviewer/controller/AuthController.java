package com.pavan.ai_resume_reviewer.controller;

import com.pavan.ai_resume_reviewer.dto.ApiResponse;
import com.pavan.ai_resume_reviewer.dto.AuthResponse;
import com.pavan.ai_resume_reviewer.dto.LoginRequest;
import com.pavan.ai_resume_reviewer.entity.User;
import com.pavan.ai_resume_reviewer.repository.UserRepository;
import com.pavan.ai_resume_reviewer.security.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;

    public AuthController(AuthenticationManager authenticationManager,
                          JwtService jwtService,
                          UserRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@RequestBody LoginRequest request) {

        var authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        String token = jwtService.generateToken(
                (org.springframework.security.core.userdetails.UserDetails)
                        authentication.getPrincipal()
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        AuthResponse authResponse = new AuthResponse(token, user.getEmail(), user.getName(), user.getRole());

        return ResponseEntity.ok(new ApiResponse<>(true, "Login successful", authResponse));
    }
}