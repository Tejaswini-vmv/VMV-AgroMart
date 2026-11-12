package com.vmv.agromart.repository;

import com.vmv.agromart.model.Order;
import com.vmv.agromart.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByCustomerOrderByCreatedAtDesc(User customer);
    Optional<Order> findByOrderNumber(String orderNumber);
    List<Order> findAllByOrderByCreatedAtDesc();
    List<Order> findByDeliveryCityIgnoreCaseOrderByCreatedAtDesc(String deliveryCity);
}
