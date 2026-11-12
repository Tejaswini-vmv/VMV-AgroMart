package com.vmv.agromart.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RejectSellRequest {

    @NotBlank
    private String reason;
}
