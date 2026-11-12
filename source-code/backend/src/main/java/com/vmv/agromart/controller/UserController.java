package com.vmv.agromart.controller;

import com.vmv.agromart.dto.ApiResponse;
import com.vmv.agromart.dto.FarmerSummary;
import com.vmv.agromart.security.UserPrincipal;
import com.vmv.agromart.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // City Admin: farmers in their own city. Super Admin: every farmer on the platform.
    @GetMapping("/farmers")
    public ApiResponse<List<FarmerSummary>> farmers(@AuthenticationPrincipal UserPrincipal principal) {
        return ApiResponse.ok(userService.listFarmers(principal.getUser()));
    }
}
