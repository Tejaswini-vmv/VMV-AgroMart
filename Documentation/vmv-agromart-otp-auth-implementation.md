# VMV AgroMart - OTP Authentication Implementation Guide

## Overview
This document provides complete implementation details for the OTP-based authentication system, which is the priority feature for VMV AgroMart.

---

## Architecture Flow

```
User → Frontend (Angular) → Backend API → Redis/MySQL → SMS Gateway → User
                                    ↓
                              JWT Token Generation
                                    ↓
                            Authenticated Session
```

---

## Part 1: Backend Implementation (Spring Boot)

### 1.1 Project Dependencies (pom.xml)

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.vmvagromart</groupId>
    <artifactId>vmv-agromart-backend</artifactId>
    <version>1.0.0</version>
    <packaging>jar</packaging>

    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>3.2.0</version>
    </parent>

    <dependencies>
        <!-- Spring Boot Web -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>

        <!-- Spring Security -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-security</artifactId>
        </dependency>

        <!-- Spring Data JPA -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-jpa</artifactId>
        </dependency>

        <!-- MySQL Driver -->
        <dependency>
            <groupId>com.mysql</groupId>
            <artifactId>mysql-connector-j</artifactId>
            <scope>runtime</scope>
        </dependency>

        <!-- Redis -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-redis</artifactId>
        </dependency>

        <!-- JWT -->
        <dependency>
            <groupId>io.jsonwebtoken</groupId>
            <artifactId>jjwt-api</artifactId>
            <version>0.12.3</version>
        </dependency>
        <dependency>
            <groupId>io.jsonwebtoken</groupId>
            <artifactId>jjwt-impl</artifactId>
            <version>0.12.3</version>
            <scope>runtime</scope>
        </dependency>
        <dependency>
            <groupId>io.jsonwebtoken</groupId>
            <artifactId>jjwt-jackson</artifactId>
            <version>0.12.3</version>
            <scope>runtime</scope>
        </dependency>

        <!-- Validation -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-validation</artifactId>
        </dependency>

        <!-- Lombok -->
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>

        <!-- Twilio (SMS Gateway) -->
        <dependency>
            <groupId>com.twilio.sdk</groupId>
            <artifactId>twilio</artifactId>
            <version>9.14.1</version>
        </dependency>
    </dependencies>
</project>
```

### 1.2 Application Configuration (application.yml)

```yaml
spring:
  application:
    name: vmv-agromart-backend

  datasource:
    url: jdbc:mysql://localhost:3306/vmv_agromart?useSSL=false&serverTimezone=UTC
    username: ${DB_USERNAME:agromart_user}
    password: ${DB_PASSWORD:your_password}
    driver-class-name: com.mysql.cj.jdbc.Driver

  jpa:
    hibernate:
      ddl-auto: validate
    show-sql: false
    properties:
      hibernate:
        dialect: org.hibernate.dialect.MySQL8Dialect
        format_sql: true

  redis:
    host: ${REDIS_HOST:localhost}
    port: ${REDIS_PORT:6379}
    password: ${REDIS_PASSWORD:}
    timeout: 60000

  security:
    jwt:
      secret: ${JWT_SECRET:your-256-bit-secret-key-change-in-production}
      expiration: 1800000  # 30 minutes
      refresh-expiration: 604800000  # 7 days

server:
  port: 8080
  servlet:
    context-path: /api

# Twilio Configuration
twilio:
  account-sid: ${TWILIO_ACCOUNT_SID}
  auth-token: ${TWILIO_AUTH_TOKEN}
  phone-number: ${TWILIO_PHONE_NUMBER}

# OTP Configuration
otp:
  length: 6
  expiry-minutes: 5
  max-attempts: 3
  max-daily-requests: 5

# CORS Configuration
cors:
  allowed-origins: http://localhost:4200,http://localhost:8100
  allowed-methods: GET,POST,PUT,DELETE,OPTIONS
  allowed-headers: '*'
  allow-credentials: true
