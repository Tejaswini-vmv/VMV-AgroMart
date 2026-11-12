package com.vmv.agromart.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "products")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "farmer_id", nullable = false)
    private User farmer;

    @Column(name = "product_name", nullable = false, length = 100)
    private String productName;

    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false, length = 30)
    private Category category;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "unit_of_measure", nullable = false, length = 20)
    private UnitOfMeasure unitOfMeasure;

    @Column(name = "price_per_unit", nullable = false, precision = 10, scale = 2)
    private BigDecimal pricePerUnit;

    @Column(name = "available_quantity", nullable = false, precision = 10, scale = 2)
    private BigDecimal availableQuantity;

    @Column(name = "minimum_order_quantity", precision = 10, scale = 2)
    private BigDecimal minimumOrderQuantity = BigDecimal.ONE;

    @Column(name = "product_image_url", length = 255)
    private String productImageUrl;

    @Column(name = "is_organic", nullable = false)
    private Boolean isOrganic = false;

    @Column(name = "is_available", nullable = false)
    private Boolean isAvailable = true;

    @Column(name = "harvest_date")
    private LocalDate harvestDate;

    // Traceability: which city admin purchased/sourced this produce, and from which
    // sell request, so orders placed against this product can be traced all the way
    // back to Farmer -> City Admin -> Super Admin.
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "source_city_admin_id")
    private User sourceCityAdmin;

    @Column(name = "source_sell_request_id")
    private Long sourceSellRequestId;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public enum Category {
        VEGETABLES, FRUITS, GRAINS, PULSES, DAIRY, SPICES, HERBS, NUTS
    }

    public enum UnitOfMeasure {
        KG, GRAM, PIECE, DOZEN, LITER, QUINTAL
    }
}
