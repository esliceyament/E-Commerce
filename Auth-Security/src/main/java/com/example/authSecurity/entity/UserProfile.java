package com.example.authSecurity.entity;

import com.example.authSecurity.validation.DoBLimit;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
public class UserProfile {
    @Id
    private Long userId;
    private LocalDate dateOfBirth;

    @Embedded
    private Address address;
}
