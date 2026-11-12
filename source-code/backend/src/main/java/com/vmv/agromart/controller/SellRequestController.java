package com.vmv.agromart.controller;

import com.vmv.agromart.dto.*;
import com.vmv.agromart.model.SellRequest;
import com.vmv.agromart.security.UserPrincipal;
import com.vmv.agromart.service.SellRequestService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sell-requests")
@RequiredArgsConstructor
public class SellRequestController {

    private final SellRequestService sellRequestService;

    // ---- Farmer ----

    @PostMapping
    public ApiResponse<SellRequest> create(@AuthenticationPrincipal UserPrincipal principal,
                                            @Valid @RequestBody SellRequestCreateRequest request) {
        return ApiResponse.ok("Sell request submitted", sellRequestService.create(principal.getUser(), request));
    }

    @GetMapping("/mine")
    public ApiResponse<List<SellRequest>> mine(@AuthenticationPrincipal UserPrincipal principal) {
        return ApiResponse.ok(sellRequestService.myRequests(principal.getUser()));
    }

    // ---- City Admin ----

    @GetMapping("/city")
    public ApiResponse<List<SellRequest>> forCity(@AuthenticationPrincipal UserPrincipal principal,
                                                    @RequestParam(required = false) String status) {
        return ApiResponse.ok(sellRequestService.forCity(principal.getUser(), status));
    }

    @PutMapping("/{id}/schedule-visit")
    public ApiResponse<SellRequest> scheduleVisit(@AuthenticationPrincipal UserPrincipal principal,
                                                    @PathVariable Long id,
                                                    @Valid @RequestBody ScheduleVisitRequest request) {
        return ApiResponse.ok("Visit scheduled", sellRequestService.scheduleVisit(principal.getUser(), id, request));
    }

    @PutMapping("/{id}/approve")
    public ApiResponse<SellRequest> approve(@AuthenticationPrincipal UserPrincipal principal,
                                             @PathVariable Long id,
                                             @Valid @RequestBody ApproveSellRequest request) {
        return ApiResponse.ok("Sell request approved", sellRequestService.approve(principal.getUser(), id, request));
    }

    @PutMapping("/{id}/reject")
    public ApiResponse<SellRequest> reject(@AuthenticationPrincipal UserPrincipal principal,
                                            @PathVariable Long id,
                                            @Valid @RequestBody RejectSellRequest request) {
        return ApiResponse.ok("Sell request rejected", sellRequestService.reject(principal.getUser(), id, request));
    }

    @PutMapping("/{id}/buy")
    public ApiResponse<SellRequest> buy(@AuthenticationPrincipal UserPrincipal principal, @PathVariable Long id) {
        return ApiResponse.ok("Produce purchased and forwarded to super admin", sellRequestService.buy(principal.getUser(), id));
    }

    // ---- Super Admin ----

    @GetMapping("/incoming")
    public ApiResponse<List<SellRequest>> incoming(@AuthenticationPrincipal UserPrincipal principal) {
        return ApiResponse.ok(sellRequestService.incoming(principal.getUser()));
    }

    @PutMapping("/{id}/list")
    public ApiResponse<SellRequest> listToCatalog(@AuthenticationPrincipal UserPrincipal principal, @PathVariable Long id) {
        return ApiResponse.ok("Product listed to public catalog", sellRequestService.listToCatalog(principal.getUser(), id));
    }

    @GetMapping("/all")
    public ApiResponse<List<SellRequest>> all(@AuthenticationPrincipal UserPrincipal principal,
                                               @RequestParam(required = false) String status) {
        return ApiResponse.ok(sellRequestService.all(principal.getUser(), status));
    }

    @GetMapping("/dashboard")
    public ApiResponse<SellRequestDashboard> dashboard(@AuthenticationPrincipal UserPrincipal principal) {
        return ApiResponse.ok(sellRequestService.dashboard(principal.getUser()));
    }
}
