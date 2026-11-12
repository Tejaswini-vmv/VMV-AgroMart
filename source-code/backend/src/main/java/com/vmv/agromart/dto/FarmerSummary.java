package com.vmv.agromart.dto;

import com.vmv.agromart.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FarmerSummary {
    private User farmer;
    private long totalSellRequests;
    private long listedProducts;
    private long pendingRequests;
    private long rejectedRequests;
}
