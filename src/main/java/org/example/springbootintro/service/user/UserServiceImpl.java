package org.example.springbootintro.service.user;

import lombok.RequiredArgsConstructor;
import org.example.springbootintro.dto.user.UserDto;
import org.example.springbootintro.dto.user.UserRegistrationRequestDto;
import org.example.springbootintro.exception.RegistrationException;
import org.example.springbootintro.mapper.user.UserMapper;
import org.example.springbootintro.model.User;
import org.example.springbootintro.repository.user.UserRepository;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserDto register(UserRegistrationRequestDto userRegistrationRequestDto) {
        if (userRepository.findByEmail(userRegistrationRequestDto.getEmail()).isPresent()) {
            throw new RegistrationException("Can not register user. This email: "
                    + userRegistrationRequestDto.getEmail() + " is already exist");
        }
        User user = userMapper.toModel(userRegistrationRequestDto);
        return userMapper.toDto(userRepository.save(user));
    }
}
