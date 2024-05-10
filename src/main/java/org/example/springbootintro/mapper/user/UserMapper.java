package org.example.springbootintro.mapper.user;

import org.example.springbootintro.config.MapperConfig;
import org.example.springbootintro.dto.user.UserDto;
import org.example.springbootintro.dto.user.UserRegistrationRequestDto;
import org.example.springbootintro.model.User;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface UserMapper {
    UserDto toDto(User user);

    User toModel(UserRegistrationRequestDto userRegistrationRequestDto);
}
