package org.example.springbootintro.mapper.order;

import org.example.springbootintro.config.MapperConfig;
import org.example.springbootintro.dto.order.OrderDto;
import org.example.springbootintro.mapper.orderitem.OrderItemMapper;
import org.example.springbootintro.model.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class, uses = OrderItemMapper.class)
public interface OrderMapper {
    @Mapping(target = "userId", source = "user.id")
    OrderDto toDto(Order order);
}
