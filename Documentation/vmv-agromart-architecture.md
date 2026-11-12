# VMV AgroMart - System Architecture & Database Design

## Executive Summary

VMV AgroMart is a comprehensive agricultural marketplace platform connecting farmers directly to storage facilities, retailers, and end customers. The system facilitates the complete supply chain from farm to consumer with real-time tracking, feedback, and payment integration.

---

## 1. System Overview

### 1.1 Business Flow
```
Farmers → City Admin (Approval) → Storage Admin (Pune) → Distribution → End Users
                                                                        ├─ Retailers
                                                                        ├─ Flat Owners
                                                                        └─ Shopkeepers
```

### 1.2 User Roles (7 Types)

| Role | Location | Responsibilities |
|------|----------|------------------|
| **Farmer** | Multiple Cities | Sell products to AgroMart |
| **City Admin** | Ahmednagar, Satara, Latur | Approve/manage farmers in their city |
| **Storage Admin** | Pune (Central) | Manage inventory, incoming/outgoing products |
| **Super Admin** | Pune | System maintenance, user management |
| **Retailer** | Various | Bulk daily purchases (D-Mart, shop owners) |
| **Flat Owner** | Various | On-demand consumer purchases |
| **Provider/Delivery** | Various | Order fulfillment and delivery |

### 1.3 Technology Stack

**Backend:**
- Java (Spring Boot 3.x)
- Spring Security with JWT
- Spring Data JPA
- Hibernate ORM
- Maven/Gradle

**Database:**
- MySQL 8.0+
- Redis (for OTP caching and session management)

**Frontend:**
- AngularJS (Web Application)
- Angular Material for UI components
- Leaflet.js / Google Maps API for tracking

**Mobile:**
- Angular with Ionic Framework (iOS + Android)
- Capacitor for native features

**Integration Services:**
- Twilio/AWS SNS for OTP SMS
- Razorpay/Stripe for Payment Gateway
- Google Maps/Mapbox for geolocation
- Firebase Cloud Messaging for notifications

---

## 2. System Architecture

### 2.1 High-Level Architecture

```
┌─────────────────────────────────────────────────────────────┐
│                     PRESENTATION LAYER                       │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐     │
│  │ Web App      │  │ Mobile App   │  │ Admin Panel  │     │
│  │ (Angular)    │  │ (Ionic)      │  │ (Angular)    │     │
│  └──────────────┘  └──────────────┘  └──────────────┘     │
└─────────────────────────────────────────────────────────────┘
                            ▼
┌─────────────────────────────────────────────────────────────┐
│                      API GATEWAY LAYER                       │
│  ┌────────────────────────────────────────────────────────┐ │
│  │  Spring Boot REST API (JWT Authentication)             │ │
│  │  - Rate Limiting  - CORS  - Request Validation        │ │
│  └────────────────────────────────────────────────────────┘ │
└─────────────────────────────────────────────────────────────┘
                            ▼
┌─────────────────────────────────────────────────────────────┐
│                     BUSINESS LOGIC LAYER                     │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐     │
│  │ Auth Service │  │ Order Service│  │ User Service │     │
│  └──────────────┘  └──────────────┘  └──────────────┘     │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐     │
│  │Product Svc   │  │Payment Svc   │  │Tracking Svc  │     │
│  └──────────────┘  └──────────────┘  └──────────────┘     │
└─────────────────────────────────────────────────────────────┘
                            ▼
┌─────────────────────────────────────────────────────────────┐
│                      DATA ACCESS LAYER                       │
│  ┌────────────────────────────────────────────────────────┐ │
│  │  Spring Data JPA Repositories                          │ │
│  └────────────────────────────────────────────────────────┘ │
└─────────────────────────────────────────────────────────────┘
                            ▼
┌─────────────────────────────────────────────────────────────┐
│                       DATA LAYER                             │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐     │
│  │ MySQL        │  │ Redis Cache  │  │ File Storage │     │
│  │ (Primary DB) │  │ (OTP/Session)│  │ (S3/Local)   │     │
│  └──────────────┘  └──────────────┘  └──────────────┘     │
└─────────────────────────────────────────────────────────────┘
                            ▼
┌─────────────────────────────────────────────────────────────┐
│                    EXTERNAL SERVICES                         │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐     │
│  │ SMS Gateway  │  │ Payment Gate │  │ Maps API     │     │
│  │ (Twilio)     │  │ (Razorpay)   │  │ (Google)     │     │
│  └──────────────┘  └──────────────┘  └──────────────┘     │
└─────────────────────────────────────────────────────────────┘
```

### 2.2 Microservices Approach (Future Enhancement)

For scalability beyond 10 farmers, consider breaking into microservices:
- Authentication Service
- User Management Service
- Product/Inventory Service
- Order Management Service
- Payment Service
- Notification Service
- Tracking Service

---

## 3. Database Design (MySQL)

### 3.1 Complete Schema

#### **Table: users**
Primary table for all user types with role-based access.

