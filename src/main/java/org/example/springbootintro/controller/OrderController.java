package org.example.springbootintro.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.springbootintro.dto.order.CreateOrderRequestDto;
import org.example.springbootintro.dto.order.OrderDto;
import org.example.springbootintro.dto.orderitem.OrderItemDto;
import org.example.springbootintro.dto.orderitem.UpdateOrderItemDto;
import org.example.springbootintro.model.User;
import org.example.springbootintro.service.order.OrderService;
import org.example.springbootintro.service.orderitem.OrderItemService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Order management", description = "Endpoints for managing orders")
@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;
    private final OrderItemService orderItemService;

    @GetMapping
    @PreAuthorize("hasRole('USER')")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get orders", description = "Get all orders")
    public List<OrderDto> findAll(@AuthenticationPrincipal User user, Pageable pageable) {
        return orderService.getAll(user.getId(), pageable);
    }

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Place an order", description = "Place an order")
    public OrderDto save(
            @AuthenticationPrincipal User user,
            @RequestBody @Valid CreateOrderRequestDto requestDto
    ) {
        return orderService.save(user, requestDto);
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update order status", description = "Update order status")
    public OrderDto update(
            @PathVariable Long id,
            @RequestBody @Valid UpdateOrderItemDto requestDto
    ) {
        return orderService.update(id, requestDto);
    }

    @GetMapping("/{orderId}/items")
    @PreAuthorize("hasRole('USER')")
    @Operation(
            summary = "Get all items from current order",
            description = "Get all items from current order"
    )
    public List<OrderItemDto> getAllOrderItemsForSpecificOrder(
            @PathVariable Long orderId,
            Pageable pageable
    ) {
        return orderItemService.getAll(orderId, pageable);
    }

    @GetMapping("/{orderId}/items/{itemId}")
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Get current item from current order",
            description = "Get current item from current order")
    public OrderItemDto getSpecificItemFromOrder(
            @AuthenticationPrincipal User user,
            @PathVariable Long orderId,
            @PathVariable Long itemId
    ) {
        return orderItemService.getByItemId(user.getId(), orderId, itemId);
    }
}
