package org.example.springbootintro.dto.order;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateOrderRequestDto {
    @NotNull(message = "Shipping address can not be null")
    @NotBlank(message = "Shipping address ca not be blank")
    private String shippingAddress;
}
