package com.serviceforevent.user.controller;

import com.serviceforevent.common.dto.ApiResponse;
import com.serviceforevent.user.dto.AuthenticationRequest;
import com.serviceforevent.user.dto.AuthenticationResponse;
import com.serviceforevent.user.dto.UserRegistrationRequest;
import com.serviceforevent.user.entity.User;
import com.serviceforevent.user.security.JwtTokenProvider;
import com.serviceforevent.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtTokenProvider tokenProvider;

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@Valid @RequestBody AuthenticationRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = tokenProvider.generateToken(authentication);
            
            return ResponseEntity.ok(new AuthenticationResponse("User logged in successfully", jwt, "Bearer"));
        } catch (Exception e) {
            log.error("Error during login: ", e);
            throw e;
        }
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<User>> register(@Valid @RequestBody UserRegistrationRequest request) {
        try {
            log.info("Attempting to register user with email: {}", request.getEmail());
            User user = userService.registerUser(request);
            log.info("Successfully registered user with email: {}", request.getEmail());
            return ResponseEntity.ok(ApiResponse.success("User registered successfully", user));
        } catch (Exception e) {
            log.error("Error during registration: ", e);
            throw e;
        }
    }
} 