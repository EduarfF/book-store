package org.example.springbootintro.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserLoginRequestDto {
    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Size(min = 8, message = "min password length is 8 symbols")
    private String password;
}
