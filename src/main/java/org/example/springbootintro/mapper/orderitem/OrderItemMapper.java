package org.example.springbootintro.mapper.orderitem;

import org.example.springbootintro.config.MapperConfig;
import org.example.springbootintro.dto.orderitem.OrderItemDto;
import org.example.springbootintro.model.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface OrderItemMapper {
    @Mapping(target = "bookId", source = "book.id")
    OrderItemDto toDto(OrderItem orderItem);
}
