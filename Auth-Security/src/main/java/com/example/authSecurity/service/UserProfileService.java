package com.example.authSecurity.service;

import com.example.authSecurity.dto.AddressDto;
import com.example.authSecurity.dto.UserProfileDto;
import org.springframework.stereotype.Service;

@Service
public interface UserProfileService {
    UserProfileDto createOrUpdateUserProfile(UserProfileDto dto);
    UserProfileDto getUserProfile();
    AddressDto addAddress(AddressDto dto);
    AddressDto getAddress(String token);
    AddressDto getAddressById(String token, Long id);
    void updateDefaultAddress(Long addressId);
}
