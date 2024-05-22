package org.example.springbootintro.dto.cartitem;

import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class UpdateCartItemDto {
    @Min(value = 1, message = "Quantity must be greater than 0")
    private int quantity;
}
