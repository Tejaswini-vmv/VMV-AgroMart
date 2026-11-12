package com.vmv.agromart.controller;

import com.vmv.agromart.dto.ApiResponse;
import com.vmv.agromart.dto.AuthResponse;
import com.vmv.agromart.dto.OtpRequest;
import com.vmv.agromart.dto.VerifyOtpRequest;
import com.vmv.agromart.security.UserPrincipal;
import com.vmv.agromart.service.AuthService;
import com.vmv.agromart.service.OtpService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final OtpService otpService;

    @PostMapping("/send-otp")
    public ApiResponse<Map<String, Object>> sendOtp(@Valid @RequestBody OtpRequest request) {
        String otp = authService.sendOtp(request.getPhoneNumber());

        Map<String, Object> data = new LinkedHashMap<>();
        data.put("phoneNumber", request.getPhoneNumber());
        // In dev mode (no Twilio configured) we echo the OTP back so the app is testable end-to-end.
        if (otpService.isDevMode()) {
            data.put("devOtp", otp);
            data.put("note", "Twilio is not configured; OTP is returned here for local testing only.");
        }
        return ApiResponse.ok("OTP sent", data);
    }

    @PostMapping("/verify-otp")
    public ApiResponse<AuthResponse> verifyOtp(@Valid @RequestBody VerifyOtpRequest request) {
        return ApiResponse.ok("Login successful", authService.verifyOtpAndLogin(request));
    }

    @GetMapping("/me")
    public ApiResponse<Object> me(@AuthenticationPrincipal UserPrincipal principal) {
        return ApiResponse.ok(principal.getUser());
    }
}
