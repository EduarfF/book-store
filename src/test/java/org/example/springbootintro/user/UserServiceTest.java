package org.example.springbootintro.user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import org.example.springbootintro.dto.user.UserDto;
import org.example.springbootintro.dto.user.UserRegistrationRequestDto;
import org.example.springbootintro.exception.RegistrationException;
import org.example.springbootintro.mapper.user.UserMapper;
import org.example.springbootintro.model.Role;
import org.example.springbootintro.model.User;
import org.example.springbootintro.repository.role.UserRoleRepository;
import org.example.springbootintro.repository.user.UserRepository;
import org.example.springbootintro.service.shoppingcart.ShoppingCartService;
import org.example.springbootintro.service.user.UserServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserRoleRepository userRoleRepository;

    @Mock
    private ShoppingCartService shoppingCartService;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    @DisplayName("Register new user successfully")
    void register_NewUser_ReturnsUserDto() {
        // Given
        UserRegistrationRequestDto requestDto = new UserRegistrationRequestDto(
                "test@example.com",
                "password"
        );

        User mappedUser = new User();
        UserDto expectedDto = new UserDto();

        // When
        when(userMapper.toModel(requestDto)).thenReturn(mappedUser);
        when(userRepository.findByEmail(requestDto.getEmail())).thenReturn(Optional.empty());
        when(userRoleRepository.findByName(Role.RoleName.USER))
                .thenReturn(Optional.of(new Role()));
        when(passwordEncoder.encode(requestDto.getPassword())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(mappedUser);
        when(userMapper.toDto(mappedUser)).thenReturn(expectedDto);

        UserDto resultDto = userService.register(requestDto);

        // Then
        assertNotNull(resultDto);
        assertEquals(expectedDto, resultDto);

        // Verify
        verify(userRepository).findByEmail(requestDto.getEmail());
        verify(userRepository).save(mappedUser);
        verify(shoppingCartService).createShoppingCartForUser(mappedUser);
    }

    @Test
    @DisplayName("Attempt to register with existing email throws exception")
    void register_ExistingEmail_ThrowsRegistrationException() {
        // Given
        UserRegistrationRequestDto requestDto = new UserRegistrationRequestDto(
                "existing@example.com",
                "password"
        );

        // When
        when(userRepository.findByEmail(requestDto.getEmail()))
                .thenReturn(Optional.of(new User()));

        // Then
        assertThrows(RegistrationException.class, () -> userService.register(requestDto));

        // Verify
        verify(userRepository).findByEmail(requestDto.getEmail());
        verify(userRepository, never()).save(any());
        verify(shoppingCartService, never()).createShoppingCartForUser(any());
    }

    @Test
    @DisplayName("Register with unknown role throws exception")
    void register_UnknownRole_ThrowsRegistrationException() {
        // Given
        UserRegistrationRequestDto requestDto = new UserRegistrationRequestDto(
                "test@example.com",
                "password"
        );

        // When
        when(userRepository.findByEmail(requestDto.getEmail())).thenReturn(Optional.empty());
        when(userRoleRepository.findByName(Role.RoleName.USER)).thenReturn(Optional.empty());

        User user = new User();
        when(userMapper.toModel(requestDto)).thenReturn(user);

        // Then
        assertThrows(RegistrationException.class, () -> userService.register(requestDto));

        // Verify
        verify(userRepository).findByEmail(requestDto.getEmail());
        verify(userRepository, never()).save(any());
        verify(shoppingCartService, never()).createShoppingCartForUser(any());
    }
}