```

### 1.3 Entity Classes

#### User.java
```java
package com.vmvagromart.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(unique = true, nullable = false, length = 15)
    private String phoneNumber;

    @Column(unique = true, length = 100)
    private String email;

    @Column(nullable = false, length = 100)
    private String fullName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole userRole;

    @Column(nullable = false)
    private Boolean isActive = true;

    @Column(nullable = false)
    private Boolean isVerified = false;

    private String profileImageUrl;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private LocalDateTime lastLogin;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}

enum UserRole {
    FARMER, CITY_ADMIN, STORAGE_ADMIN, SUPER_ADMIN,
    RETAILER, FLAT_OWNER, PROVIDER
}
```

#### OtpVerification.java
```java
package com.vmvagromart.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "otp_verification")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OtpVerification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long otpId;

    @Column(nullable = false)
    private String phoneNumber;

    @Column(nullable = false)
    private String otpCode;

    @Enumerated(EnumType.STRING)
    private OtpType otpType;

    @Column(nullable = false)
    private Boolean isVerified = false;

    @Column(nullable = false)
    private LocalDateTime expiresAt;

    private LocalDateTime createdAt;

    private LocalDateTime verifiedAt;

    private Integer attemptCount = 0;

    private String ipAddress;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}

enum OtpType {
    LOGIN, REGISTRATION, PASSWORD_RESET
}
```

#### UserSession.java
```java
package com.vmvagromart.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_sessions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sessionId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String jwtToken;

    private String refreshToken;

    @Enumerated(EnumType.STRING)
    private DeviceType deviceType;

    private String deviceId;

    private String ipAddress;

    @Column(columnDefinition = "TEXT")
    private String userAgent;

    private Boolean isActive = true;

    private LocalDateTime createdAt;

    private LocalDateTime expiresAt;

    private LocalDateTime lastActivity;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        lastActivity = LocalDateTime.now();
    }
}

enum DeviceType {
    WEB, MOBILE_ANDROID, MOBILE_IOS
}
```

### 1.4 DTOs (Data Transfer Objects)

#### SendOtpRequest.java
```java
package com.vmvagromart.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class SendOtpRequest {

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^\\+91[6-9]\\d{9}$", message = "Invalid phone number format")
    private String phoneNumber;

    private String otpType = "LOGIN"; // LOGIN, REGISTRATION
}
```

#### VerifyOtpRequest.java
```java
package com.vmvagromart.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class VerifyOtpRequest {

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^\\+91[6-9]\\d{9}$", message = "Invalid phone number format")
    private String phoneNumber;

    @NotBlank(message = "OTP is required")
    @Size(min = 6, max = 6, message = "OTP must be 6 digits")
    private String otp;

    @NotBlank(message = "Device type is required")
    private String deviceType; // WEB, MOBILE_ANDROID, MOBILE_IOS

    private String deviceId;
}
```

#### AuthResponse.java
```java
package com.vmvagromart.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {
    private String accessToken;
    private String refreshToken;
    private String tokenType = "Bearer";
    private Long expiresIn;
    private UserDto user;
}
```

#### UserDto.java
```java
package com.vmvagromart.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private Long userId;
    private String phoneNumber;
    private String email;
    private String fullName;
    private String userRole;
    private Boolean isActive;
    private Boolean isVerified;
    private String profileImageUrl;
}
```

### 1.5 Repositories

#### UserRepository.java
```java
package com.vmvagromart.repository;

import com.vmvagromart.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByPhoneNumber(String phoneNumber);
    Optional<User> findByEmail(String email);
    boolean existsByPhoneNumber(String phoneNumber);
    boolean existsByEmail(String email);
}
```

#### OtpVerificationRepository.java
```java
package com.vmvagromart.repository;

