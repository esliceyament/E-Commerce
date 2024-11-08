package com.example.authSecurity.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class UserProfile {
    @Id
    private Long userId;
    private LocalDate dateOfBirth;

    @OneToMany(mappedBy = "userProfile", cascade = CascadeType.ALL)
    private List<Address> address = new ArrayList<>();

    @Override
    public String toString() {
        return "UserProfile{" +
                "userId=" + userId +
                ", dateOfBirth=" + dateOfBirth +
                '}';
    }
}
