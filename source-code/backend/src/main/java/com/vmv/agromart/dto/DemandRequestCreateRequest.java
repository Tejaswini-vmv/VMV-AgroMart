package com.vmv.agromart.dto;

import com.vmv.agromart.model.Product;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class DemandRequestCreateRequest {

    // Optional: address a specific farmer. If omitted, request is broadcast to every
    // farmer in the raising admin's city (or the given city, for a super admin).
    private Long targetFarmerId;

    // Only used by SUPER_ADMIN when broadcasting (city admins always use their own city).
    private String city;

    @NotBlank
    private String productName;

    @NotNull
    private Product.Category category;

    @NotNull
    private Product.UnitOfMeasure unitOfMeasure;

    @NotNull
    private BigDecimal quantity;

    private BigDecimal offeredPricePerUnit;

    @NotNull
    private LocalDate neededByDate;

    private String notes;
}
