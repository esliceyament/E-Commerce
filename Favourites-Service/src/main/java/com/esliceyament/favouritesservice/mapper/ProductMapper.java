package com.esliceyament.favouritesservice.mapper;

import com.esliceyament.favouritesservice.dto.FavoriteProductDTO;
import com.esliceyament.favouritesservice.dto.ProductDto;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS, unmappedTargetPolicy = ReportingPolicy.IGNORE, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ProductMapper {

    FavoriteProductDTO productDtoToFavoriteProductDTO(ProductDto productDto);

}
