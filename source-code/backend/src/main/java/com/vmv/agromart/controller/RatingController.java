package com.vmv.agromart.controller;

import com.vmv.agromart.dto.ApiResponse;
import com.vmv.agromart.dto.RatingRequest;
import com.vmv.agromart.model.Rating;
import com.vmv.agromart.security.UserPrincipal;
import com.vmv.agromart.service.RatingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ratings")
@RequiredArgsConstructor
public class RatingController {

    private final RatingService ratingService;

    @PostMapping
    public ApiResponse<Rating> create(@AuthenticationPrincipal UserPrincipal principal,
                                       @Valid @RequestBody RatingRequest request) {
        return ApiResponse.ok("Feedback submitted", ratingService.create(principal.getUser(), request));
    }

    @GetMapping("/product/{productId}")
    public ApiResponse<List<Rating>> forProduct(@PathVariable Long productId) {
        return ApiResponse.ok(ratingService.forProduct(productId));
    }
}
