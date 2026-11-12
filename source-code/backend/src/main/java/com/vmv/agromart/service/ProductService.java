package com.vmv.agromart.service;

import com.vmv.agromart.dto.ProductRequest;
import com.vmv.agromart.exception.ForbiddenException;
import com.vmv.agromart.exception.ResourceNotFoundException;
import com.vmv.agromart.model.Product;
import com.vmv.agromart.model.User;
import com.vmv.agromart.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public List<Product> listAvailable() {
        return productRepository.findByIsAvailableTrue();
    }

    public List<Product> search(String query) {
        return productRepository.findByProductNameContainingIgnoreCaseAndIsAvailableTrue(query);
    }

    public List<Product> byCategory(Product.Category category) {
        return productRepository.findByCategoryAndIsAvailableTrue(category);
    }

    public Product get(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found: " + id));
    }

    public List<Product> myProducts(User farmer) {
        return productRepository.findByFarmer(farmer);
    }

    public Product create(User farmer, ProductRequest request) {
        if (farmer.getRole() != User.UserRole.FARMER && farmer.getRole() != User.UserRole.SUPER_ADMIN) {
            throw new ForbiddenException("Only farmers or super admins can list products");
        }
        Product product = new Product();
        product.setFarmer(farmer);
        product.setProductName(request.getProductName());
        product.setCategory(request.getCategory());
        product.setDescription(request.getDescription());
        product.setUnitOfMeasure(request.getUnitOfMeasure());
        product.setPricePerUnit(request.getPricePerUnit());
        product.setAvailableQuantity(request.getAvailableQuantity());
        product.setMinimumOrderQuantity(request.getMinimumOrderQuantity() != null ? request.getMinimumOrderQuantity() : BigDecimal.ONE);
        product.setProductImageUrl(request.getProductImageUrl());
        product.setIsOrganic(Boolean.TRUE.equals(request.getIsOrganic()));
        product.setHarvestDate(request.getHarvestDate());
        product.setIsAvailable(true);
        return productRepository.save(product);
    }

    public Product update(User user, Long productId, ProductRequest request) {
        Product product = get(productId);
        boolean isOwner = product.getFarmer().getId().equals(user.getId());
        boolean isSuperAdmin = user.getRole() == User.UserRole.SUPER_ADMIN;
        if (!isOwner && !isSuperAdmin) {
            throw new ResourceNotFoundException("Product not found for this farmer: " + productId);
        }
        product.setProductName(request.getProductName());
        product.setCategory(request.getCategory());
        product.setDescription(request.getDescription());
        product.setUnitOfMeasure(request.getUnitOfMeasure());
        product.setPricePerUnit(request.getPricePerUnit());
        product.setAvailableQuantity(request.getAvailableQuantity());
        if (request.getMinimumOrderQuantity() != null) {
            product.setMinimumOrderQuantity(request.getMinimumOrderQuantity());
        }
        product.setProductImageUrl(request.getProductImageUrl());
        product.setIsOrganic(Boolean.TRUE.equals(request.getIsOrganic()));
        product.setHarvestDate(request.getHarvestDate());
        return productRepository.save(product);
    }

    public void delete(User user, Long productId) {
        Product product = get(productId);
        boolean isOwner = product.getFarmer().getId().equals(user.getId());
        boolean isSuperAdmin = user.getRole() == User.UserRole.SUPER_ADMIN;
        if (!isOwner && !isSuperAdmin) {
            throw new ResourceNotFoundException("Product not found for this farmer: " + productId);
        }
        product.setIsAvailable(false);
        productRepository.save(product);
    }
}
