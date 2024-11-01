package com.example.authSecurity.mapper;

import com.example.authSecurity.dto.UserProfileDto;
import com.example.authSecurity.entity.UserProfile;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS, unmappedTargetPolicy = ReportingPolicy.IGNORE, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserProfileMapper {
    UserProfileDto toDto(UserProfile userProfile);
    UserProfile toEntity(UserProfileDto userProfileDto);
}
