package com.example.authSecurity.service.implementation;

import com.example.authSecurity.dto.UserDto;
import com.example.authSecurity.dto.UserLoginDto;
import com.example.authSecurity.entity.User;
import com.example.authSecurity.entity.UserProfile;
import com.example.authSecurity.enums.Role;
import com.example.authSecurity.exception.InvalidCredentialsException;
import com.example.authSecurity.exception.ProfileNotFoundException;
import com.example.authSecurity.exception.UsernameAlreadyExistsException;
import com.example.authSecurity.mapper.UserMapper;
import com.example.authSecurity.repository.UserProfileRepository;
import com.example.authSecurity.repository.UserRepository;
import com.example.authSecurity.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final JWTUtil jwtUtil;
    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;
    private final AuthenticationManager authManager;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);
    private final UserMapper userMapper;

    public UserDto register(UserDto dto) {
        if (userRepository.existsByUsername(dto.getUsername()) || userRepository.existsByPhone(dto.getPhone())) {
            throw new UsernameAlreadyExistsException("Username or phone is already taken.");
        }
        User user = userMapper.toEntity(dto);
        if (dto.getRole() == null) {
            user.setRole(Role.USER);
        }
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setStatus(true);
        user.setOrderNumber(getOrderNumber() + 1);
        userRepository.save(user);
        UserProfile userProfile = new UserProfile();
        userProfile.setUserId(user.getId());
        userProfileRepository.save(userProfile);
        return userMapper.toDto(user);
    }
/////try to cache
    public String verify(UserLoginDto dto) {
        try {
            Authentication authentication = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword()));
            User user = userRepository.findByUsername(dto.getUsername())
                    .orElseThrow(() -> new ProfileNotFoundException("User " + dto.getUsername() + " not found!"));
            return jwtUtil.generateToken(dto.getUsername(), user.getRole().toString());
        } catch (BadCredentialsException e) {
            throw new InvalidCredentialsException("Invalid username or password.");
        }
    }

    private Long getOrderNumber() {
        Optional<User> user = userRepository.findFirstByStatusTrueOrderByOrderNumberDesc();
        if (user.isEmpty()) {
            return 0L;
        }
        else {
            return user.get().getOrderNumber();
        }
    }
}
