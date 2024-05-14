package org.example.springbootintro.service.user;

import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.example.springbootintro.dto.user.UserDto;
import org.example.springbootintro.dto.user.UserRegistrationRequestDto;
import org.example.springbootintro.exception.RegistrationException;
import org.example.springbootintro.mapper.user.UserMapper;
import org.example.springbootintro.model.Role.RoleName;
import org.example.springbootintro.model.User;
import org.example.springbootintro.repository.role.UserRoleRepository;
import org.example.springbootintro.repository.user.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final UserRoleRepository userRoleRepository;

    @Override
    public UserDto register(UserRegistrationRequestDto userRegistrationRequestDto) {
        if (userRepository.findByEmail(userRegistrationRequestDto.getEmail()).isPresent()) {
            throw new RegistrationException("Can not register user. This email: "
                    + userRegistrationRequestDto.getEmail() + " is already exist");
        }
        User user = userMapper.toModel(userRegistrationRequestDto);
        user.setPassword(passwordEncoder.encode(userRegistrationRequestDto.getPassword()));
        user.setRoles(Set.of(
                userRoleRepository.findByName(RoleName.USER)
                        .orElseThrow(() ->
                                new RegistrationException("Can't register user with such role"))
        ));
        return userMapper.toDto(userRepository.save(user));
    }
}
