package org.example.springbootintro.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.springbootintro.dto.user.UserDto;
import org.example.springbootintro.dto.user.UserLoginRequestDto;
import org.example.springbootintro.dto.user.UserLoginResponseDto;
import org.example.springbootintro.dto.user.UserRegistrationRequestDto;
import org.example.springbootintro.exception.RegistrationException;
import org.example.springbootintro.security.AuthenticationService;
import org.example.springbootintro.service.user.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "User authentication", description = "Endpoints for register a new user")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final UserService userService;
    private final AuthenticationService authenticationService;

    @PostMapping("/authentication")
    @Operation(summary = "Register a new user", description = "Register a new user")
    public UserDto register(@RequestBody @Valid UserRegistrationRequestDto request)
            throws RegistrationException {
        return userService.register(request);
    }

    @PostMapping("/login")
    @Operation(summary = "Login user", description = "Login user")
    public UserLoginResponseDto login(@RequestBody @Valid UserLoginRequestDto request) {
        return authenticationService.authenticate(request);
    }
}
