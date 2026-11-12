package com.vmv.agromart.repository;

import com.vmv.agromart.model.DemandRequest;
import com.vmv.agromart.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DemandRequestRepository extends JpaRepository<DemandRequest, Long> {
    List<DemandRequest> findByRaisedByOrderByCreatedAtDesc(User raisedBy);
    List<DemandRequest> findByTargetFarmerOrderByCreatedAtDesc(User targetFarmer);
    List<DemandRequest> findByCityIgnoreCaseAndTargetFarmerIsNullOrderByCreatedAtDesc(String city);
    List<DemandRequest> findAllByOrderByCreatedAtDesc();
}
