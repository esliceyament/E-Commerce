package com.example.authSecurity.dto;

import com.example.authSecurity.enums.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NonNull;

@Data
public class UserDto {

    @Size(min = 4, max = 16)
    @NotBlank(message = "Enter the username!")
    private String username;
    @Size(min = 8, max = 20)
    @NotBlank(message = "Enter the password!")
    private String password;
    @Pattern(regexp = "^\\+994\\d{9}$", message = "Invalid phone format")
    @NotBlank(message = "Enter your phone!")
    private String phone;
    private Role role;

}
