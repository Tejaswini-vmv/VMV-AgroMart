package com.vmv.agromart.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class VerifyOtpRequest {

    @NotBlank(message = "Phone number is required")
    private String phoneNumber;

    @NotBlank(message = "OTP is required")
    private String otp;

    // Required only for first-time registration
    private String fullName;
    private String role;
    private String city;
}
