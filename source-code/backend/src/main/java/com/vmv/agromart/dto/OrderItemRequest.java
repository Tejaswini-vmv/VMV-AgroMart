package com.vmv.agromart.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderItemRequest {

    @NotNull
    private Long productId;

    @NotNull
    private BigDecimal quantity;
}
