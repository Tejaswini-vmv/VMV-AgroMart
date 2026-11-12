package com.vmv.agromart.dto;

import com.vmv.agromart.model.Product;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class SellRequestCreateRequest {

    @NotBlank
    private String productName;

    @NotNull
    private Product.Category category;

    @NotNull
    private Product.UnitOfMeasure unitOfMeasure;

    private String description;

    @NotNull
    private BigDecimal quantity;

    @NotNull
    private BigDecimal proposedPricePerUnit;

    private String productImageUrl;
    private Boolean isOrganic;
    private LocalDate harvestDate;
}
