package com.example.authSecurity.controller;

import com.example.authSecurity.dto.AddressDto;
import com.example.authSecurity.dto.UserProfileDto;
import com.example.authSecurity.service.UserProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
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

    @PostMapping("/add-address")
    public ResponseEntity<?> addAddress(@RequestBody AddressDto dto) {
        return ResponseEntity.ok(userProfileService.addAddress(dto));
    }

    @GetMapping
    public ResponseEntity<?> getUserProfile() {
        return ResponseEntity.ok(userProfileService.getUserProfile());
    }

    @GetMapping("/get-address")
    public AddressDto getAddress(@RequestHeader("Authorization") String token) {
        return userProfileService.getAddress(token.substring(7));
    }

    @GetMapping("/get-address-by-id")
    public AddressDto getAddressById(@RequestHeader(HttpHeaders.AUTHORIZATION) String token, Long id) {
        token = token.substring(7);
        return userProfileService.getAddressById(token, id);
    }

    @PutMapping("/update-address")
    public void updateDefaultAddress(@RequestParam Long addressId) {
        userProfileService.updateDefaultAddress(addressId);
    }

    //flatmap
//    public Address getAddress(String token) {
//        return userRepository.findByUsername(jwtUtil.extractUsername(token))
//                .flatMap(user -> userProfileRepository.findByUserId(user.getId()))
//                .map(UserProfile::getAddress)
//                .orElse(null);
//    }
}
