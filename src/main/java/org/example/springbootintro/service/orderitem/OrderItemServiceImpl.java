package org.example.springbootintro.service.orderitem;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.springbootintro.dto.orderitem.OrderItemDto;
import org.example.springbootintro.exception.EntityNotFoundException;
import org.example.springbootintro.mapper.orderitem.OrderItemMapper;
import org.example.springbootintro.model.Order;
import org.example.springbootintro.model.OrderItem;
import org.example.springbootintro.repository.order.OrderRepository;
import org.example.springbootintro.repository.orderitem.OrderItemRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class OrderItemServiceImpl implements OrderItemService {
    private final OrderItemRepository orderItemRepository;
    private final OrderItemMapper orderItemMapper;
    private final OrderRepository orderRepository;

    @Override
    public List<OrderItemDto> getAll(Long orderId, Long userId, Pageable pageable) {
        Order order = orderRepository.findOrderByIdAndUserId(orderId, userId)
                .orElseThrow(() -> new EntityNotFoundException("Can not find order"));

        return order.getOrderItems()
                .stream()
                .map(orderItemMapper::toDto)
                .toList();
    }

    @Override
    public OrderItemDto getByItemId(Long userId, Long orderId, Long itemId) {
        OrderItem orderItem = orderItemRepository
                .findByOrderIdAndUserIdAndItemId(orderId, userId, itemId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Can not find order item with id: " + itemId)
                );
        return orderItemMapper.toDto(orderItem);
    }
}
