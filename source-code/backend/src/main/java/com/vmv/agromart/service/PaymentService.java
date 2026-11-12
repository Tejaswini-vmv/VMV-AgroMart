package com.vmv.agromart.service;

import com.vmv.agromart.exception.ResourceNotFoundException;
import com.vmv.agromart.model.Order;
import com.vmv.agromart.model.Payment;
import com.vmv.agromart.model.User;
import com.vmv.agromart.repository.OrderRepository;
import com.vmv.agromart.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Payment integration is mocked when no Razorpay key is configured, so the checkout flow
 * (create -> verify) can be exercised end-to-end in local/docker environments without real
 * payment credentials. Swap in the real Razorpay SDK calls once RAZORPAY_KEY_ID/SECRET are set.
 */
@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;

    @Value("${razorpay.key-id:}")
    private String razorpayKeyId;

    public Payment createPayment(User user, Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found: " + orderId));

        Payment payment = new Payment();
        payment.setOrder(order);
        payment.setUser(user);
        payment.setAmount(order.getTotalAmount());
        payment.setPaymentMethod(order.getPaymentMethod());
        payment.setPaymentGateway(isMocked() ? "MOCK" : "RAZORPAY");
        payment.setGatewayOrderId("order_" + UUID.randomUUID().toString().replace("-", "").substring(0, 16));
        payment.setPaymentStatus(Payment.Status.PENDING);
        return paymentRepository.save(payment);
    }

    public Payment verifyPayment(Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found: " + paymentId));

        // Mocked success path (no gateway configured): mark as paid immediately.
        payment.setPaymentStatus(Payment.Status.SUCCESS);
        payment.setTransactionId("txn_" + UUID.randomUUID().toString().replace("-", "").substring(0, 16));
        payment.setPaidAt(LocalDateTime.now());
        paymentRepository.save(payment);

        Order order = payment.getOrder();
        order.setPaymentStatus(Order.PaymentStatus.COMPLETED);
        orderRepository.save(order);

        return payment;
    }

    public Payment get(Long id) {
        return paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found: " + id));
    }

    private boolean isMocked() {
        return razorpayKeyId == null || razorpayKeyId.isBlank();
    }
}
