package com.example.authSecurity.dto;

import com.example.authSecurity.validation.DoBLimit;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UserProfileDto {
    @DoBLimit(message = "Minimum age of registration is 12!")
    private LocalDate dateOfBirth;
    private AddressDto address;
}