```sql
CREATE TABLE users (
    user_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    phone_number VARCHAR(15) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE,
    full_name VARCHAR(100) NOT NULL,
    user_role ENUM('FARMER', 'CITY_ADMIN', 'STORAGE_ADMIN', 'SUPER_ADMIN',
                   'RETAILER', 'FLAT_OWNER', 'PROVIDER') NOT NULL,
    is_active BOOLEAN DEFAULT TRUE,
    is_verified BOOLEAN DEFAULT FALSE,
    profile_image_url VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    last_login TIMESTAMP NULL,

    INDEX idx_phone (phone_number),
    INDEX idx_role (user_role),
    INDEX idx_active (is_active)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
```

#### **Table: otp_verification**
Temporary OTP storage with expiration.

```sql
CREATE TABLE otp_verification (
    otp_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    phone_number VARCHAR(15) NOT NULL,
    otp_code VARCHAR(6) NOT NULL,
    otp_type ENUM('LOGIN', 'REGISTRATION', 'PASSWORD_RESET') NOT NULL,
    is_verified BOOLEAN DEFAULT FALSE,
    expires_at TIMESTAMP NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    verified_at TIMESTAMP NULL,
    attempt_count INT DEFAULT 0,

    INDEX idx_phone_otp (phone_number, otp_code),
    INDEX idx_expires (expires_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
```

#### **Table: user_sessions**
JWT token management and session tracking.

```sql
CREATE TABLE user_sessions (
    session_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    jwt_token TEXT NOT NULL,
    refresh_token VARCHAR(255),
    device_type ENUM('WEB', 'MOBILE_ANDROID', 'MOBILE_IOS') NOT NULL,
    device_id VARCHAR(255),
    ip_address VARCHAR(45),
    user_agent TEXT,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    expires_at TIMESTAMP NOT NULL,
    last_activity TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    INDEX idx_user (user_id),
    INDEX idx_token (jwt_token(255)),
    INDEX idx_active (is_active)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
```

#### **Table: cities**
City master data for City Admins.

```sql
CREATE TABLE cities (
    city_id INT PRIMARY KEY AUTO_INCREMENT,
    city_name VARCHAR(100) NOT NULL UNIQUE,
    state_name VARCHAR(100) NOT NULL,
    city_code VARCHAR(10) UNIQUE,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    INDEX idx_city_name (city_name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Pre-populate cities
INSERT INTO cities (city_name, state_name, city_code) VALUES
('Ahmednagar', 'Maharashtra', 'AHM'),
('Satara', 'Maharashtra', 'SAT'),
('Latur', 'Maharashtra', 'LAT'),
('Pune', 'Maharashtra', 'PUN');
```

#### **Table: user_profiles**
Extended profile information for each user type.

```sql
CREATE TABLE user_profiles (
    profile_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT UNIQUE NOT NULL,
    address_line1 VARCHAR(255),
    address_line2 VARCHAR(255),
    city_id INT,
    pincode VARCHAR(10),
    latitude DECIMAL(10, 8),
    longitude DECIMAL(11, 8),

    -- Farmer specific
    farm_name VARCHAR(100),
    farm_size_acres DECIMAL(10, 2),
    farming_type VARCHAR(50),

    -- Retailer specific
    business_name VARCHAR(100),
    business_type VARCHAR(50),
    gst_number VARCHAR(20),

    -- Storage Admin specific
    storage_capacity_kg DECIMAL(12, 2),

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (city_id) REFERENCES cities(city_id),
    INDEX idx_city (city_id),
    INDEX idx_location (latitude, longitude)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
```

#### **Table: farmer_city_mapping**
Maps farmers to their city admin for approval workflow.

```sql
CREATE TABLE farmer_city_mapping (
    mapping_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    farmer_id BIGINT NOT NULL,
    city_id INT NOT NULL,
    city_admin_id BIGINT,
    approval_status ENUM('PENDING', 'APPROVED', 'REJECTED') DEFAULT 'PENDING',
    approved_at TIMESTAMP NULL,
    rejected_reason TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (farmer_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (city_id) REFERENCES cities(city_id),
    FOREIGN KEY (city_admin_id) REFERENCES users(user_id),
    UNIQUE KEY unique_farmer_city (farmer_id, city_id),
    INDEX idx_status (approval_status),
    INDEX idx_city_admin (city_admin_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
```

#### **Table: product_categories**
Product classification.

```sql
CREATE TABLE product_categories (
    category_id INT PRIMARY KEY AUTO_INCREMENT,
    category_name VARCHAR(100) NOT NULL UNIQUE,
    category_image_url VARCHAR(255),
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    INDEX idx_active (is_active)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
```

#### **Table: products**
Products listed by farmers.

