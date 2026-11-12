package com.vmv.agromart.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * A demand-driven produce request raised by a City Admin or Super Admin -- e.g. because
 * customers/retailers are asking for a product that isn't currently in stock. It is sent
 * either to one specific farmer, or broadcast to every farmer in a city, who can then
 * accept (which auto-creates a normal SellRequest on their behalf) or decline.
 *
 * Lifecycle: OPEN -> ACCEPTED (-> becomes a SellRequest) | DECLINED | CANCELLED
 */
@Entity
@Table(name = "demand_requests")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DemandRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "raised_by_id", nullable = false)
    private User raisedBy;

    // Null means broadcast to every farmer in `city`; set means addressed to one farmer.
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "target_farmer_id")
    private User targetFarmer;

    @Column(name = "city", length = 50)
    private String city;

    @Column(name = "product_name", nullable = false, length = 100)
    private String productName;

    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false, length = 30)
    private Product.Category category;

    @Enumerated(EnumType.STRING)
    @Column(name = "unit_of_measure", nullable = false, length = 20)
    private Product.UnitOfMeasure unitOfMeasure;

    @Column(name = "quantity", nullable = false, precision = 10, scale = 2)
    private BigDecimal quantity;

    @Column(name = "offered_price_per_unit", precision = 10, scale = 2)
    private BigDecimal offeredPricePerUnit;

    @Column(name = "needed_by_date", nullable = false)
    private LocalDate neededByDate;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private DemandRequestStatus status = DemandRequestStatus.OPEN;

    @Column(name = "linked_sell_request_id")
    private Long linkedSellRequestId;

    @Column(name = "response_note", columnDefinition = "TEXT")
    private String responseNote;

    @Column(name = "responded_at")
    private LocalDateTime respondedAt;

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

    public enum DemandRequestStatus {
        OPEN, ACCEPTED, DECLINED, CANCELLED
    }
}
