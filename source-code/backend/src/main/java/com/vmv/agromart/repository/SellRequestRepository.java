package com.vmv.agromart.repository;

import com.vmv.agromart.model.SellRequest;
import com.vmv.agromart.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SellRequestRepository extends JpaRepository<SellRequest, Long> {
    List<SellRequest> findByFarmerOrderByCreatedAtDesc(User farmer);
    List<SellRequest> findByCityOrderByCreatedAtDesc(String city);
    List<SellRequest> findByCityAndStatusOrderByCreatedAtDesc(String city, SellRequest.SellRequestStatus status);
    List<SellRequest> findByStatusOrderByCreatedAtDesc(SellRequest.SellRequestStatus status);
    List<SellRequest> findAllByOrderByCreatedAtDesc();
}
