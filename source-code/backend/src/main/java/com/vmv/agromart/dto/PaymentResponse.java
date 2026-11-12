package com.vmv.agromart.dto;

import com.vmv.agromart.model.Order;
import com.vmv.agromart.model.Payment;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class PaymentResponse {
    private Long id;
    private Long orderId;
    private Long userId;
    private String transactionId;
    private String paymentGateway;
    private Order.PaymentMethod paymentMethod;
    private BigDecimal amount;
    private Payment.Status paymentStatus;
    private String gatewayOrderId;
    private String failureReason;
    private LocalDateTime paidAt;
    private LocalDateTime createdAt;

    public static PaymentResponse from(Payment payment) {
        return new PaymentResponse(
            payment.getId(),
            payment.getOrder() != null ? payment.getOrder().getId() : null,
            payment.getUser() != null ? payment.getUser().getId() : null,
            payment.getTransactionId(),
            payment.getPaymentGateway(),
            payment.getPaymentMethod(),
            payment.getAmount(),
            payment.getPaymentStatus(),
            payment.getGatewayOrderId(),
            payment.getFailureReason(),
            payment.getPaidAt(),
            payment.getCreatedAt()
        );
    }
}
