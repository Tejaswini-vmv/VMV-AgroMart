package com.vmv.agromart.service;

import com.vmv.agromart.dto.*;
import com.vmv.agromart.exception.BadRequestException;
import com.vmv.agromart.exception.ForbiddenException;
import com.vmv.agromart.exception.ResourceNotFoundException;
import com.vmv.agromart.model.Product;
import com.vmv.agromart.model.SellRequest;
import com.vmv.agromart.model.SellRequest.SellRequestStatus;
import com.vmv.agromart.model.User;
import com.vmv.agromart.model.User.UserRole;
import com.vmv.agromart.repository.OrderRepository;
import com.vmv.agromart.repository.ProductRepository;
import com.vmv.agromart.repository.SellRequestRepository;
import com.vmv.agromart.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class SellRequestService {

    private final SellRequestRepository sellRequestRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    // ---------- FARMER ----------

    public SellRequest create(User farmer, SellRequestCreateRequest request) {
        requireRole(farmer, UserRole.FARMER);

        SellRequest sellRequest = new SellRequest();
        sellRequest.setFarmer(farmer);
        sellRequest.setProductName(request.getProductName());
        sellRequest.setCategory(request.getCategory());
        sellRequest.setUnitOfMeasure(request.getUnitOfMeasure());
        sellRequest.setDescription(request.getDescription());
        sellRequest.setQuantity(request.getQuantity());
        sellRequest.setProposedPricePerUnit(request.getProposedPricePerUnit());
        sellRequest.setProductImageUrl(request.getProductImageUrl());
        sellRequest.setIsOrganic(Boolean.TRUE.equals(request.getIsOrganic()));
        sellRequest.setHarvestDate(request.getHarvestDate());
        sellRequest.setCity(farmer.getCity());
        sellRequest.setStatus(SellRequestStatus.SUBMITTED);
        return sellRequestRepository.save(sellRequest);
    }

    public List<SellRequest> myRequests(User farmer) {
        requireRole(farmer, UserRole.FARMER);
        return sellRequestRepository.findByFarmerOrderByCreatedAtDesc(farmer);
    }

    // ---------- CITY ADMIN ----------

    public List<SellRequest> forCity(User cityAdmin, String statusFilter) {
        if (cityAdmin.getRole() != UserRole.CITY_ADMIN && cityAdmin.getRole() != UserRole.STORAGE_ADMIN) {
            throw new ForbiddenException("This action requires role CITY_ADMIN or STORAGE_ADMIN");
        }
        String city = cityAdmin.getCity();
        if (city == null || city.isBlank()) {
            throw new BadRequestException("Your account has no city assigned; contact super admin");
        }
        if (statusFilter != null && !statusFilter.isBlank()) {
            return sellRequestRepository.findByCityAndStatusOrderByCreatedAtDesc(city, SellRequestStatus.valueOf(statusFilter.toUpperCase()));
        }
        return sellRequestRepository.findByCityOrderByCreatedAtDesc(city);
    }

    public SellRequest scheduleVisit(User cityAdmin, Long id, ScheduleVisitRequest request) {
        SellRequest sellRequest = getForCityAdmin(cityAdmin, id);
        if (sellRequest.getStatus() != SellRequestStatus.SUBMITTED && sellRequest.getStatus() != SellRequestStatus.VISIT_SCHEDULED) {
            throw new BadRequestException("Cannot schedule a visit for a request in status " + sellRequest.getStatus());
        }
        sellRequest.setCityAdmin(cityAdmin);
        sellRequest.setVisitDate(request.getVisitDate());
        sellRequest.setVisitNotes(request.getVisitNotes());
        sellRequest.setStatus(SellRequestStatus.VISIT_SCHEDULED);
        return sellRequestRepository.save(sellRequest);
    }

    public SellRequest approve(User cityAdmin, Long id, ApproveSellRequest request) {
        SellRequest sellRequest = getForCityAdmin(cityAdmin, id);
        if (sellRequest.getStatus() != SellRequestStatus.SUBMITTED && sellRequest.getStatus() != SellRequestStatus.VISIT_SCHEDULED) {
            throw new BadRequestException("Cannot approve a request in status " + sellRequest.getStatus());
        }
        sellRequest.setCityAdmin(cityAdmin);
        sellRequest.setPurchaseDate(request.getPurchaseDate());
        sellRequest.setAgreedPricePerUnit(request.getAgreedPricePerUnit());
        sellRequest.setStatus(SellRequestStatus.APPROVED);
        return sellRequestRepository.save(sellRequest);
    }

    public SellRequest reject(User cityAdmin, Long id, RejectSellRequest request) {
        SellRequest sellRequest = getForCityAdmin(cityAdmin, id);
        if (sellRequest.getStatus() == SellRequestStatus.PURCHASED || sellRequest.getStatus() == SellRequestStatus.LISTED) {
            throw new BadRequestException("Cannot reject a request already purchased/listed");
        }
        sellRequest.setCityAdmin(cityAdmin);
        sellRequest.setRejectionReason(request.getReason());
        sellRequest.setStatus(SellRequestStatus.REJECTED);
        return sellRequestRepository.save(sellRequest);
    }

    public SellRequest buy(User cityAdmin, Long id) {
        SellRequest sellRequest = getForCityAdmin(cityAdmin, id);
        if (sellRequest.getStatus() != SellRequestStatus.APPROVED) {
            throw new BadRequestException("Request must be APPROVED before it can be purchased");
        }
        sellRequest.setStatus(SellRequestStatus.PURCHASED);
        return sellRequestRepository.save(sellRequest);
    }

    private SellRequest getForCityAdmin(User cityAdmin, Long id) {
        requireRole(cityAdmin, UserRole.CITY_ADMIN);
        SellRequest sellRequest = get(id);
        if (cityAdmin.getCity() == null || !cityAdmin.getCity().equalsIgnoreCase(sellRequest.getCity() == null ? "" : sellRequest.getCity())) {
            throw new ForbiddenException("This request does not belong to your city");
        }
        return sellRequest;
    }

    // ---------- SUPER ADMIN ----------

    public List<SellRequest> incoming(User superAdmin) {
        requireRole(superAdmin, UserRole.SUPER_ADMIN);
        return sellRequestRepository.findByStatusOrderByCreatedAtDesc(SellRequestStatus.PURCHASED);
    }

    public SellRequest listToCatalog(User superAdmin, Long id) {
        requireRole(superAdmin, UserRole.SUPER_ADMIN);
        SellRequest sellRequest = get(id);
        if (sellRequest.getStatus() != SellRequestStatus.PURCHASED) {
            throw new BadRequestException("Only PURCHASED requests can be listed to the catalog");
        }

        Product product = new Product();
        product.setFarmer(sellRequest.getFarmer());
        product.setProductName(sellRequest.getProductName());
        product.setCategory(sellRequest.getCategory());
        product.setDescription(sellRequest.getDescription());
        product.setUnitOfMeasure(sellRequest.getUnitOfMeasure());
        product.setPricePerUnit(sellRequest.getAgreedPricePerUnit() != null ? sellRequest.getAgreedPricePerUnit() : sellRequest.getProposedPricePerUnit());
        product.setAvailableQuantity(sellRequest.getQuantity());
        product.setMinimumOrderQuantity(BigDecimal.ONE);
        product.setProductImageUrl(sellRequest.getProductImageUrl());
        product.setIsOrganic(Boolean.TRUE.equals(sellRequest.getIsOrganic()));
        product.setHarvestDate(sellRequest.getHarvestDate());
        product.setIsAvailable(true);
        // Traceability: record which city admin sourced this produce and from which sell
        // request, so any order placed against this product can be traced back to the
        // farmer, the city admin who purchased it, and the sell request itself.
        product.setSourceCityAdmin(sellRequest.getCityAdmin());
        product.setSourceSellRequestId(sellRequest.getId());
        product = productRepository.save(product);

        sellRequest.setLinkedProductId(product.getId());
        sellRequest.setReviewedBySuperAdmin(superAdmin);
        sellRequest.setStatus(SellRequestStatus.LISTED);
        return sellRequestRepository.save(sellRequest);
    }

    public List<SellRequest> all(User superAdmin, String statusFilter) {
        requireRole(superAdmin, UserRole.SUPER_ADMIN);
        if (statusFilter != null && !statusFilter.isBlank()) {
            return sellRequestRepository.findByStatusOrderByCreatedAtDesc(SellRequestStatus.valueOf(statusFilter.toUpperCase()));
        }
        return sellRequestRepository.findAllByOrderByCreatedAtDesc();
    }

    public SellRequestDashboard dashboard(User superAdmin) {
        requireRole(superAdmin, UserRole.SUPER_ADMIN);

        List<SellRequest> all = sellRequestRepository.findAll();
        Map<String, Long> byStatus = new LinkedHashMap<>();
        for (SellRequestStatus status : SellRequestStatus.values()) {
            byStatus.put(status.name(), 0L);
        }
        for (SellRequest sr : all) {
            byStatus.merge(sr.getStatus().name(), 1L, Long::sum);
        }

        BigDecimal totalRevenue = orderRepository.findAll().stream()
                .map(o -> o.getTotalAmount() != null ? o.getTotalAmount() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new SellRequestDashboard(
                all.size(),
                byStatus,
                productRepository.findByIsAvailableTrue().size(),
                userRepository.findAll().size(),
                orderRepository.findAll().size(),
                totalRevenue
        );
    }

    // ---------- shared ----------

    public SellRequest get(Long id) {
        return sellRequestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sell request not found: " + id));
    }

    private void requireRole(User user, UserRole role) {
        if (user.getRole() != role) {
            throw new ForbiddenException("This action requires role " + role);
        }
    }
}