```sql
CREATE TABLE products (
    product_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    farmer_id BIGINT NOT NULL,
    category_id INT NOT NULL,
    product_name VARCHAR(100) NOT NULL,
    description TEXT,
    unit_of_measure ENUM('KG', 'GRAM', 'PIECE', 'DOZEN', 'LITER') NOT NULL,
    price_per_unit DECIMAL(10, 2) NOT NULL,
    available_quantity DECIMAL(10, 2) NOT NULL,
    product_image_url VARCHAR(255),
    is_organic BOOLEAN DEFAULT FALSE,
    is_available BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    FOREIGN KEY (farmer_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (category_id) REFERENCES product_categories(category_id),
    INDEX idx_farmer (farmer_id),
    INDEX idx_category (category_id),
    INDEX idx_available (is_available)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
```

#### **Table: storage_inventory**
Central storage (Pune) inventory tracking.

```sql
CREATE TABLE storage_inventory (
    inventory_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    product_id BIGINT NOT NULL,
    storage_admin_id BIGINT NOT NULL,
    quantity_received DECIMAL(10, 2) NOT NULL,
    quantity_available DECIMAL(10, 2) NOT NULL,
    quantity_reserved DECIMAL(10, 2) DEFAULT 0,
    received_date DATE NOT NULL,
    expiry_date DATE,
    batch_number VARCHAR(50),
    storage_location VARCHAR(50),
    quality_grade ENUM('A', 'B', 'C') DEFAULT 'A',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    FOREIGN KEY (product_id) REFERENCES products(product_id) ON DELETE CASCADE,
    FOREIGN KEY (storage_admin_id) REFERENCES users(user_id),
    INDEX idx_product (product_id),
    INDEX idx_available (quantity_available),
    INDEX idx_expiry (expiry_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
```

#### **Table: orders**
Main order table for all user types.

```sql
CREATE TABLE orders (
    order_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    order_number VARCHAR(50) UNIQUE NOT NULL,
    customer_id BIGINT NOT NULL,
    customer_type ENUM('RETAILER', 'FLAT_OWNER', 'SHOPKEEPER') NOT NULL,
    storage_admin_id BIGINT NOT NULL,
    provider_id BIGINT,

    order_status ENUM('PENDING', 'CONFIRMED', 'PROCESSING', 'OUT_FOR_DELIVERY',
                      'DELIVERED', 'CANCELLED', 'RETURNED') DEFAULT 'PENDING',

    delivery_type ENUM('IMMEDIATE', 'SCHEDULED') NOT NULL,
    required_date DATE NOT NULL,
    required_time_slot VARCHAR(20),

    delivery_address TEXT NOT NULL,
    delivery_city_id INT NOT NULL,
    delivery_pincode VARCHAR(10),
    delivery_latitude DECIMAL(10, 8),
    delivery_longitude DECIMAL(11, 8),

    subtotal DECIMAL(10, 2) NOT NULL,
    delivery_charges DECIMAL(10, 2) DEFAULT 0,
    tax_amount DECIMAL(10, 2) DEFAULT 0,
    discount_amount DECIMAL(10, 2) DEFAULT 0,
    total_amount DECIMAL(10, 2) NOT NULL,

    payment_status ENUM('PENDING', 'COMPLETED', 'FAILED', 'REFUNDED') DEFAULT 'PENDING',
    payment_method ENUM('COD', 'ONLINE', 'UPI', 'CARD') NOT NULL,

    special_instructions TEXT,

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    confirmed_at TIMESTAMP NULL,
    delivered_at TIMESTAMP NULL,
    cancelled_at TIMESTAMP NULL,

    FOREIGN KEY (customer_id) REFERENCES users(user_id),
    FOREIGN KEY (storage_admin_id) REFERENCES users(user_id),
    FOREIGN KEY (provider_id) REFERENCES users(user_id),
    FOREIGN KEY (delivery_city_id) REFERENCES cities(city_id),

    INDEX idx_customer (customer_id),
    INDEX idx_status (order_status),
    INDEX idx_provider (provider_id),
    INDEX idx_required_date (required_date),
    INDEX idx_order_number (order_number)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
```

#### **Table: order_items**
Individual items within an order.

```sql
CREATE TABLE order_items (
    order_item_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    order_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    inventory_id BIGINT,

    quantity DECIMAL(10, 2) NOT NULL,
    unit_price DECIMAL(10, 2) NOT NULL,
    total_price DECIMAL(10, 2) NOT NULL,

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (order_id) REFERENCES orders(order_id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES products(product_id),
    FOREIGN KEY (inventory_id) REFERENCES storage_inventory(inventory_id),

    INDEX idx_order (order_id),
    INDEX idx_product (product_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
```

#### **Table: order_tracking**
Real-time order status updates and GPS tracking.

```sql
CREATE TABLE order_tracking (
    tracking_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    order_id BIGINT NOT NULL,
    provider_id BIGINT,

    status ENUM('PENDING', 'CONFIRMED', 'PROCESSING', 'OUT_FOR_DELIVERY',
                'DELIVERED', 'CANCELLED', 'RETURNED') NOT NULL,
    status_message VARCHAR(255),

    current_latitude DECIMAL(10, 8),
    current_longitude DECIMAL(11, 8),

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (order_id) REFERENCES orders(order_id) ON DELETE CASCADE,
    FOREIGN KEY (provider_id) REFERENCES users(user_id),

    INDEX idx_order (order_id),
    INDEX idx_status (status),
    INDEX idx_created (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
```