import com.vmvagromart.model.OtpVerification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface OtpVerificationRepository extends JpaRepository<OtpVerification, Long> {

    Optional<OtpVerification> findByPhoneNumberAndOtpCodeAndIsVerifiedFalse(
        String phoneNumber, String otpCode
    );

    @Query("SELECT COUNT(o) FROM OtpVerification o " +
           "WHERE o.phoneNumber = ?1 AND o.createdAt > ?2")
    long countOtpRequestsToday(String phoneNumber, LocalDateTime startOfDay);

    List<OtpVerification> findByPhoneNumberAndIsVerifiedFalseOrderByCreatedAtDesc(
        String phoneNumber
    );
}
```

#### UserSessionRepository.java
```java
package com.vmvagromart.repository;

import com.vmvagromart.model.UserSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserSessionRepository extends JpaRepository<UserSession, Long> {
    Optional<UserSession> findByJwtTokenAndIsActiveTrue(String jwtToken);
    void deleteByUserId(Long userId);
}
```

### 1.6 Services

#### OtpService.java
```java
package com.vmvagromart.service;

import com.vmvagromart.model.OtpVerification;
import com.vmvagromart.model.OtpType;
import com.vmvagromart.repository.OtpVerificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class OtpService {

    private final OtpVerificationRepository otpRepository;
    private final RedisTemplate<String, String> redisTemplate;
    private final SmsService smsService;

    @Value("${otp.length:6}")
    private int otpLength;

    @Value("${otp.expiry-minutes:5}")
    private int expiryMinutes;

    @Value("${otp.max-attempts:3}")
    private int maxAttempts;

    @Value("${otp.max-daily-requests:5}")
    private int maxDailyRequests;

    private static final String OTP_PREFIX = "otp:";
    private static final String ATTEMPT_PREFIX = "otp_attempts:";
    private static final String DAILY_LIMIT_PREFIX = "otp_daily:";

    @Transactional
    public boolean sendOtp(String phoneNumber, String ipAddress) {
        // Check daily limit
        if (!checkDailyLimit(phoneNumber)) {
            throw new RuntimeException("Daily OTP limit exceeded. Try again tomorrow.");
        }

        // Generate OTP
        String otpCode = generateOtp();

        // Store in Redis
        String redisKey = OTP_PREFIX + phoneNumber;
        redisTemplate.opsForValue().set(
            redisKey,
            otpCode,
            expiryMinutes,
            TimeUnit.MINUTES
        );

        // Reset attempt counter
        redisTemplate.delete(ATTEMPT_PREFIX + phoneNumber);

        // Store in MySQL for audit
        OtpVerification otp = new OtpVerification();
        otp.setPhoneNumber(phoneNumber);
        otp.setOtpCode(otpCode);
        otp.setOtpType(OtpType.LOGIN);
        otp.setExpiresAt(LocalDateTime.now().plusMinutes(expiryMinutes));
        otp.setIpAddress(ipAddress);
        otpRepository.save(otp);

        // Increment daily counter
        String dailyKey = DAILY_LIMIT_PREFIX + phoneNumber;
        redisTemplate.opsForValue().increment(dailyKey);
        redisTemplate.expire(dailyKey, 24, TimeUnit.HOURS);

        // Send SMS
        String message = String.format(
            "Your VMV AgroMart verification code is: %s. Valid for %d minutes.",
            otpCode, expiryMinutes
        );
        smsService.sendSms(phoneNumber, message);

        log.info("OTP sent successfully to {}", phoneNumber);
        return true;
    }

    @Transactional
    public boolean verifyOtp(String phoneNumber, String otpCode) {
        // Check attempts
        String attemptKey = ATTEMPT_PREFIX + phoneNumber;
        Integer attempts = (Integer) redisTemplate.opsForValue().get(attemptKey);
        if (attempts != null && attempts >= maxAttempts) {
            throw new RuntimeException("Maximum OTP verification attempts exceeded");
        }

        // Get OTP from Redis
        String redisKey = OTP_PREFIX + phoneNumber;
        String storedOtp = redisTemplate.opsForValue().get(redisKey);

        if (storedOtp == null) {
            throw new RuntimeException("OTP expired or not found");
        }

        if (!storedOtp.equals(otpCode)) {
            // Increment attempt counter
            redisTemplate.opsForValue().increment(attemptKey);
            redisTemplate.expire(attemptKey, expiryMinutes, TimeUnit.MINUTES);
            throw new RuntimeException("Invalid OTP");
        }

        // Mark as verified in MySQL
        otpRepository.findByPhoneNumberAndOtpCodeAndIsVerifiedFalse(phoneNumber, otpCode)
            .ifPresent(otp -> {
                otp.setIsVerified(true);
                otp.setVerifiedAt(LocalDateTime.now());
                otpRepository.save(otp);
            });

        // Clean up Redis
        redisTemplate.delete(redisKey);
        redisTemplate.delete(attemptKey);

        log.info("OTP verified successfully for {}", phoneNumber);
        return true;
    }

    private String generateOtp() {
        SecureRandom random = new SecureRandom();
        StringBuilder otp = new StringBuilder();
        for (int i = 0; i < otpLength; i++) {
            otp.append(random.nextInt(10));
        }
        return otp.toString();
    }

    private boolean checkDailyLimit(String phoneNumber) {
        String dailyKey = DAILY_LIMIT_PREFIX + phoneNumber;
        Integer count = (Integer) redisTemplate.opsForValue().get(dailyKey);
        return count == null || count < maxDailyRequests;
    }
}
```

#### SmsService.java
```java
package com.vmvagromart.service;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;

