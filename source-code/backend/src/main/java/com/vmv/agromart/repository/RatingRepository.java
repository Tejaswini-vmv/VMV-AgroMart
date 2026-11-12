package com.vmv.agromart.repository;

import com.vmv.agromart.model.Product;
import com.vmv.agromart.model.Rating;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RatingRepository extends JpaRepository<Rating, Long> {
    List<Rating> findByProduct(Product product);
}