#### **Table: payments**
Payment transaction records.

```sql
CREATE TABLE payments (
    payment_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    order_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,

    transaction_id VARCHAR(100) UNIQUE,
    payment_gateway VARCHAR(50),
    payment_method ENUM('COD', 'ONLINE', 'UPI', 'CARD', 'WALLET') NOT NULL,

    amount DECIMAL(10, 2) NOT NULL,
    currency VARCHAR(3) DEFAULT 'INR',

    payment_status ENUM('PENDING', 'SUCCESS', 'FAILED', 'REFUNDED') DEFAULT 'PENDING',

    gateway_response TEXT,
    failure_reason VARCHAR(255),

    paid_at TIMESTAMP NULL,
    refunded_at TIMESTAMP NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (order_id) REFERENCES orders(order_id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(user_id),

    INDEX idx_order (order_id),
    INDEX idx_user (user_id),
    INDEX idx_transaction (transaction_id),
    INDEX idx_status (payment_status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
```

#### **Table: ratings_feedback**
User feedback and ratings system.

```sql
CREATE TABLE ratings_feedback (
    feedback_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    order_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,

    rating_type ENUM('PRODUCT', 'DELIVERY', 'OVERALL') NOT NULL,
    rating_value INT CHECK (rating_value BETWEEN 1 AND 5),

    feedback_text TEXT,
    images_url TEXT,

    is_verified_purchase BOOLEAN DEFAULT TRUE,
    is_visible BOOLEAN DEFAULT TRUE,

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    FOREIGN KEY (order_id) REFERENCES orders(order_id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(user_id),

    INDEX idx_order (order_id),
    INDEX idx_user (user_id),
    INDEX idx_rating (rating_value),
    INDEX idx_visible (is_visible)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
```

#### **Table: notifications**
System notifications for all users.

```sql
CREATE TABLE notifications (
    notification_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,

    notification_type ENUM('ORDER_UPDATE', 'PAYMENT', 'DELIVERY', 'SYSTEM',
                          'PROMOTION', 'FEEDBACK_REQUEST') NOT NULL,

    title VARCHAR(255) NOT NULL,
    message TEXT NOT NULL,

    reference_id BIGINT,
    reference_type VARCHAR(50),

    is_read BOOLEAN DEFAULT FALSE,
    read_at TIMESTAMP NULL,

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,

    INDEX idx_user (user_id),
    INDEX idx_read (is_read),
    INDEX idx_type (notification_type)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
```

#### **Table: system_logs**
Audit trail and system activity logs.

```sql
CREATE TABLE system_logs (
    log_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT,

    action_type VARCHAR(50) NOT NULL,
    action_description TEXT,

    entity_type VARCHAR(50),
    entity_id BIGINT,

    ip_address VARCHAR(45),
    user_agent TEXT,

    request_data TEXT,
    response_data TEXT,

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE SET NULL,

    INDEX idx_user (user_id),
    INDEX idx_action (action_type),
    INDEX idx_entity (entity_type, entity_id),
    INDEX idx_created (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
```

---

## 4. User Authentication & Role Management (Priority Feature)

### 4.1 OTP-Based Authentication Flow

```
┌──────────────┐
│   User       │
│  (Mobile/Web)│
└──────┬───────┘
       │
       │ 1. Enter Phone Number
       ▼
┌──────────────────────────────────────┐
│  Frontend (Angular)                  │
│  POST /api/auth/send-otp             │
│  { phoneNumber: "+919876543210" }    │
└──────────────┬───────────────────────┘
               │
               │ 2. API Request
               ▼
┌──────────────────────────────────────┐
│  Backend (Spring Boot)               │
│  AuthController.sendOTP()            │
│  - Validate phone number             │
│  - Check rate limiting               │
│  - Generate 6-digit OTP              │
└──────────────┬───────────────────────┘
               │
               │ 3. Store OTP
               ▼
┌──────────────────────────────────────┐
│  Redis Cache (5 min expiry)          │
│  Key: otp:{phoneNumber}              │
│  Value: { otp: "123456",             │
│           attempts: 0,               │
│           expiresAt: timestamp }     │
└──────────────┬───────────────────────┘
               │
               │ 4. Also store in MySQL
               ▼
┌──────────────────────────────────────┐
│  MySQL: otp_verification             │
└──────────────┬───────────────────────┘
               │
               │ 5. Send SMS
               ▼
┌──────────────────────────────────────┐
│  SMS Gateway (Twilio/AWS SNS)        │
│  Send OTP to user's phone            │
└──────────────┬───────────────────────┘
               │
               │ 6. SMS Delivered
               ▼
┌──────────────────────────────────────┐
│  User receives OTP                   │
│  "Your OTP is 123456"                │
└──────────────┬───────────────────────┘
               │
               │ 7. Enter OTP
               ▼
┌──────────────────────────────────────┐
│  Frontend                            │
│  POST /api/auth/verify-otp           │
│  { phoneNumber: "+91...",            │
│    otp: "123456",                    │
│    deviceType: "MOBILE_ANDROID" }    │
└──────────────┬───────────────────────┘
               │
               │ 8. Verify OTP
               ▼
┌──────────────────────────────────────┐
│  Backend                             │
│  AuthController.verifyOTP()          │
│  - Check OTP validity                │
│  - Check expiration                  │
│  - Check attempts (max 3)            │
│  - Verify user exists                │
└──────────────┬───────────────────────┘
               │
               │ 9. Generate JWT Token
               ▼
┌──────────────────────────────────────┐
│  JWT Token Generation                │
│  - userId, role, permissions         │
│  - Access Token (30 min)             │
│  - Refresh Token (7 days)            │
└──────────────┬───────────────────────┘
               │
               │ 10. Store Session
               ▼
┌──────────────────────────────────────┐
│  MySQL: user_sessions                │
│  Redis: session:{userId}             │
└──────────────┬───────────────────────┘
               │
               │ 11. Return Tokens
               ▼
┌──────────────────────────────────────┐
│  Frontend                            │
│  Store tokens in:                    │
│  - LocalStorage (Web)                │
│  - SecureStorage (Mobile)            │
└──────────────┬───────────────────────┘
               │
               │ 12. Authenticated!
               ▼
┌──────────────────────────────────────┐
│  User Dashboard                      │
└──────────────────────────────────────┘
```

