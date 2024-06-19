package org.example.springbootintro.user;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.springbootintro.controller.AuthenticationController;
import org.example.springbootintro.dto.user.UserDto;
import org.example.springbootintro.dto.user.UserLoginRequestDto;
import org.example.springbootintro.dto.user.UserLoginResponseDto;
import org.example.springbootintro.dto.user.UserRegistrationRequestDto;
import org.example.springbootintro.security.AuthenticationService;
import org.example.springbootintro.service.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerTest {
    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @Mock
    private AuthenticationService authenticationService;

    @Autowired
    private ObjectMapper objectMapper;

    @InjectMocks
    private AuthenticationController authenticationController;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(authenticationController).build();
    }

    @Test
    @DisplayName("Test register new user")
    public void registerNewUserTest() throws Exception {
        // Given
        UserRegistrationRequestDto requestDto = new UserRegistrationRequestDto();
        requestDto.setEmail("test@example.com");
        requestDto.setPassword("password");
        requestDto.setRepeatPassword("password");
        requestDto.setFirstName("John");
        requestDto.setLastName("Doe");

        UserDto expectedUserDto = new UserDto();
        expectedUserDto.setId(1L);
        expectedUserDto.setEmail("test@example.com");

        given(userService.register(any(UserRegistrationRequestDto.class)))
                .willReturn(expectedUserDto);

        // When
        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        MvcResult mvcResult = mockMvc.perform(post("/api/auth/authentication")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andReturn();

        // Then
        String jsonResponse = mvcResult.getResponse().getContentAsString();

        assertThat(mvcResult.getResponse().getStatus()).isEqualTo(HttpStatus.OK.value());

        UserDto actualUserDto = objectMapper.readValue(jsonResponse, UserDto.class);
        assertThat(actualUserDto.getId()).isEqualTo(1L);
        assertThat(actualUserDto.getEmail()).isEqualTo("test@example.com");

        // Verify
        verify(userService).register(any(UserRegistrationRequestDto.class));
        verifyNoMoreInteractions(userService);
    }

    @Test
    @DisplayName("Test login user")
    public void loginUserTest() throws Exception {
        // Given
        UserLoginRequestDto requestDto = new UserLoginRequestDto();
        requestDto.setEmail("test@example.com");
        requestDto.setPassword("password");

        UserLoginResponseDto expectedResponseDto = new UserLoginResponseDto();
        expectedResponseDto.setToken("mocked.token.string");

        given(authenticationService.authenticate(any(UserLoginRequestDto.class)))
                .willReturn(expectedResponseDto);

        // When
        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        MvcResult mvcResult = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andReturn();

        // Then
        String jsonResponse = mvcResult.getResponse().getContentAsString();

        assertThat(mvcResult.getResponse().getStatus()).isEqualTo(HttpStatus.OK.value());

        UserLoginResponseDto actualResponse = objectMapper
                .readValue(jsonResponse, UserLoginResponseDto.class);
        assertThat(actualResponse.getToken()).isEqualTo("mocked.token.string");

        // Verify
        verify(authenticationService).authenticate(any(UserLoginRequestDto.class));
        verifyNoMoreInteractions(authenticationService);
    }
}
