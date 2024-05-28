package org.example.springbootintro.dto.order;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;
import org.example.springbootintro.dto.orderitem.OrderItemDto;

@Data
public class OrderDto {
    private Long id;
    private Long userId;
    private LocalDateTime orderDate;
    private BigDecimal total;
    private String status;
    private List<OrderItemDto> orderItems;
}