### 4.2 JWT Token Structure

```json
{
  "header": {
    "alg": "HS256",
    "typ": "JWT"
  },
  "payload": {
    "userId": 12345,
    "phoneNumber": "+919876543210",
    "role": "FARMER",
    "permissions": ["VIEW_PRODUCTS", "CREATE_PRODUCTS", "UPDATE_PRODUCTS"],
    "sessionId": 67890,
    "iat": 1234567890,
    "exp": 1234569690
  },
  "signature": "..."
}
```

### 4.3 Role-Based Access Control (RBAC) Matrix

| Feature | Farmer | City Admin | Storage Admin | Super Admin | Retailer | Flat Owner | Provider |
|---------|--------|------------|---------------|-------------|----------|------------|----------|
| **Authentication** |
| OTP Login | ✓ | ✓ | ✓ | ✓ | ✓ | ✓ | ✓ |
| **Product Management** |
| Add Products | ✓ | ✗ | ✗ | ✓ | ✗ | ✗ | ✗ |
| View Products | ✓ | ✓ | ✓ | ✓ | ✓ | ✓ | ✓ |
| Update Own Products | ✓ | ✗ | ✗ | ✓ | ✗ | ✗ | ✗ |
| **Farmer Management** |
| Approve Farmers | ✗ | ✓ | ✗ | ✓ | ✗ | ✗ | ✗ |
| View City Farmers | ✗ | ✓ | ✓ | ✓ | ✗ | ✗ | ✗ |
| **Inventory Management** |
| View Inventory | ✗ | ✗ | ✓ | ✓ | ✗ | ✗ | ✗ |
| Update Inventory | ✗ | ✗ | ✓ | ✓ | ✗ | ✗ | ✗ |
| Track Incoming | ✗ | ✗ | ✓ | ✓ | ✗ | ✗ | ✗ |
| Track Outgoing | ✗ | ✗ | ✓ | ✓ | ✗ | ✗ | ✗ |
| **Order Management** |
| Place Order | ✗ | ✗ | ✗ | ✗ | ✓ | ✓ | ✗ |
| View Own Orders | ✗ | ✗ | ✗ | ✗ | ✓ | ✓ | ✓ |
| Cancel Order | ✗ | ✗ | ✗ | ✗ | ✓ | ✓ | ✗ |
| Return Order | ✗ | ✗ | ✗ | ✗ | ✓ | ✓ | ✗ |
| View All Orders | ✗ | ✗ | ✓ | ✓ | ✗ | ✗ | ✗ |
| Assign Provider | ✗ | ✗ | ✓ | ✓ | ✗ | ✗ | ✗ |
| **Delivery** |
| Accept Order | ✗ | ✗ | ✗ | ✗ | ✗ | ✗ | ✓ |
| Update Location | ✗ | ✗ | ✗ | ✗ | ✗ | ✗ | ✓ |
| Mark Delivered | ✗ | ✗ | ✗ | ✗ | ✗ | ✗ | ✓ |
| **Payment** |
| Make Payment | ✗ | ✗ | ✗ | ✗ | ✓ | ✓ | ✗ |
| View Payment History | ✗ | ✗ | ✗ | ✗ | ✓ | ✓ | ✗ |
| **Feedback** |
| Give Feedback | ✗ | ✗ | ✗ | ✗ | ✓ | ✓ | ✗ |
| View Feedback | ✓ | ✗ | ✓ | ✓ | ✗ | ✗ | ✗ |
| **System Admin** |
| Manage All Users | ✗ | ✗ | ✗ | ✓ | ✗ | ✗ | ✗ |
| View All Data | ✗ | ✗ | ✗ | ✓ | ✗ | ✗ | ✗ |
| System Settings | ✗ | ✗ | ✗ | ✓ | ✗ | ✗ | ✗ |

