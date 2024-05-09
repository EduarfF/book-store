package org.example.springbootintro.service.user;

import org.example.springbootintro.dto.user.UserDto;
import org.example.springbootintro.dto.user.UserRegistrationRequestDto;

public interface UserService {
    UserDto register(UserRegistrationRequestDto userRegistrationRequestDto);
}
