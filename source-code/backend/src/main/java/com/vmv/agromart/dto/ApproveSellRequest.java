package com.vmv.agromart.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class ApproveSellRequest {

    @NotNull
    private LocalDate purchaseDate;

    @NotNull
    private BigDecimal agreedPricePerUnit;
}