@Service
@Slf4j
public class SmsService {

    @Value("${twilio.account-sid}")
    private String accountSid;

    @Value("${twilio.auth-token}")
    private String authToken;

    @Value("${twilio.phone-number}")
    private String twilioPhoneNumber;

    @PostConstruct
    public void init() {
        Twilio.init(accountSid, authToken);
    }

    public void sendSms(String toPhoneNumber, String messageBody) {
        try {
            Message message = Message.creator(
                new PhoneNumber(toPhoneNumber),
                new PhoneNumber(twilioPhoneNumber),
                messageBody
            ).create();

            log.info("SMS sent successfully. SID: {}", message.getSid());
        } catch (Exception e) {
            log.error("Failed to send SMS to {}: {}", toPhoneNumber, e.getMessage());
            throw new RuntimeException("Failed to send SMS");
        }
    }
}
```

#### JwtService.java
```java
package com.vmvagromart.service;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;

@Service
@Slf4j
public class JwtService {

    @Value("${spring.security.jwt.secret}")
    private String jwtSecret;

    @Value("${spring.security.jwt.expiration}")
    private long jwtExpiration;

    @Value("${spring.security.jwt.refresh-expiration}")
    private long refreshExpiration;

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }

    public String generateAccessToken(Long userId, String role, Long sessionId) {
        return Jwts.builder()
            .subject(userId.toString())
            .claim("role", role)
            .claim("sessionId", sessionId)
            .issuedAt(new Date())
            .expiration(new Date(System.currentTimeMillis() + jwtExpiration))
            .signWith(getSigningKey())
            .compact();
    }

    public String generateRefreshToken(Long userId) {
        return Jwts.builder()
            .subject(userId.toString())
            .issuedAt(new Date())
            .expiration(new Date(System.currentTimeMillis() + refreshExpiration))
            .signWith(getSigningKey())
            .compact();
    }

    public Claims extractClaims(String token) {
        return Jwts.parser()
            .verifyWith(getSigningKey())
            .build()
            .parseSignedClaims(token)
            .getPayload();
    }

    public Long extractUserId(String token) {
        return Long.parseLong(extractClaims(token).getSubject());
    }

    public String extractRole(String token) {
        return extractClaims(token).get("role", String.class);
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            log.error("Invalid JWT token: {}", e.getMessage());
            return false;
        }
    }
}
```

#### AuthService.java
```java
package com.vmvagromart.service;

