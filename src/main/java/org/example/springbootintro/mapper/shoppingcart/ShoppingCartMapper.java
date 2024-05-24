package org.example.springbootintro.mapper.shoppingcart;

import org.example.springbootintro.config.MapperConfig;
import org.example.springbootintro.dto.shoppingcart.ShoppingCartDto;
import org.example.springbootintro.model.ShoppingCart;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface ShoppingCartMapper {
    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "cartItems", ignore = true)
    ShoppingCartDto toDto(ShoppingCart shoppingCart);
}
