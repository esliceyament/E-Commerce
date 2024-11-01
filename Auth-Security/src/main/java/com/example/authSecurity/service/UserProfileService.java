package com.example.authSecurity.service;

import com.example.authSecurity.dto.UserProfileDto;
import org.springframework.stereotype.Service;

@Service
public interface UserProfileService {
    public UserProfileDto createOrUpdateUserProfile(UserProfileDto dto);
    public UserProfileDto getUserProfile();
}