import com.vmvagromart.dto.*;
import com.vmvagromart.model.*;
import com.vmvagromart.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final UserSessionRepository sessionRepository;
    private final OtpService otpService;
    private final JwtService jwtService;

    @Transactional
    public boolean sendOtp(SendOtpRequest request, String ipAddress) {
        return otpService.sendOtp(request.getPhoneNumber(), ipAddress);
    }

    @Transactional
    public AuthResponse verifyOtpAndLogin(VerifyOtpRequest request, String ipAddress, String userAgent) {
        // Verify OTP
        otpService.verifyOtp(request.getPhoneNumber(), request.getOtp());

        // Get or create user
        User user = userRepository.findByPhoneNumber(request.getPhoneNumber())
            .orElseGet(() -> createNewUser(request.getPhoneNumber()));

        // Update last login
        user.setLastLogin(LocalDateTime.now());
        userRepository.save(user);

        // Create session
        UserSession session = new UserSession();
        session.setUser(user);
        session.setDeviceType(DeviceType.valueOf(request.getDeviceType()));
        session.setDeviceId(request.getDeviceId());
        session.setIpAddress(ipAddress);
        session.setUserAgent(userAgent);
        session.setExpiresAt(LocalDateTime.now().plusMinutes(30));

        // Generate tokens
        String accessToken = jwtService.generateAccessToken(
            user.getUserId(),
            user.getUserRole().name(),
            null  // Session ID will be set after save
        );
        String refreshToken = jwtService.generateRefreshToken(user.getUserId());

        session.setJwtToken(accessToken);
        session.setRefreshToken(refreshToken);
        sessionRepository.save(session);

        // Build response
        return AuthResponse.builder()
            .accessToken(accessToken)
            .refreshToken(refreshToken)
            .expiresIn(1800L)  // 30 minutes
            .user(mapToUserDto(user))
            .build();
    }

    private User createNewUser(String phoneNumber) {
        User user = new User();
        user.setPhoneNumber(phoneNumber);
        user.setFullName("New User");
        user.setUserRole(UserRole.FLAT_OWNER);  // Default role
        user.setIsActive(true);
        user.setIsVerified(false);
        return userRepository.save(user);
    }

    private UserDto mapToUserDto(User user) {
        return UserDto.builder()
            .userId(user.getUserId())
            .phoneNumber(user.getPhoneNumber())
            .email(user.getEmail())
            .fullName(user.getFullName())
            .userRole(user.getUserRole().name())
            .isActive(user.getIsActive())
            .isVerified(user.getIsVerified())
            .profileImageUrl(user.getProfileImageUrl())
            .build();
    }
}
```

### 1.7 Controller

#### AuthController.java
```java
package com.vmvagromart.controller;

import com.vmvagromart.dto.*;
import com.vmvagromart.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "${cors.allowed-origins}")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/send-otp")
    public ResponseEntity<?> sendOtp(
        @Valid @RequestBody SendOtpRequest request,
        HttpServletRequest httpRequest
    ) {
        try {
            String ipAddress = getClientIp(httpRequest);
            authService.sendOtp(request, ipAddress);
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "OTP sent successfully"
            ));
        } catch (Exception e) {
            log.error("Error sending OTP: {}", e.getMessage());
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", e.getMessage()
            ));
        }
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(
        @Valid @RequestBody VerifyOtpRequest request,
        HttpServletRequest httpRequest
    ) {
        try {
            String ipAddress = getClientIp(httpRequest);
            String userAgent = httpRequest.getHeader("User-Agent");

            AuthResponse response = authService.verifyOtpAndLogin(
                request, ipAddress, userAgent
            );

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error verifying OTP: {}", e.getMessage());
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", e.getMessage()
            ));
        }
    }

    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty()) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}
