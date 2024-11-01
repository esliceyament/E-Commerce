package com.esliceyament.categoryservice.mapper;

import com.esliceyament.categoryservice.dto.AttributesDto;
import com.esliceyament.categoryservice.entity.Attributes;
import com.esliceyament.categoryservice.payload.AttributesPayload;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface AttributesMapper {

    @Mapping(target = "categoryName", source = "category.name")
    AttributesPayload toPayload(Attributes attributes);

    @Mapping(target = "category.name", source = "categoryName")
    Attributes toEntity(AttributesPayload attributes);

    @Mapping(target = "categoryName", source = "category.name")
    AttributesDto toDto(Attributes attributes);

    @Mapping(target = "category.name", source = "categoryName")
    Attributes toEntity(AttributesDto dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateAttributeFromDto(AttributesDto dto, @MappingTarget Attributes attributes);
}
