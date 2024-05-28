package org.example.springbootintro.dto.order;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateOrderRequestDto {
    @NotBlank(message = "Shipping address ca not be blank")
    @Size(max = 255, message = "Shipping address can not be longer than 255 characters")
    private String shippingAddress;
}