```

### 1.8 Security Configuration

#### SecurityConfig.java
```java
package com.vmvagromart.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/auth/**").permitAll()
                .anyRequest().authenticated()
            )
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            );

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList(
            "http://localhost:4200",
            "http://localhost:8100"
        ));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
```

---

## Part 2: Frontend Implementation (Angular)

### 2.1 Create Auth Service

```typescript
// src/app/services/auth.service.ts
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable, tap } from 'rxjs';
import { Router } from '@angular/router';

export interface User {
  userId: number;
  phoneNumber: string;
  email?: string;
  fullName: string;
  userRole: string;
  isActive: boolean;
  isVerified: boolean;
  profileImageUrl?: string;
}

export interface AuthResponse {
  accessToken: string;
  refreshToken: string;
  tokenType: string;
  expiresIn: number;
  user: User;
}

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl = 'http://localhost:8080/api/auth';
  private currentUserSubject = new BehaviorSubject<User | null>(null);
  public currentUser$ = this.currentUserSubject.asObservable();

  constructor(
    private http: HttpClient,
    private router: Router
  ) {
    this.loadUserFromStorage();
  }

  sendOtp(phoneNumber: string): Observable<any> {
    return this.http.post(`${this.apiUrl}/send-otp`, { phoneNumber });
  }

  verifyOtp(phoneNumber: string, otp: string, deviceType: string): Observable<AuthResponse> {
    const deviceId = this.getDeviceId();

    return this.http.post<AuthResponse>(`${this.apiUrl}/verify-otp`, {
      phoneNumber,
      otp,
      deviceType,
      deviceId
    }).pipe(
      tap(response => {
        this.handleAuthentication(response);
      })
    );
  }

  private handleAuthentication(response: AuthResponse): void {
    // Store tokens
    localStorage.setItem('accessToken', response.accessToken);
    localStorage.setItem('refreshToken', response.refreshToken);
    localStorage.setItem('user', JSON.stringify(response.user));

    // Update current user
    this.currentUserSubject.next(response.user);
  }

  private loadUserFromStorage(): void {
    const userJson = localStorage.getItem('user');
    if (userJson) {
      const user = JSON.parse(userJson);
      this.currentUserSubject.next(user);
    }
  }

  logout(): void {
    localStorage.removeItem('accessToken');
    localStorage.removeItem('refreshToken');
    localStorage.removeItem('user');
    this.currentUserSubject.next(null);
    this.router.navigate(['/login']);
  }

  getAccessToken(): string | null {
    return localStorage.getItem('accessToken');
  }

  isAuthenticated(): boolean {
    return !!this.getAccessToken();
  }

  getCurrentUser(): User | null {
    return this.currentUserSubject.value;
  }

  private getDeviceId(): string {
    let deviceId = localStorage.getItem('deviceId');
    if (!deviceId) {
      deviceId = 'WEB_' + Math.random().toString(36).substring(7);
      localStorage.setItem('deviceId', deviceId);
    }
    return deviceId;
  }
}
```

### 2.2 Login Component

```typescript
// src/app/pages/login/login.component.ts
import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent {
  step: 'phone' | 'otp' = 'phone';
  phoneForm: FormGroup;
  otpForm: FormGroup;
  loading = false;
  error: string | null = null;
  resendTimer = 0;
  private timerInterval: any;

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router
  ) {
    this.phoneForm = this.fb.group({
      phoneNumber: ['', [
        Validators.required,
        Validators.pattern(/^\+91[6-9]\d{9}$/)
      ]]
    });

    this.otpForm = this.fb.group({
      otp: ['', [
        Validators.required,
        Validators.pattern(/^\d{6}$/)
      ]]
    });
  }

  sendOtp(): void {
    if (this.phoneForm.invalid) return;

    this.loading = true;
    this.error = null;

    const phoneNumber = this.phoneForm.value.phoneNumber;

    this.authService.sendOtp(phoneNumber).subscribe({
      next: () => {
        this.step = 'otp';
        this.loading = false;
        this.startResendTimer();
      },
      error: (err) => {
        this.error = err.error.message || 'Failed to send OTP';
        this.loading = false;
      }
    });
  }

  verifyOtp(): void {
    if (this.otpForm.invalid) return;

    this.loading = true;
    this.error = null;

    const phoneNumber = this.phoneForm.value.phoneNumber;
    const otp = this.otpForm.value.otp;
    const deviceType = 'WEB';

    this.authService.verifyOtp(phoneNumber, otp, deviceType).subscribe({
      next: (response) => {
        this.loading = false;
        this.router.navigate(['/dashboard']);
      },
      error: (err) => {
        this.error = err.error.message || 'Invalid OTP';
        this.loading = false;
      }
    });
  }

  resendOtp(): void {
    if (this.resendTimer > 0) return;
    this.sendOtp();
  }

  private startResendTimer(): void {
    this.resendTimer = 60;
    this.timerInterval = setInterval(() => {
      this.resendTimer--;
      if (this.resendTimer === 0) {
        clearInterval(this.timerInterval);
      }
    }, 1000);
  }

  goBack(): void {
    this.step = 'phone';
    this.otpForm.reset();
    this.error = null;
  }
}
```

### 2.3 Login Template

```html
<!-- src/app/pages/login/login.component.html -->
<div class="login-container">
  <div class="login-card">
    <div class="logo">
      <img src="assets/logo.png" alt="VMV AgroMart">
      <h1>VMV AgroMart</h1>
    </div>

    <!-- Phone Number Step -->
    <div *ngIf="step === 'phone'" class="form-section">
      <h2>Welcome Back</h2>
      <p>Enter your phone number to continue</p>

      <form [formGroup]="phoneForm" (ngSubmit)="sendOtp()">
        <div class="form-group">
          <label>Phone Number</label>
          <input
            type="tel"
            formControlName="phoneNumber"
            placeholder="+919876543210"
            class="form-control"
          >
          <div class="error" *ngIf="phoneForm.get('phoneNumber')?.invalid && phoneForm.get('phoneNumber')?.touched">
            Please enter a valid Indian phone number
          </div>
        </div>

        <div class="error-message" *ngIf="error">
          {{ error }}
        </div>

        <button
          type="submit"
          class="btn-primary"
          [disabled]="phoneForm.invalid || loading"
        >
          {{ loading ? 'Sending...' : 'Send OTP' }}
        </button>
      </form>
    </div>

    <!-- OTP Step -->
    <div *ngIf="step === 'otp'" class="form-section">
      <button class="back-button" (click)="goBack()">← Back</button>

      <h2>Enter OTP</h2>
      <p>We've sent a 6-digit code to<br><strong>{{ phoneForm.value.phoneNumber }}</strong></p>

      <form [formGroup]="otpForm" (ngSubmit)="verifyOtp()">
        <div class="form-group">
          <input
            type="text"
            formControlName="otp"
            placeholder="000000"
            class="otp-input"
            maxlength="6"
            autofocus
          >
          <div class="error" *ngIf="otpForm.get('otp')?.invalid && otpForm.get('otp')?.touched">
            Please enter a valid 6-digit OTP
          </div>
        </div>

        <div class="error-message" *ngIf="error">
          {{ error }}
        </div>

        <button
          type="submit"
          class="btn-primary"
          [disabled]="otpForm.invalid || loading"
        >
          {{ loading ? 'Verifying...' : 'Verify OTP' }}
        </button>

        <div class="resend-section">
          <button
            type="button"
            class="btn-link"
            (click)="resendOtp()"
            [disabled]="resendTimer > 0"
          >
            {{ resendTimer > 0 ? 'Resend in ' + resendTimer + 's' : 'Resend OTP' }}
          </button>
        </div>
      </form>
    </div>
  </div>
</div>
```

---

## Testing Guide

### Manual Testing Steps:

1. **Send OTP:**
   ```bash
   curl -X POST http://localhost:8080/api/auth/send-otp \
     -H "Content-Type: application/json" \
     -d '{"phoneNumber": "+919876543210"}'
   ```

2. **Verify OTP:**
   ```bash
   curl -X POST http://localhost:8080/api/auth/verify-otp \
     -H "Content-Type: application/json" \
     -d '{
       "phoneNumber": "+919876543210",
       "otp": "123456",
       "deviceType": "WEB"
     }'
   ```

---

## Next Steps

1. Implement JWT authentication filter
2. Add role-based authorization
3. Create user profile management
4. Build dashboard for each user role
5. Implement password-less refresh token flow

---

**Document Version:** 1.0
**Implementation Priority:** HIGH
**Estimated Development Time:** 3-4 days
