package com.vmv.agromart.repository;

import com.vmv.agromart.model.Product;
import com.vmv.agromart.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByIsAvailableTrue();
    List<Product> findByFarmer(User farmer);
    List<Product> findByCategoryAndIsAvailableTrue(Product.Category category);
    List<Product> findByProductNameContainingIgnoreCaseAndIsAvailableTrue(String name);
}
