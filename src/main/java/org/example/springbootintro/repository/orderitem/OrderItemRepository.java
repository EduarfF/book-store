package org.example.springbootintro.repository.orderitem;

import java.util.List;
import java.util.Optional;
import org.example.springbootintro.model.OrderItem;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    List<OrderItem> findAllByOrderId(Long id, Pageable pageable);

    @Query("SELECT oi "
            + "FROM OrderItem oi "
            + "JOIN oi.order o "
            + "WHERE o.id = :orderId AND o.user.id = :userId AND oi.id = :itemId")
    Optional<OrderItem> findByOrderIdAndUserIdAndItemId(Long orderId, Long userId, Long itemId);
}
