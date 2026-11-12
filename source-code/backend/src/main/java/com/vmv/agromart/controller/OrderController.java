package com.vmv.agromart.controller;

import com.vmv.agromart.dto.ApiResponse;
import com.vmv.agromart.dto.OrderRequest;
import com.vmv.agromart.model.Order;
import com.vmv.agromart.security.UserPrincipal;
import com.vmv.agromart.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping
    public ApiResponse<List<Order>> myOrders(@AuthenticationPrincipal UserPrincipal principal) {
        return ApiResponse.ok(orderService.myOrders(principal.getUser()));
    }

    @GetMapping("/all")
    public ApiResponse<List<Order>> allOrders(@AuthenticationPrincipal UserPrincipal principal) {
        return ApiResponse.ok(orderService.allOrders(principal.getUser()));
    }

    @GetMapping("/city")
    public ApiResponse<List<Order>> ordersForCity(@AuthenticationPrincipal UserPrincipal principal) {
        return ApiResponse.ok(orderService.ordersForCity(principal.getUser()));
    }

    @GetMapping("/{id}")
    public ApiResponse<Order> get(@PathVariable Long id) {
        return ApiResponse.ok(orderService.get(id));
    }

    @PostMapping
    public ApiResponse<Order> create(@AuthenticationPrincipal UserPrincipal principal,
                                      @Valid @RequestBody OrderRequest request) {
        return ApiResponse.ok("Order placed", orderService.placeOrder(principal.getUser(), request));
    }

    @PutMapping("/{id}/status")
    public ApiResponse<Order> updateStatus(@AuthenticationPrincipal UserPrincipal principal,
                                            @PathVariable Long id, @RequestBody Map<String, String> body) {
        Order.OrderStatus status = Order.OrderStatus.valueOf(body.get("status").toUpperCase());
        return ApiResponse.ok("Order status updated", orderService.updateStatus(principal.getUser(), id, status));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Order> cancel(@AuthenticationPrincipal UserPrincipal principal, @PathVariable Long id) {
        return ApiResponse.ok("Order cancelled", orderService.cancel(principal.getUser(), id));
    }
}
