package com.vmv.agromart.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class DemandRequestRespondRequest {

    @NotNull
    private Boolean accept;

    private String note;

    // Only needed when accepting: the farmer's proposed price per unit for the
    // auto-created sell request (defaults to the admin's offered price if omitted).
    private java.math.BigDecimal proposedPricePerUnit;
}
