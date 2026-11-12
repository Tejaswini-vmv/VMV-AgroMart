package com.vmv.agromart.dto;

import com.vmv.agromart.model.Rating.RatingType;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RatingRequest {

    @NotNull
    private Long orderId;

    private Long productId;

    @NotNull
    private RatingType ratingType = RatingType.PRODUCT;

    @NotNull
    @Min(1)
    @Max(5)
    private Integer ratingValue;

    private String feedbackText;
}
