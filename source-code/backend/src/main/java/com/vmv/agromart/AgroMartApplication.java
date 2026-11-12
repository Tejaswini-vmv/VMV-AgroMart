package com.vmv.agromart;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * VMV AgroMart - Agricultural Marketplace Application
 *
 * Main Spring Boot Application Entry Point
 *
 * Features:
 * - Multi-role authentication (OTP-based)
 * - Product marketplace
 * - Order management
 * - GPS tracking
 * - Payment integration (Razorpay)
 * - SMS notifications (Twilio)
 *
 * @author VMV Team
 * @version 1.0.0
 */
@SpringBootApplication
@EnableCaching
@EnableAsync
@EnableScheduling
public class AgroMartApplication {

    public static void main(String[] args) {
        SpringApplication.run(AgroMartApplication.class, args);
        System.out.println("===========================================");
        System.out.println("VMV AgroMart Application Started Successfully!");
        System.out.println("API Documentation: http://localhost:8080/swagger-ui.html");
        System.out.println("Health Check: http://localhost:8080/actuator/health");
        System.out.println("===========================================");
    }
}
