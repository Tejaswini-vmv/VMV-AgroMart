package com.vmv.agromart.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Represents a farmer's request to sell a batch of produce.
 * Lifecycle: SUBMITTED -> VISIT_SCHEDULED -> APPROVED -> PURCHASED -> LISTED
 * (city admin can REJECT at any point before PURCHASED)
 */
@Entity
@Table(name = "sell_requests")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SellRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "farmer_id", nullable = false)
    private User farmer;

    @Column(name = "product_name", nullable = false, length = 100)
    private String productName;

    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false, length = 30)
    private Product.Category category;

    @Enumerated(EnumType.STRING)
    @Column(name = "unit_of_measure", nullable = false, length = 20)
    private Product.UnitOfMeasure unitOfMeasure;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "quantity", nullable = false, precision = 10, scale = 2)
    private BigDecimal quantity;

    @Column(name = "proposed_price_per_unit", nullable = false, precision = 10, scale = 2)
    private BigDecimal proposedPricePerUnit;

    @Column(name = "product_image_url", length = 255)
    private String productImageUrl;

    @Column(name = "is_organic", nullable = false)
    private Boolean isOrganic = false;

    @Column(name = "harvest_date")
    private LocalDate harvestDate;

    @Column(name = "city", length = 50)
    private String city;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private SellRequestStatus status = SellRequestStatus.SUBMITTED;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "city_admin_id")
    private User cityAdmin;

    @Column(name = "visit_date")
    private LocalDate visitDate;

    @Column(name = "visit_notes", columnDefinition = "TEXT")
    private String visitNotes;

    @Column(name = "purchase_date")
    private LocalDate purchaseDate;

    @Column(name = "agreed_price_per_unit", precision = 10, scale = 2)
    private BigDecimal agreedPricePerUnit;

    @Column(name = "rejection_reason", columnDefinition = "TEXT")
    private String rejectionReason;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "reviewed_by_super_admin_id")
    private User reviewedBySuperAdmin;

    @Column(name = "linked_product_id")
    private Long linkedProductId;

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

    public enum SellRequestStatus {
        SUBMITTED,
        VISIT_SCHEDULED,
        APPROVED,
        REJECTED,
        PURCHASED,
        LISTED
    }
}
