package com.vmv.agromart.service;

import com.vmv.agromart.dto.OrderItemRequest;
import com.vmv.agromart.dto.OrderRequest;
import com.vmv.agromart.exception.BadRequestException;
import com.vmv.agromart.exception.ForbiddenException;
import com.vmv.agromart.exception.ResourceNotFoundException;
import com.vmv.agromart.model.*;
import com.vmv.agromart.repository.OrderRepository;
import com.vmv.agromart.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.SecureRandom;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    private static final BigDecimal DELIVERY_CHARGE = new BigDecimal("50.00");
    private static final BigDecimal TAX_PERCENT = new BigDecimal("0.05");
    private static final SecureRandom RANDOM = new SecureRandom();

    @Transactional
    public Order placeOrder(User customer, OrderRequest request) {
        if (request.getItems().isEmpty()) {
            throw new BadRequestException("Order must contain at least one item");
        }

        Order order = new Order();
        order.setOrderNumber(generateOrderNumber());
        order.setCustomer(customer);
        order.setDeliveryAddress(request.getDeliveryAddress());
        order.setDeliveryCity(request.getDeliveryCity());
        order.setDeliveryPincode(request.getDeliveryPincode());
        order.setDeliveryContactPhone(request.getDeliveryContactPhone());
        order.setPaymentMethod(request.getPaymentMethod());
        order.setSpecialInstructions(request.getSpecialInstructions());
        order.setRequestedDeliveryDate(request.getRequestedDeliveryDate());
        order.setRequestedDeliveryTimeSlot(request.getRequestedDeliveryTimeSlot());

        BigDecimal subtotal = BigDecimal.ZERO;

        for (OrderItemRequest itemRequest : request.getItems()) {
            Product product = productRepository.findById(itemRequest.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found: " + itemRequest.getProductId()));

            if (!Boolean.TRUE.equals(product.getIsAvailable())) {
                throw new BadRequestException("Product not available: " + product.getProductName());
            }
            if (product.getAvailableQuantity().compareTo(itemRequest.getQuantity()) < 0) {
                throw new BadRequestException("Insufficient stock for: " + product.getProductName());
            }

            BigDecimal lineTotal = product.getPricePerUnit().multiply(itemRequest.getQuantity())
                    .setScale(2, RoundingMode.HALF_UP);

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setQuantity(itemRequest.getQuantity());
            orderItem.setUnitPrice(product.getPricePerUnit());
            orderItem.setTotalPrice(lineTotal);
            order.getItems().add(orderItem);

            product.setAvailableQuantity(product.getAvailableQuantity().subtract(itemRequest.getQuantity()));
            productRepository.save(product);

            subtotal = subtotal.add(lineTotal);
        }

        BigDecimal tax = subtotal.multiply(TAX_PERCENT).setScale(2, RoundingMode.HALF_UP);
        BigDecimal total = subtotal.add(DELIVERY_CHARGE).add(tax);

        order.setSubtotal(subtotal);
        order.setDeliveryCharges(DELIVERY_CHARGE);
        order.setTaxAmount(tax);
        order.setTotalAmount(total);

        if (order.getPaymentMethod() == Order.PaymentMethod.COD) {
            order.setPaymentStatus(Order.PaymentStatus.PENDING);
        }

        return orderRepository.save(order);
    }

    @Transactional
    public List<Order> myOrders(User customer) {
        List<Order> orders = orderRepository.findByCustomerOrderByCreatedAtDesc(customer);
        orders.forEach(this::initItems);
        return orders;
    }

    @Transactional
    public List<Order> allOrders(User admin) {
        requireSuperAdmin(admin);
        List<Order> orders = orderRepository.findAllByOrderByCreatedAtDesc();
        orders.forEach(this::initItems);
        return orders;
    }

    @Transactional
    public List<Order> ordersForCity(User admin) {
        if (admin.getRole() != User.UserRole.STORAGE_ADMIN && admin.getRole() != User.UserRole.DELIVERY_PROVIDER) {
            throw new ForbiddenException("This action requires role STORAGE_ADMIN or DELIVERY_PROVIDER");
        }
        String city = admin.getCity();
        if (city == null || city.isBlank()) {
            throw new BadRequestException("Your account has no city assigned; contact super admin");
        }
        List<Order> orders = orderRepository.findByDeliveryCityIgnoreCaseOrderByCreatedAtDesc(city);
        orders.forEach(this::initItems);
        return orders;
    }

    @Transactional
    public Order get(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found: " + id));
        initItems(order);
        return order;
    }

    @Transactional
    public Order updateStatus(User admin, Long id, Order.OrderStatus status) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found: " + id));

        if (admin.getRole() == User.UserRole.SUPER_ADMIN) {
            // Super Admin can update any order's status without restriction.
        } else if (admin.getRole() == User.UserRole.STORAGE_ADMIN) {
            requireSameCity(admin, order);
        } else if (admin.getRole() == User.UserRole.DELIVERY_PROVIDER) {
            requireSameCity(admin, order);
            if (status != Order.OrderStatus.OUT_FOR_DELIVERY && status != Order.OrderStatus.DELIVERED) {
                throw new ForbiddenException("Delivery Provider can only set status to OUT_FOR_DELIVERY or DELIVERED");
            }
        } else {
            throw new ForbiddenException("This action requires role SUPER_ADMIN, STORAGE_ADMIN, or DELIVERY_PROVIDER");
        }

        order.setOrderStatus(status);
        order = orderRepository.save(order);
        initItems(order);
        return order;
    }

    private void requireSuperAdmin(User user) {
        if (user.getRole() != User.UserRole.SUPER_ADMIN) {
            throw new ForbiddenException("This action requires role SUPER_ADMIN");
        }
    }

    private void requireSameCity(User admin, Order order) {
        String city = admin.getCity();
        if (city == null || city.isBlank()) {
            throw new BadRequestException("Your account has no city assigned; contact super admin");
        }
        if (order.getDeliveryCity() == null || !order.getDeliveryCity().equalsIgnoreCase(city)) {
            throw new ForbiddenException("This order is outside your assigned city");
        }
    }

    // Force-loads the lazy items collection while the persistence context is still
    // open (open-in-view is disabled), so responses can be serialized safely after
    // the transactional method returns.
    private void initItems(Order order) {
        order.getItems().size();
    }

    @Transactional
    public Order cancel(User customer, Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found: " + id));
        if (!order.getCustomer().getId().equals(customer.getId())) {
            throw new ResourceNotFoundException("Order not found: " + id);
        }
        if (order.getOrderStatus() == Order.OrderStatus.DELIVERED) {
            throw new BadRequestException("Delivered orders cannot be cancelled");
        }
        order.setOrderStatus(Order.OrderStatus.CANCELLED);
        order = orderRepository.save(order);
        initItems(order);
        return order;
    }

    private String generateOrderNumber() {
        return "ORD" + System.currentTimeMillis() + RANDOM.nextInt(1000);
    }
}