### 4.4 Security Implementation Details

#### Password-less Security
- No passwords stored (OTP-only authentication)
- OTP valid for 5 minutes
- Maximum 3 OTP attempts per phone number per hour
- Rate limiting: 5 OTP requests per phone per day
- Redis-backed OTP cache with automatic expiration

#### JWT Security
- Access Token: 30 minutes expiry
- Refresh Token: 7 days expiry
- Token rotation on refresh
- Blacklist tokens on logout (Redis)
- Device binding (optional)

#### API Security
- All endpoints require JWT except `/auth/*`
- Role-based method security with `@PreAuthorize`
- Rate limiting per user/IP
- CORS configuration for web/mobile
- SQL injection prevention via JPA/Hibernate
- XSS prevention in Angular

---

## 5. API Endpoint Structure

### 5.1 Authentication Endpoints

```
POST   /api/auth/send-otp           - Send OTP to phone number
POST   /api/auth/verify-otp         - Verify OTP and login
POST   /api/auth/refresh-token      - Refresh access token
POST   /api/auth/logout             - Logout and invalidate session
GET    /api/auth/profile            - Get current user profile
PUT    /api/auth/profile            - Update user profile
```

### 5.2 User Management Endpoints

```
GET    /api/users                   - List users (Admin only)
GET    /api/users/{id}              - Get user details
POST   /api/users                   - Create new user (Admin only)
PUT    /api/users/{id}              - Update user
DELETE /api/users/{id}              - Deactivate user (Admin only)
PUT    /api/users/{id}/activate     - Activate user (Admin only)
```

### 5.3 Farmer Management Endpoints (City Admin)

```
GET    /api/farmers                 - List farmers in city
GET    /api/farmers/{id}            - Get farmer details
PUT    /api/farmers/{id}/approve    - Approve farmer
PUT    /api/farmers/{id}/reject     - Reject farmer
GET    /api/farmers/pending         - Get pending approvals
```

### 5.4 Product Endpoints

```
GET    /api/products                - List all available products
GET    /api/products/{id}           - Get product details
POST   /api/products                - Create product (Farmer only)
PUT    /api/products/{id}           - Update product (Farmer only)
DELETE /api/products/{id}           - Delete product (Farmer only)
GET    /api/products/category/{id}  - Get products by category
GET    /api/products/farmer/{id}    - Get farmer's products
```

### 5.5 Inventory Endpoints (Storage Admin)

```
GET    /api/inventory               - View storage inventory
POST   /api/inventory               - Add inventory entry
PUT    /api/inventory/{id}          - Update inventory
GET    /api/inventory/incoming      - Today's incoming products
GET    /api/inventory/outgoing      - Today's outgoing orders
GET    /api/inventory/expiring      - Products expiring soon
GET    /api/inventory/low-stock     - Low stock alerts
```

### 5.6 Order Endpoints

```
POST   /api/orders                  - Create new order
GET    /api/orders                  - Get user's orders
GET    /api/orders/{id}             - Get order details
PUT    /api/orders/{id}/cancel      - Cancel order
PUT    /api/orders/{id}/return      - Return order
GET    /api/orders/all              - Get all orders (Admin/Storage)
PUT    /api/orders/{id}/assign      - Assign provider (Storage Admin)
PUT    /api/orders/{id}/status      - Update order status
```

### 5.7 Tracking Endpoints

```
GET    /api/tracking/{orderId}      - Get order tracking info
POST   /api/tracking/{orderId}      - Add tracking update (Provider)
PUT    /api/tracking/{orderId}/location - Update GPS location (Provider)
GET    /api/tracking/{orderId}/history  - Get tracking history
```

### 5.8 Payment Endpoints

```
POST   /api/payments/initiate       - Initiate payment
POST   /api/payments/verify         - Verify payment
GET    /api/payments/{orderId}      - Get payment status
POST   /api/payments/webhook        - Payment gateway webhook
GET    /api/payments/history        - Get payment history
```

### 5.9 Feedback Endpoints

```
POST   /api/feedback                - Submit feedback
GET    /api/feedback/order/{id}     - Get order feedback
GET    /api/feedback/product/{id}   - Get product feedback
PUT    /api/feedback/{id}           - Update feedback
GET    /api/feedback/my             - Get my feedback
```

### 5.10 Notification Endpoints

```
GET    /api/notifications           - Get user notifications
PUT    /api/notifications/{id}/read - Mark as read
PUT    /api/notifications/read-all  - Mark all as read
DELETE /api/notifications/{id}      - Delete notification
```

---

## 6. Mobile & Web Platform Considerations

### 6.1 Responsive Design Requirements

