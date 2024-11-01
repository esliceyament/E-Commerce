package com.example.productservice.mapper;

import com.example.productservice.dto.ProductDto;
import com.example.productservice.dto.RatingDto;
import com.example.productservice.entity.Product;
import com.example.productservice.entity.Rating;
import com.example.productservice.payload.ProductPayload;
import com.example.productservice.response.ProductResponse;
import com.example.productservice.service.implementation.RatingServiceImpl;
import org.mapstruct.*;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS, unmappedTargetPolicy = ReportingPolicy.IGNORE, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ProductMapper {
    ProductDto toDto(Product product);

    Product toEntity(ProductDto dto);

    ProductResponse toResponse(Product product);

    Product toEntity(ProductResponse response);

    ProductPayload toPayload(Product product);

    Product toEntity(ProductPayload payload);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateProductFromDto(ProductDto dto, @MappingTarget Product product);

}
