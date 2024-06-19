package org.example.springbootintro.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.springbootintro.dto.user.validator.FieldMatch;

@Data
@FieldMatch(
        field = "password",
        fieldMatch = "repeatPassword",
        message = "Passwords must be equal"
)
@NoArgsConstructor
public class UserRegistrationRequestDto {
    @NotBlank(message = "Email can not be blank")
    private String email;

    @NotBlank(message = "Password can not be blank")
    @Size(min = 8, message = "min password length is 8 symbols")
    private String password;

    @NotBlank(message = "Password can not be blank")
    @Size(min = 8, message = "min password length is 8 symbols")
    private String repeatPassword;

    @NotBlank(message = "FirstName can not be blank")
    private String firstName;

    @NotBlank(message = "LastName can not be blank")
    private String lastName;

    private String shippingAddress;

    public UserRegistrationRequestDto(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