**Mobile (Ionic/Angular)**
- Bottom navigation for main features
- Swipe gestures for order tracking
- Native camera for scanning QR codes
- Push notifications via FCM
- Offline mode support (service workers)
- Location services for delivery tracking

**Web (Angular)**
- Desktop-first responsive design
- Sidebar navigation
- Real-time updates via WebSockets
- Multi-tab support
- CSV/Excel export functionality
- Advanced filtering and search

### 6.2 Shared Components
- Authentication module
- Order management module
- Product catalog
- Payment integration
- Map integration
- Notification system

---

## 7. Key Features Implementation

### 7.1 Order Tracking & Map Integration

**Technology:** Google Maps JavaScript API / Mapbox

**Features:**
1. **Real-time Provider Location**
   - Provider app sends GPS coordinates every 30 seconds
   - WebSocket connection for live updates
   - Estimated arrival time calculation

2. **Route Visualization**
   - Draw route from storage to delivery location
   - Show current provider position on route
   - Traffic-aware ETA updates

3. **Status Updates**
   - Push notifications on status changes
   - SMS alerts for key milestones
   - In-app status badge

**Implementation:**
```typescript
// Provider location update (Mobile app)
setInterval(() => {
  navigator.geolocation.getCurrentPosition((position) => {
    socket.emit('location-update', {
      orderId: currentOrder.id,
      latitude: position.coords.latitude,
      longitude: position.coords.longitude,
      timestamp: new Date()
    });
  });
}, 30000); // Every 30 seconds

// Customer real-time tracking (Web/Mobile)
socket.on('location-update', (data) => {
  updateMarkerPosition(data.latitude, data.longitude);
  updateETA(data);
});
```

### 7.2 Payment Gateway Integration

**Recommended:** Razorpay (India-focused)

**Flow:**
1. Customer initiates checkout
2. Backend creates Razorpay order
3. Frontend shows payment modal
4. User completes payment
5. Razorpay webhook confirms payment
6. Backend updates order status
7. Confirmation notification sent

**Security:**
- Payment signature verification
- Webhook signature validation
- PCI DSS compliance via Razorpay
- No card details stored locally

### 7.3 Feedback & Ratings System

**Features:**
- 5-star rating system
- Product quality rating
- Delivery experience rating
- Photo upload with feedback
- Verified purchase badge
- Response from storage admin

**Display:**
- Average ratings on product cards
- Recent reviews section
- Filter by rating
- Helpful votes on reviews

---

## 8. Scalability & Performance

### 8.1 Database Optimization

**Indexing Strategy:**
- Composite indexes on frequently joined columns
- Full-text search on product names/descriptions
- Partitioning orders table by date (monthly)
- Archive old orders to separate table

**Query Optimization:**
- Use connection pooling (HikariCP)
- Implement pagination for large result sets
- Cache frequently accessed data (Redis)
- Use read replicas for reporting queries

### 8.2 Caching Strategy

**Redis Usage:**
- OTP storage (5 min TTL)
- User session data (30 min TTL)
- Product catalog cache (1 hour TTL)
- API response caching
- Rate limiting counters

### 8.3 Performance Targets

| Metric | Target | Notes |
|--------|--------|-------|
| API Response Time | < 200ms | 95th percentile |
| Page Load Time | < 2s | Initial load |
| Order Placement | < 3s | End-to-end |
| GPS Update Frequency | 30s | Provider location |
| Concurrent Users | 1000+ | Peak capacity |
| Database Queries | < 50ms | Average |

---

## 9. Deployment Architecture

### 9.1 Recommended Infrastructure

**Production Environment:**
```
┌─────────────────────────────────────────────────────────┐
│                    Load Balancer (Nginx)                │
│              SSL Termination / HTTPS                     │
└────────────────────┬────────────────────────────────────┘
                     │
        ┌────────────┴────────────┐
        │                         │
┌───────▼────────┐       ┌────────▼───────┐
│  App Server 1  │       │  App Server 2  │
│  (Spring Boot) │       │  (Spring Boot) │
└───────┬────────┘       └────────┬───────┘
        │                         │
        └────────────┬────────────┘
                     │
        ┌────────────┴────────────┐
        │                         │
┌───────▼────────┐       ┌────────▼───────┐
│  MySQL Master  │◄─────►│  MySQL Replica │
│  (Read/Write)  │       │  (Read Only)   │
└────────────────┘       └────────────────┘
        │
        │
┌───────▼────────┐
│  Redis Cache   │
│  (Session/OTP) │
└────────────────┘
```

### 9.2 Cloud Hosting Options

**Option 1: AWS**
- EC2 instances for application servers
- RDS MySQL for database
- ElastiCache Redis
- S3 for file storage
- CloudFront CDN
- Route 53 DNS

**Option 2: Azure**
- App Service for Spring Boot
- Azure Database for MySQL
- Azure Cache for Redis
- Blob Storage
- Azure CDN

**Option 3: Google Cloud Platform**
- Compute Engine / Cloud Run
- Cloud SQL for MySQL
- Memorystore for Redis
- Cloud Storage
- Cloud CDN

