package org.example.springbootintro.dto.category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CreateCategoryRequestDto {
    @NotBlank(message = "category name can not be blank")
    @Size(max = 20, message = "max name length is 20 characters")
    private String name;

    @Size(max = 255, message = "max description is 255 characters")
    private String description;

    public CreateCategoryRequestDto(String description) {
        this.description = description;
    }
}
