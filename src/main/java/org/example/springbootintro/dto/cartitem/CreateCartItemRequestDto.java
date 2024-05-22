package org.example.springbootintro.dto.cartitem;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateCartItemRequestDto {
    @NotNull(message = "Book ID can not be null")
    private Long bookId;

    @Min(value = 1, message = "Quantity must be greater than 0")
    private int quantity;
}
