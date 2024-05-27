package org.example.springbootintro.service.orderitem;

import java.util.List;
import org.example.springbootintro.dto.orderitem.OrderItemDto;
import org.springframework.data.domain.Pageable;

public interface OrderItemService {
    List<OrderItemDto> getAll(Long orderId, Long userId, Pageable pageable);

    OrderItemDto getByItemId(Long userId, Long orderId, Long itemId);
}
