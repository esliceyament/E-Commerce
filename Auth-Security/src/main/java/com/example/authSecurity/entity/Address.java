package com.example.authSecurity.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Data
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String street;
    private String city;
    private String postalCode;
    private String country;
    private Date lastUsedAt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserProfile userProfile;
}
