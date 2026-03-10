package com.clouds3.userservice.controller;

import com.clouds3.userservice.dto.auth.AuthRequest;
import com.clouds3.userservice.dto.auth.AuthResponse;
import com.clouds3.userservice.dto.auth.RegisterRequest;
import com.clouds3.userservice.service.auth.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@CrossOrigin("*")
public class AuthController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @RequestBody AuthRequest authRequestDto)
    {

        log.info("POST /auth/login request received for email={}",
                authRequestDto.getUsername());

        return ResponseEntity.ok(authService.login(authRequestDto));
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refreshToken(
            @RequestParam String refreshToken
    ) {

        log.info("POST /auth/refresh request received");

        return ResponseEntity.ok(authService.refreshToken(refreshToken));
    }

    @PostMapping("/register")
    public ResponseEntity<Void> register(
            @RequestBody RegisterRequest dto
    ) {

        log.info("POST /auth/register request received for email={}",
                dto.getEmail());

        authService.register(dto);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
            @RequestParam String refreshToken
    ) {

        log.info("POST /auth/logout request received");

        authService.logout(refreshToken);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/logout/{userId}")
    public ResponseEntity<Void> logoutAll(
            @PathVariable Long userId
    ) {

        log.info("POST /auth/logout/{} request received", userId);

        authService.logoutAll(userId);
        return ResponseEntity.noContent().build();
    }
}
