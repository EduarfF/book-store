package org.example.springbootintro.repository.order;

import java.util.List;
import java.util.Optional;
import org.example.springbootintro.model.Order;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("SELECT o FROM Order o JOIN FETCH o.orderItems WHERE o.user.id = :userId")
    List<Order> findAllByUserId(Long userId, Pageable pageable);

    @EntityGraph(attributePaths = "orderItems")
    Optional<Order> findOrderByIdAndUserId(Long orderId, Long userId);
}
