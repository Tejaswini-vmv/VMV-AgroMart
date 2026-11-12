package com.vmv.agromart.service;

import com.vmv.agromart.dto.AuthResponse;
import com.vmv.agromart.dto.VerifyOtpRequest;
import com.vmv.agromart.exception.BadRequestException;
import com.vmv.agromart.model.User;
import com.vmv.agromart.repository.UserRepository;
import com.vmv.agromart.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final OtpService otpService;
    private final JwtUtil jwtUtil;

    public String sendOtp(String phoneNumber) {
        return otpService.generateAndSendOtp(phoneNumber);
    }

    public AuthResponse verifyOtpAndLogin(VerifyOtpRequest request) {
        boolean valid = otpService.verifyOtp(request.getPhoneNumber(), request.getOtp());
        if (!valid) {
            throw new BadRequestException("Invalid or expired OTP");
        }

        boolean isNewUser = !userRepository.existsByPhoneNumber(request.getPhoneNumber());
        User user;

        if (isNewUser) {
            if (request.getFullName() == null || request.getFullName().isBlank()) {
                throw new BadRequestException("fullName is required to complete registration");
            }
            User.UserRole newRole = parseRole(request.getRole());
            if (newRole == User.UserRole.SUPER_ADMIN && userRepository.existsByRole(User.UserRole.SUPER_ADMIN)) {
                throw new BadRequestException("A Super Admin already exists; only one Super Admin is allowed on this platform");
            }
            user = new User();
            user.setPhoneNumber(request.getPhoneNumber());
            user.setFullName(request.getFullName());
            user.setRole(newRole);
            user.setCity(request.getCity());
            user.setStatus(User.UserStatus.ACTIVE);
            user.setIsVerified(true);
            user = userRepository.save(user);
        } else {
            user = userRepository.findByPhoneNumber(request.getPhoneNumber())
                    .orElseThrow(() -> new BadRequestException("User not found"));
            user.setIsVerified(true);
            user.setLastLoginAt(java.time.LocalDateTime.now());
            user = userRepository.save(user);
        }

        String token = jwtUtil.generateToken(user.getPhoneNumber(), user.getId(), user.getRole().name());

        return new AuthResponse(token, user.getId(), user.getFullName(), user.getPhoneNumber(), user.getRole(), isNewUser, user.getCity());
    }

    private User.UserRole parseRole(String role) {
        if (role == null || role.isBlank()) {
            return User.UserRole.CUSTOMER;
        }
        try {
            return User.UserRole.valueOf(role.toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new BadRequestException("Invalid role: " + role);
        }
    }
}
