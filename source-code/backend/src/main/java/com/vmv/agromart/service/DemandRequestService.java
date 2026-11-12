package com.vmv.agromart.service;

import com.vmv.agromart.dto.DemandRequestCreateRequest;
import com.vmv.agromart.dto.DemandRequestRespondRequest;
import com.vmv.agromart.dto.SellRequestCreateRequest;
import com.vmv.agromart.exception.BadRequestException;
import com.vmv.agromart.exception.ForbiddenException;
import com.vmv.agromart.exception.ResourceNotFoundException;
import com.vmv.agromart.model.DemandRequest;
import com.vmv.agromart.model.DemandRequest.DemandRequestStatus;
import com.vmv.agromart.model.SellRequest;
import com.vmv.agromart.model.User;
import com.vmv.agromart.model.User.UserRole;
import com.vmv.agromart.repository.DemandRequestRepository;
import com.vmv.agromart.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Lets City Admins / Super Admin raise a produce demand (e.g. driven by customer/retailer
 * requests for a product that's out of stock, needed by a certain date) either to one
 * named farmer or broadcast to every farmer in a city. Farmers can accept (which
 * auto-creates a normal SellRequest so it flows through the existing approval pipeline)
 * or decline.
 */
@Service
@RequiredArgsConstructor
public class DemandRequestService {

    private final DemandRequestRepository demandRequestRepository;
    private final UserRepository userRepository;
    private final SellRequestService sellRequestService;

    public DemandRequest create(User admin, DemandRequestCreateRequest request) {
        if (admin.getRole() != UserRole.CITY_ADMIN && admin.getRole() != UserRole.SUPER_ADMIN) {
            throw new ForbiddenException("This action requires role CITY_ADMIN or SUPER_ADMIN");
        }

        User targetFarmer = null;
        String city;

        if (request.getTargetFarmerId() != null) {
            targetFarmer = userRepository.findById(request.getTargetFarmerId())
                    .orElseThrow(() -> new ResourceNotFoundException("Farmer not found: " + request.getTargetFarmerId()));
            if (targetFarmer.getRole() != UserRole.FARMER) {
                throw new BadRequestException("Target user is not a farmer");
            }
            if (admin.getRole() == UserRole.CITY_ADMIN
                    && (admin.getCity() == null || !admin.getCity().equalsIgnoreCase(targetFarmer.getCity() == null ? "" : targetFarmer.getCity()))) {
                throw new ForbiddenException("This farmer is not in your city");
            }
            city = targetFarmer.getCity();
        } else {
            city = admin.getRole() == UserRole.CITY_ADMIN ? admin.getCity() : request.getCity();
            if (city == null || city.isBlank()) {
                throw new BadRequestException("A target farmer or a city is required");
            }
        }

        DemandRequest demandRequest = new DemandRequest();
        demandRequest.setRaisedBy(admin);
        demandRequest.setTargetFarmer(targetFarmer);
        demandRequest.setCity(city);
        demandRequest.setProductName(request.getProductName());
        demandRequest.setCategory(request.getCategory());
        demandRequest.setUnitOfMeasure(request.getUnitOfMeasure());
        demandRequest.setQuantity(request.getQuantity());
        demandRequest.setOfferedPricePerUnit(request.getOfferedPricePerUnit());
        demandRequest.setNeededByDate(request.getNeededByDate());
        demandRequest.setNotes(request.getNotes());
        demandRequest.setStatus(DemandRequestStatus.OPEN);
        return demandRequestRepository.save(demandRequest);
    }

    // Requests addressed directly to this farmer, plus open broadcasts for their city.
    public List<DemandRequest> forFarmer(User farmer) {
        requireRole(farmer, UserRole.FARMER);
        List<DemandRequest> direct = demandRequestRepository.findByTargetFarmerOrderByCreatedAtDesc(farmer);
        if (farmer.getCity() != null && !farmer.getCity().isBlank()) {
            List<DemandRequest> broadcast = demandRequestRepository
                    .findByCityIgnoreCaseAndTargetFarmerIsNullOrderByCreatedAtDesc(farmer.getCity());
            direct = new java.util.ArrayList<>(direct);
            direct.addAll(broadcast);
            direct.sort((a, b) -> b.getCreatedAt().compareTo(a.getCreatedAt()));
        }
        return direct;
    }

    public DemandRequest respond(User farmer, Long id, DemandRequestRespondRequest request) {
        requireRole(farmer, UserRole.FARMER);
        DemandRequest demandRequest = get(id);

        boolean addressedToMe = demandRequest.getTargetFarmer() != null
                && demandRequest.getTargetFarmer().getId().equals(farmer.getId());
        boolean broadcastToMyCity = demandRequest.getTargetFarmer() == null
                && demandRequest.getCity() != null
                && demandRequest.getCity().equalsIgnoreCase(farmer.getCity() == null ? "" : farmer.getCity());
        if (!addressedToMe && !broadcastToMyCity) {
            throw new ForbiddenException("This demand request is not addressed to you");
        }
        if (demandRequest.getStatus() != DemandRequestStatus.OPEN) {
            throw new BadRequestException("This demand request is no longer open");
        }

        demandRequest.setResponseNote(request.getNote());
        demandRequest.setRespondedAt(LocalDateTime.now());

        if (Boolean.TRUE.equals(request.getAccept())) {
            SellRequestCreateRequest sellRequestPayload = new SellRequestCreateRequest();
            sellRequestPayload.setProductName(demandRequest.getProductName());
            sellRequestPayload.setCategory(demandRequest.getCategory());
            sellRequestPayload.setUnitOfMeasure(demandRequest.getUnitOfMeasure());
            sellRequestPayload.setQuantity(demandRequest.getQuantity());
            java.math.BigDecimal proposedPrice = request.getProposedPricePerUnit() != null
                    ? request.getProposedPricePerUnit()
                    : demandRequest.getOfferedPricePerUnit() != null
                            ? demandRequest.getOfferedPricePerUnit()
                            : java.math.BigDecimal.ZERO;
            sellRequestPayload.setProposedPricePerUnit(proposedPrice);
            sellRequestPayload.setDescription("Fulfilling demand request raised by "
                    + demandRequest.getRaisedBy().getFullName() + " (needed by " + demandRequest.getNeededByDate() + ")");
            SellRequest sellRequest = sellRequestService.create(farmer, sellRequestPayload);

            demandRequest.setStatus(DemandRequestStatus.ACCEPTED);
            demandRequest.setTargetFarmer(farmer);
            demandRequest.setLinkedSellRequestId(sellRequest.getId());
        } else {
            demandRequest.setStatus(DemandRequestStatus.DECLINED);
        }

        return demandRequestRepository.save(demandRequest);
    }

    public List<DemandRequest> createdByMe(User admin) {
        if (admin.getRole() != UserRole.CITY_ADMIN && admin.getRole() != UserRole.SUPER_ADMIN) {
            throw new ForbiddenException("This action requires role CITY_ADMIN or SUPER_ADMIN");
        }
        return demandRequestRepository.findByRaisedByOrderByCreatedAtDesc(admin);
    }

    public List<DemandRequest> all(User superAdmin) {
        requireRole(superAdmin, UserRole.SUPER_ADMIN);
        return demandRequestRepository.findAllByOrderByCreatedAtDesc();
    }

    public DemandRequest cancel(User admin, Long id) {
        DemandRequest demandRequest = get(id);
        boolean isOwner = demandRequest.getRaisedBy().getId().equals(admin.getId());
        boolean isSuperAdmin = admin.getRole() == UserRole.SUPER_ADMIN;
        if (!isOwner && !isSuperAdmin) {
            throw new ForbiddenException("Only the admin who raised this request (or a super admin) can cancel it");
        }
        if (demandRequest.getStatus() != DemandRequestStatus.OPEN) {
            throw new BadRequestException("Only open demand requests can be cancelled");
        }
        demandRequest.setStatus(DemandRequestStatus.CANCELLED);
        return demandRequestRepository.save(demandRequest);
    }

    public DemandRequest get(Long id) {
        return demandRequestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Demand request not found: " + id));
    }

    private void requireRole(User user, UserRole role) {
        if (user.getRole() != role) {
            throw new ForbiddenException("This action requires role " + role);
        }
    }
}
