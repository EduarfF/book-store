package org.example.springbootintro.dto.category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateCategoryRequestDto {
    @NotBlank(message = "category name can not be blank")
    @Size(max = 20, message = "max name length is 20 characters")
    private String name;

    @NotBlank(message = "category description can not be blank")
    @Size(max = 255, message = "max description is 255 characters")
    private String description;
}
