package com.example.authSecurity.controller;

import com.example.authSecurity.dto.UserProfileDto;
import com.example.authSecurity.service.UserProfileService;
import com.example.authSecurity.service.implementation.UserProfileServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users/profile")
@RequiredArgsConstructor
public class UserProfileController {

    private final UserProfileService userProfileService;

    @PostMapping("/add")
    public ResponseEntity<?> createOrUpdateProfile(@Valid @RequestBody UserProfileDto dto) {
        return ResponseEntity.ok(userProfileService.createOrUpdateUserProfile(dto));
    }

    @GetMapping
    public ResponseEntity<?> getUserProfile() {
        return ResponseEntity.ok(userProfileService.getUserProfile());
    }
}
