package com.vmv.agromart.dto;

import com.vmv.agromart.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {
    private String token;
    private Long userId;
    private String fullName;
    private String phoneNumber;
    private User.UserRole role;
    private boolean newUser;
    private String city;
}
