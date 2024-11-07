package com.example.authSecurity.service;

import com.example.authSecurity.dto.UserDto;
import com.example.authSecurity.dto.UserLoginDto;
import org.springframework.stereotype.Service;

@Service
public interface UserService {

    UserDto register(UserDto dto);
    String verify(UserLoginDto dto);
}
