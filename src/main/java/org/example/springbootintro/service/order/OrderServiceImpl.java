package org.example.springbootintro.service.order;

import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.example.springbootintro.dto.order.CreateOrderRequestDto;
import org.example.springbootintro.dto.order.OrderDto;
import org.example.springbootintro.dto.orderitem.UpdateOrderItemDto;
import org.example.springbootintro.exception.EntityNotFoundException;
import org.example.springbootintro.mapper.order.OrderMapper;
import org.example.springbootintro.model.CartItem;
import org.example.springbootintro.model.Order;
import org.example.springbootintro.model.Order.Status;
import org.example.springbootintro.model.OrderItem;
import org.example.springbootintro.model.ShoppingCart;
import org.example.springbootintro.model.User;
import org.example.springbootintro.repository.cartitem.CartItemRepository;
import org.example.springbootintro.repository.order.OrderRepository;
import org.example.springbootintro.repository.shoppingcart.ShoppingCartRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final ShoppingCartRepository shoppingCartRepository;
    private final CartItemRepository cartItemRepository;
    private final OrderMapper orderMapper;

    @Override
    public List<OrderDto> getAll(Long id, Pageable pageable) {
        List<Order> orders = orderRepository.findAllByUserId(id, pageable);
        return orders.stream()
                .map(orderMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public OrderDto save(User user, CreateOrderRequestDto requestDto) {
        ShoppingCart shoppingCart = shoppingCartRepository.findByUserId(user.getId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Can not find shopping cart with such user id: " + user.getId())
                );

        Order order = new Order(
                user,
                Status.PENDING,
                LocalDateTime.now(),
                requestDto.getShippingAddress()
        );

        Set<OrderItem> orderItems = getOrderItems(shoppingCart, order);

        order.setTotal(getSumOfAllItems(orderItems));
        order.setOrderItems(orderItems);
        cartItemRepository.deleteAll(shoppingCart.getCartItems());

        return orderMapper.toDto(orderRepository.save(order));
    }

    @Override
    @Transactional
    public OrderDto update(Long id, UpdateOrderItemDto requestDto) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Can not find order with id: " + id)
                );
        order.setStatus(requestDto.getStatus());
        return orderMapper.toDto(orderRepository.save(order));
    }

    private static Set<OrderItem> getOrderItems(ShoppingCart shoppingCart, Order order) {
        Set<OrderItem> orderItems = new HashSet<>();
        for (CartItem item : shoppingCart.getCartItems()) {
            OrderItem orderItem = new OrderItem(
                    order,
                    item.getBook(),
                    item.getQuantity(),
                    item.getBook().getPrice()
            );
            orderItems.add(orderItem);
        }
        return orderItems;
    }

    private static BigDecimal getSumOfAllItems(Set<OrderItem> orderItems) {
        BigDecimal sum = BigDecimal.ZERO;
        for (OrderItem item : orderItems) {
            BigDecimal itemTotal = item.getPrice()
                    .multiply(BigDecimal.valueOf(item.getQuantity()));
            sum = sum.add(itemTotal);
        }
        return sum;
    }
}
