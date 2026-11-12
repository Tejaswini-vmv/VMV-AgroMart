package com.vmv.agromart.dto;

import com.vmv.agromart.model.Order;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class OrderRequest {

    @NotEmpty
    @Valid
    private List<OrderItemRequest> items;

    @NotBlank
    private String deliveryAddress;

    private String deliveryCity;
    private String deliveryPincode;
    private String deliveryContactPhone;

    @NotNull
    private Order.PaymentMethod paymentMethod;

    private String specialInstructions;

    // Optional scheduled delivery: a future date and a time slot label (e.g. "10:00-12:00").
    // If left blank, the order is delivered as soon as possible (existing behavior).
    private LocalDate requestedDeliveryDate;
    private String requestedDeliveryTimeSlot;
}
