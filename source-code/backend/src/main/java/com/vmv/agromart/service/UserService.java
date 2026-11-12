package com.vmv.agromart.service;

import com.vmv.agromart.dto.FarmerSummary;
import com.vmv.agromart.exception.BadRequestException;
import com.vmv.agromart.exception.ForbiddenException;
import com.vmv.agromart.model.SellRequest;
import com.vmv.agromart.model.User;
import com.vmv.agromart.model.User.UserRole;
import com.vmv.agromart.repository.SellRequestRepository;
import com.vmv.agromart.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Gives City Admins and Super Admins visibility into the farmers on the platform,
 * so the whole Farmer -> City Admin -> Super Admin chain can be tracked correctly:
 * City Admins see the farmers in their own city, Super Admin sees every farmer.
 */
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final SellRequestRepository sellRequestRepository;

    public List<FarmerSummary> listFarmers(User admin) {
        List<User> farmers;
        if (admin.getRole() == UserRole.SUPER_ADMIN) {
            farmers = userRepository.findByRoleOrderByFullNameAsc(UserRole.FARMER);
        } else if (admin.getRole() == UserRole.CITY_ADMIN || admin.getRole() == UserRole.STORAGE_ADMIN) {
            if (admin.getCity() == null || admin.getCity().isBlank()) {
                throw new BadRequestException("Your account has no city assigned; contact super admin");
            }
            farmers = userRepository.findByRoleAndCityIgnoreCaseOrderByFullNameAsc(UserRole.FARMER, admin.getCity());
        } else {
            throw new ForbiddenException("This action requires role CITY_ADMIN, STORAGE_ADMIN, or SUPER_ADMIN");
        }

        return farmers.stream().map(farmer -> {
            List<SellRequest> requests = sellRequestRepository.findByFarmerOrderByCreatedAtDesc(farmer);
            long listed = requests.stream().filter(r -> r.getStatus() == SellRequest.SellRequestStatus.LISTED).count();
            long rejected = requests.stream().filter(r -> r.getStatus() == SellRequest.SellRequestStatus.REJECTED).count();
            long pending = requests.stream().filter(r ->
                    r.getStatus() == SellRequest.SellRequestStatus.SUBMITTED
                            || r.getStatus() == SellRequest.SellRequestStatus.VISIT_SCHEDULED
                            || r.getStatus() == SellRequest.SellRequestStatus.APPROVED
                            || r.getStatus() == SellRequest.SellRequestStatus.PURCHASED
            ).count();
            return new FarmerSummary(farmer, requests.size(), listed, pending, rejected);
        }).toList();
    }
}
