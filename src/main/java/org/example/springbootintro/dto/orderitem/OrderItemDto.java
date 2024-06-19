package org.example.springbootintro.dto.orderitem;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OrderItemDto {
    private Long id;
    private Long bookId;
    private int quantity;

    public OrderItemDto(Long id, Long bookId, int quantity) {
        this.id = id;
        this.bookId = bookId;
        this.quantity = quantity;
    }
}
