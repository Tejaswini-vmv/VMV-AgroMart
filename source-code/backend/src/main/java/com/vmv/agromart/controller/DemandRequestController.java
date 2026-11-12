package com.vmv.agromart.controller;

import com.vmv.agromart.dto.ApiResponse;
import com.vmv.agromart.dto.DemandRequestCreateRequest;
import com.vmv.agromart.dto.DemandRequestRespondRequest;
import com.vmv.agromart.model.DemandRequest;
import com.vmv.agromart.security.UserPrincipal;
import com.vmv.agromart.service.DemandRequestService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/demand-requests")
@RequiredArgsConstructor
public class DemandRequestController {

    private final DemandRequestService demandRequestService;

    // ---- City Admin / Super Admin ----

    @PostMapping
    public ApiResponse<DemandRequest> create(@AuthenticationPrincipal UserPrincipal principal,
                                              @Valid @RequestBody DemandRequestCreateRequest request) {
        return ApiResponse.ok("Demand request raised", demandRequestService.create(principal.getUser(), request));
    }

    @GetMapping("/created")
    public ApiResponse<List<DemandRequest>> createdByMe(@AuthenticationPrincipal UserPrincipal principal) {
        return ApiResponse.ok(demandRequestService.createdByMe(principal.getUser()));
    }

    @PutMapping("/{id}/cancel")
    public ApiResponse<DemandRequest> cancel(@AuthenticationPrincipal UserPrincipal principal, @PathVariable Long id) {
        return ApiResponse.ok("Demand request cancelled", demandRequestService.cancel(principal.getUser(), id));
    }

    // ---- Super Admin: track all ----

    @GetMapping("/all")
    public ApiResponse<List<DemandRequest>> all(@AuthenticationPrincipal UserPrincipal principal) {
        return ApiResponse.ok(demandRequestService.all(principal.getUser()));
    }

    // ---- Farmer ----

    @GetMapping("/mine")
    public ApiResponse<List<DemandRequest>> mine(@AuthenticationPrincipal UserPrincipal principal) {
        return ApiResponse.ok(demandRequestService.forFarmer(principal.getUser()));
    }

    @PutMapping("/{id}/respond")
    public ApiResponse<DemandRequest> respond(@AuthenticationPrincipal UserPrincipal principal,
                                               @PathVariable Long id,
                                               @Valid @RequestBody DemandRequestRespondRequest request) {
        return ApiResponse.ok("Response recorded", demandRequestService.respond(principal.getUser(), id, request));
    }
}
