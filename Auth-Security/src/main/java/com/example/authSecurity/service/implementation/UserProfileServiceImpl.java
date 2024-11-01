package com.example.authSecurity.service.implementation;

import com.example.authSecurity.dto.UserProfileDto;
import com.example.authSecurity.entity.UserPrincipal;
import com.example.authSecurity.entity.UserProfile;
import com.example.authSecurity.exception.ProfileNotFoundException;
import com.example.authSecurity.mapper.UserProfileMapper;
import com.example.authSecurity.repository.UserProfileRepository;
import com.example.authSecurity.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserProfileServiceImpl implements UserProfileService {

    private final UserProfileRepository userProfileRepository;
    private final UserProfileMapper userProfileMapper;

    public UserProfileDto createOrUpdateUserProfile(UserProfileDto dto) {
        UserProfile userProfile = userProfileMapper.toEntity(dto);
        Long userId = getCurrentUserId();
        userProfile.setUserId(userId);
        userProfileRepository.save(userProfile);
        return userProfileMapper.toDto(userProfile);
    }

    public UserProfileDto getUserProfile() {
        Long userId = getCurrentUserId();
        UserProfile userProfile = userProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new ProfileNotFoundException("Profile not found for userId: " + userId));
        return userProfileMapper.toDto(userProfile);
    }

    private Long getCurrentUserId() {
        UserPrincipal userId = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userId.user().getId();
    }
}
