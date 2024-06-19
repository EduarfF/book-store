package org.example.springbootintro.order;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;
import org.example.springbootintro.model.Order;
import org.example.springbootintro.repository.order.OrderRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class OrderRepositoryTest {
    @Autowired
    private OrderRepository orderRepository;

    @Test
    @DisplayName("Test finding all orders by user ID when no orders exist")
    void findAllByUserId_NoOrders_ReturnsEmptyList() {
        // Given
        Long userId = 999L;
        PageRequest pageable = PageRequest.of(0, 10);

        // When
        List<Order> orders = orderRepository.findAllByUserId(userId, pageable);

        // Then
        assertTrue(orders.isEmpty());
    }

    @Test
    @DisplayName("Test finding an order by non-existent ID and user ID")
    void findOrderByIdAndUserId_NonExistingId_ReturnsEmptyOptional() {
        // Given
        Long nonExistingOrderId = 999L;
        Long userId = 1L;

        // When
        Optional<Order> foundOrder = orderRepository
                .findOrderByIdAndUserId(nonExistingOrderId, userId);

        // Then
        assertTrue(foundOrder.isEmpty());
    }

    @Test
    @DisplayName("Test finding an order by existing ID but different user ID")
    void findOrderByIdAndUserId_DifferentUserId_ReturnsEmptyOptional() {
        // Given
        Long orderId = 1L;
        Long differentUserId = 999L;

        // When
        Optional<Order> foundOrder = orderRepository
                .findOrderByIdAndUserId(orderId, differentUserId);

        // Then
        assertTrue(foundOrder.isEmpty());
    }
}
