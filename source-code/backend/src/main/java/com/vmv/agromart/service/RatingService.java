package com.vmv.agromart.service;

import com.vmv.agromart.dto.RatingRequest;
import com.vmv.agromart.exception.BadRequestException;
import com.vmv.agromart.exception.ForbiddenException;
import com.vmv.agromart.exception.ResourceNotFoundException;
import com.vmv.agromart.model.Order;
import com.vmv.agromart.model.OrderItem;
import com.vmv.agromart.model.Product;
import com.vmv.agromart.model.Rating;
import com.vmv.agromart.model.User;
import com.vmv.agromart.repository.OrderRepository;
import com.vmv.agromart.repository.ProductRepository;
import com.vmv.agromart.repository.RatingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RatingService {

    private final RatingRepository ratingRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    @Transactional
    public Rating create(User user, RatingRequest request) {
        Order order = orderRepository.findById(request.getOrderId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Order not found: " + request.getOrderId()));

        if (order.getCustomer() == null
                || !order.getCustomer().getId().equals(user.getId())) {
            throw new ForbiddenException("You can only rate your own orders");
        }

        if (order.getOrderStatus() != Order.OrderStatus.DELIVERED) {
            throw new BadRequestException("Only delivered orders can be rated");
        }

        order.getItems().size();

        Rating.RatingType ratingType = request.getRatingType();
        if (ratingType == null) {
            throw new BadRequestException("Rating type is required");
        }

        Rating rating = new Rating();
        rating.setOrder(order);
        rating.setUser(user);
        rating.setRatingType(ratingType);
        rating.setRatingValue(request.getRatingValue());
        rating.setFeedbackText(request.getFeedbackText());
        rating.setVerifiedPurchase(true);
        rating.setVisible(true);

        if (ratingType == Rating.RatingType.PRODUCT) {
            if (request.getProductId() == null) {
                throw new BadRequestException("Product ID is required for a product rating");
            }

            Product product = productRepository.findById(request.getProductId())
                    .orElseThrow(() ->
                            new ResourceNotFoundException("Product not found: " + request.getProductId()));

            boolean productBelongsToOrder = order.getItems().stream()
                    .map(OrderItem::getProduct)
                    .anyMatch(itemProduct ->
                            itemProduct != null
                                    && itemProduct.getId().equals(product.getId()));

            if (!productBelongsToOrder) {
                throw new BadRequestException("The product does not belong to this order");
            }

            rating.setProduct(product);
        } else {
            rating.setProduct(null);
        }

        return ratingRepository.save(rating);
    }

    @Transactional
    public List<Rating> forProduct(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Product not found: " + productId));

        List<Rating> ratings = ratingRepository.findByProduct(product);
        for (Rating rating : ratings) {
            rating.getOrder().getItems().size();
        }
        return ratings;
    }
}
