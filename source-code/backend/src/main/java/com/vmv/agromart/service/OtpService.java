package com.vmv.agromart.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Duration;

@Service
@RequiredArgsConstructor
@Slf4j
public class OtpService {

    private final StringRedisTemplate redisTemplate;

    @Value("${otp.length:6}")
    private int otpLength;

    @Value("${otp.expiry-minutes:5}")
    private long otpExpiryMinutes;

    @Value("${twilio.account-sid:}")
    private String twilioAccountSid;

    private static final SecureRandom RANDOM = new SecureRandom();

    private String otpKey(String phoneNumber) {
        return "otp:" + phoneNumber;
    }

    /**
     * Generates and "sends" an OTP. When Twilio credentials are not configured (local/dev/docker
     * demo environment), the OTP is only logged to the backend console/logs and echoed back in the
     * API response so the app remains fully testable without a paid SMS provider.
     */
    public String generateAndSendOtp(String phoneNumber) {
        String otp = generateNumericOtp(otpLength);
        redisTemplate.opsForValue().set(otpKey(phoneNumber), otp, Duration.ofMinutes(otpExpiryMinutes));

        if (twilioAccountSid == null || twilioAccountSid.isBlank()) {
            log.info("=================================================================");
            log.info(" DEV MODE (no Twilio configured): OTP for {} is {}", phoneNumber, otp);
            log.info("=================================================================");
        } else {
            // Real SMS dispatch would go here via Twilio SDK using the configured credentials.
            log.info("OTP generated for {} (SMS dispatch via Twilio not implemented in this build)", phoneNumber);
        }

        return otp;
    }

    public boolean verifyOtp(String phoneNumber, String candidate) {
        String stored = redisTemplate.opsForValue().get(otpKey(phoneNumber));
        if (stored == null) {
            return false;
        }
        boolean matches = stored.equals(candidate);
        if (matches) {
            redisTemplate.delete(otpKey(phoneNumber));
        }
        return matches;
    }

    public boolean isDevMode() {
        return twilioAccountSid == null || twilioAccountSid.isBlank();
    }

    private String generateNumericOtp(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(RANDOM.nextInt(10));
        }
        return sb.toString();
    }
}
