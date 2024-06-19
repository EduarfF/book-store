package org.example.springbootintro.user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.example.springbootintro.dto.user.UserLoginRequestDto;
import org.example.springbootintro.dto.user.UserLoginResponseDto;
import org.example.springbootintro.security.AuthenticationService;
import org.example.springbootintro.security.JwtUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

@ExtendWith(MockitoExtension.class)
public class UserAuthenticationServiceTest {
    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthenticationService authenticationService;

    @Test
    @DisplayName("Authenticate user successfully")
    void authenticate_Success() {
        // Given
        UserLoginRequestDto requestDto = new UserLoginRequestDto("test@example.com", "password");
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                requestDto.getEmail(), requestDto.getPassword()
        );
        String token = "generated-token";

        // When
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(jwtUtil.generateToken(requestDto.getEmail())).thenReturn(token);
        UserLoginResponseDto responseDto = authenticationService.authenticate(requestDto);

        // Then
        assertEquals(token, responseDto.getToken());
    }

    @Test
    @DisplayName("Authenticate user with invalid credentials")
    void authenticate_InvalidCredentials() {
        // Given
        UserLoginRequestDto requestDto = new
                UserLoginRequestDto("test@example.com", "wrongpassword");

        // When
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new RuntimeException("Authentication failed"));

        // Then
        assertThrows(RuntimeException.class, () -> authenticationService.authenticate(requestDto));
    }
}
