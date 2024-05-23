package org.example.springbootintro.dto.cartitem;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateCartItemDto {
    @NotNull(message = "Quantity can not be null")
    @Min(value = 1, message = "Quantity must be greater than 0")
    private int quantity;
}
