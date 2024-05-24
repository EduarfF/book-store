package org.example.springbootintro.repository.orderitem;

import java.util.List;
import org.example.springbootintro.model.OrderItem;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    List<OrderItem> findAllByOrderId(Long id, Pageable pageable);
}
