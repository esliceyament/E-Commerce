package com.example.authSecurity.dto;

import lombok.Data;

@Data
public class AddressDto {
    private String street;
    private String city;
    private String postalCode;
    private String country;
}