**Initial Setup (Small Scale - 10 Farmers):**
- 1x Application Server: 2 vCPU, 4GB RAM
- 1x MySQL Server: 2 vCPU, 8GB RAM, 100GB SSD
- 1x Redis Server: 1 vCPU, 1GB RAM
- Estimated Cost: $100-150/month

---

## 10. Security Checklist

- [ ] OTP rate limiting implemented
- [ ] JWT token expiration and rotation
- [ ] Session management with Redis
- [ ] SQL injection prevention (JPA/Hibernate)
- [ ] XSS prevention (Angular sanitization)
- [ ] CORS configuration
- [ ] API rate limiting per user/IP
- [ ] HTTPS/SSL certificates
- [ ] Payment gateway webhook signature verification
- [ ] Role-based access control on all endpoints
- [ ] Sensitive data encryption at rest
- [ ] Audit logging for critical actions
- [ ] Regular security updates
- [ ] Database backup strategy
- [ ] Disaster recovery plan

---

## 11. Development Roadmap

### Phase 1: Foundation (Weeks 1-2) ✓ PRIORITY
- [x] Database schema creation
- [x] Spring Boot project setup
- [x] Angular project setup
- [ ] User authentication (OTP login)
- [ ] Role management system
- [ ] Basic user profiles

### Phase 2: Core Features (Weeks 3-4)
- [ ] Product management (Farmer)
- [ ] City admin approval workflow
- [ ] Storage inventory system
- [ ] Basic order placement
- [ ] Order listing and details

### Phase 3: Advanced Features (Weeks 5-6)
- [ ] Payment gateway integration
- [ ] Order tracking system
- [ ] Map integration
- [ ] Provider assignment
- [ ] Real-time GPS updates

### Phase 4: Additional Features (Weeks 7-8)
- [ ] Feedback and ratings
- [ ] Notification system
- [ ] Order cancellation/return
- [ ] Analytics dashboard
- [ ] Report generation

### Phase 5: Mobile App (Weeks 9-10)
- [ ] Ionic framework setup
- [ ] Mobile UI components
- [ ] Native features (camera, GPS, push notifications)
- [ ] App testing on Android/iOS

### Phase 6: Testing & Deployment (Weeks 11-12)
- [ ] Unit testing (JUnit, Jasmine)
- [ ] Integration testing
- [ ] UAT with 10 farmers
- [ ] Performance testing
- [ ] Production deployment
- [ ] Training and documentation

---

## 12. Next Steps

### Immediate Actions:
1. **Create Database** - Execute SQL scripts to create all tables
2. **Setup Spring Boot Project** - Initialize with required dependencies
3. **Implement OTP Authentication** - Priority feature
4. **Setup Angular Project** - Create login and registration screens
5. **Configure Redis** - For OTP caching
6. **SMS Gateway Setup** - Twilio/AWS SNS integration

### Code Structure to Build:
```
backend/
├── src/main/java/com/vmvagromart/
│   ├── config/          - Security, DB, Redis config
│   ├── controller/      - REST endpoints
│   ├── service/         - Business logic
│   ├── repository/      - JPA repositories
│   ├── model/           - Entity classes
│   ├── dto/             - Data transfer objects
│   ├── security/        - JWT, OTP handling
│   └── util/            - Helper classes

frontend/
├── src/app/
│   ├── auth/            - Login, OTP components
│   ├── dashboard/       - Role-based dashboards
│   ├── products/        - Product management
│   ├── orders/          - Order management
│   ├── tracking/        - Map and tracking
│   ├── shared/          - Shared components
│   └── services/        - API services
```

---

## Appendix

### A. Sample OTP SMS Template
```
Your VMV AgroMart verification code is: {OTP_CODE}
Valid for 5 minutes. Do not share this code.
- VMV AgroMart
```

### B. Environment Variables Required
```
# Application
SPRING_PROFILES_ACTIVE=production
SERVER_PORT=8080

# Database
DB_HOST=localhost
DB_PORT=3306
DB_NAME=vmv_agromart
DB_USERNAME=agromart_user
DB_PASSWORD=secure_password

# Redis
REDIS_HOST=localhost
REDIS_PORT=6379
REDIS_PASSWORD=redis_password

# JWT
JWT_SECRET=your-256-bit-secret-key
JWT_EXPIRATION=1800000  # 30 minutes

# SMS Gateway
TWILIO_ACCOUNT_SID=your_account_sid
TWILIO_AUTH_TOKEN=your_auth_token
TWILIO_PHONE_NUMBER=+1234567890

# Payment Gateway
RAZORPAY_KEY_ID=your_key_id
RAZORPAY_KEY_SECRET=your_key_secret

# Maps API
GOOGLE_MAPS_API_KEY=your_maps_api_key
```

### C. Database Initialization Scripts
See separate SQL file: `vmv-agromart-schema.sql`

---

**Document Version:** 1.0
**Last Updated:** 2026-05-15
**Prepared for:** VMV AgroMart Development Team
