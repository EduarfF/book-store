package org.example.springbootintro.service.order;

import java.util.List;
import org.example.springbootintro.dto.order.CreateOrderRequestDto;
import org.example.springbootintro.dto.order.OrderDto;
import org.example.springbootintro.dto.orderitem.UpdateOrderItemDto;
import org.example.springbootintro.model.User;
import org.springframework.data.domain.Pageable;

public interface OrderService {
    List<OrderDto> getAll(Long id, Pageable pageable);

    OrderDto save(User user, CreateOrderRequestDto requestDto);

    OrderDto update(Long id, UpdateOrderItemDto requestDto);
}
