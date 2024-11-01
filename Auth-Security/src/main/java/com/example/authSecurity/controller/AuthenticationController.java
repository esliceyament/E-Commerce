package com.example.authSecurity.controller;

import com.example.authSecurity.dto.UserDto;
import com.example.authSecurity.dto.UserLoginDto;
import com.example.authSecurity.service.UserService;
import com.example.authSecurity.service.implementation.JWTUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/authenticate")
@RequiredArgsConstructor
public class AuthenticationController {

    private final UserService userService;
    private final JWTUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody UserDto dto) {
        UserDto createdUser = userService.register(dto);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserLoginDto dto) {
        String token = userService.verify(dto);
        return ResponseEntity.ok(token);
    }

    @PostMapping("/validate")
    public Boolean validateToken(@RequestParam String token) {
        return jwtUtil.validateToken(token);
    }

    @GetMapping("/storeName")
    public String getSellerName(@RequestHeader("Authorization") String token) {
        String token1 = token.substring(7);
        return jwtUtil.extractUsername(token1);
    }
}