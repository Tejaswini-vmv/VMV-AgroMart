package com.vmv.agromart.dto;

import com.vmv.agromart.model.Product;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class ProductRequest {

    @NotBlank
    private String productName;

    @NotNull
    private Product.Category category;

    private String description;

    @NotNull
    private Product.UnitOfMeasure unitOfMeasure;

    @NotNull
    private BigDecimal pricePerUnit;

    @NotNull
    private BigDecimal availableQuantity;

    private BigDecimal minimumOrderQuantity;
    private String productImageUrl;
    private Boolean isOrganic;
    private LocalDate harvestDate;
}
