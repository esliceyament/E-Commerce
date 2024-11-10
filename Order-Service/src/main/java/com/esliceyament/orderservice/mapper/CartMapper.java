package com.esliceyament.orderservice.mapper;

import com.esliceyament.orderservice.entity.Cart;
import com.esliceyament.orderservice.response.CartResponse;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS, unmappedTargetPolicy = ReportingPolicy.IGNORE, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)

public interface CartMapper {
    CartResponse toResponse(Cart cart);
}
