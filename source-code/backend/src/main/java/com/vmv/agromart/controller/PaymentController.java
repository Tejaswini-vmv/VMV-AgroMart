package com.vmv.agromart.controller;

import com.vmv.agromart.dto.ApiResponse;
import com.vmv.agromart.dto.PaymentResponse;
import com.vmv.agromart.security.UserPrincipal;
import com.vmv.agromart.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/create")
    public ApiResponse<PaymentResponse> create(
            @AuthenticationPrincipal UserPrincipal principal,
            @RequestBody Map<String, Long> body) {
        return ApiResponse.ok(
            PaymentResponse.from(
                paymentService.createPayment(principal.getUser(), body.get("orderId"))
            )
        );
    }

    @PostMapping("/{id}/verify")
    public ApiResponse<PaymentResponse> verify(@PathVariable Long id) {
        return ApiResponse.ok(
            "Payment verified",
            PaymentResponse.from(paymentService.verifyPayment(id))
        );
    }

    @GetMapping("/{id}")
    public ApiResponse<PaymentResponse> get(@PathVariable Long id) {
        return ApiResponse.ok(PaymentResponse.from(paymentService.get(id)));
    }
}
