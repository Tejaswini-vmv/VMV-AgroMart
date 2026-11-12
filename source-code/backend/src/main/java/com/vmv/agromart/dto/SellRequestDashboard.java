package com.vmv.agromart.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SellRequestDashboard {
    private long totalSellRequests;
    private Map<String, Long> sellRequestsByStatus;
    private long totalProductsListed;
    private long totalUsers;
    private long totalOrders;
    private BigDecimal totalOrderRevenue;
}
