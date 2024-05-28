package org.example.springbootintro.dto.orderitem;

import lombok.Data;
import org.example.springbootintro.model.Order.Status;

@Data
public class UpdateOrderItemDto {
    private Status status;
}
