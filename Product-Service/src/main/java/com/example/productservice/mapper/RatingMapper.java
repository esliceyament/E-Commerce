package com.example.productservice.mapper;

import com.example.productservice.dto.RatingDto;
import com.example.productservice.entity.Rating;
import org.mapstruct.*;

@Mapper(componentModel = "spring", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS, unmappedTargetPolicy = ReportingPolicy.IGNORE, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface RatingMapper {
    RatingDto toDto(Rating rating);

    Rating toEntity(RatingDto dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateRatingFromDto(RatingDto dto, @MappingTarget Rating rating);
}
