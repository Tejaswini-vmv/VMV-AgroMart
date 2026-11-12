# VMV AgroMart - Complete Source Code Structure

## 🎯 PROJECT OVERVIEW

This is the **complete source code** for VMV AgroMart - an agricultural marketplace platform.

**Stack:**
- **Backend:** Spring Boot 3.2 (Java 17)
- **Frontend:** Angular 16+
- **Mobile:** Ionic 7 + Angular
- **Database:** MySQL 8.0
- **Cache:** Redis 7.0

---

## 📂 COMPLETE PROJECT STRUCTURE

```
vmv-agromart/
│
├── backend/                          # Spring Boot Backend API
│   ├── src/main/
│   │   ├── java/com/vmv/agromart/
│   │   │   ├── model/               # JPA Entities
│   │   │   │   ├── User.java
│   │   │   │   ├── Product.java
│   │   │   │   ├── Order.java
│   │   │   │   ├── OrderItem.java
│   │   │   │   ├── Inventory.java
│   │   │   │   ├── Payment.java
│   │   │   │   ├── Tracking.java
│   │   │   │   ├── Rating.java
│   │   │   │   └── Notification.java
│   │   │   │
│   │   │   ├── repository/          # JPA Repositories
│   │   │   │   ├── UserRepository.java
│   │   │   │   ├── ProductRepository.java
│   │   │   │   ├── OrderRepository.java
│   │   │   │   ├── InventoryRepository.java
│   │   │   │   ├── PaymentRepository.java
│   │   │   │   ├── TrackingRepository.java
│   │   │   │   ├── RatingRepository.java
│   │   │   │   └── NotificationRepository.java
│   │   │   │
│   │   │   ├── service/             # Business Logic
│   │   │   │   ├── AuthService.java
│   │   │   │   ├── UserService.java
│   │   │   │   ├── ProductService.java
│   │   │   │   ├── OrderService.java
│   │   │   │   ├── PaymentService.java
│   │   │   │   ├── SmsService.java
│   │   │   │   ├── EmailService.java
│   │   │   │   ├── TrackingService.java
│   │   │   │   └── NotificationService.java
│   │   │   │
│   │   │   ├── controller/          # REST Controllers
│   │   │   │   ├── AuthController.java
│   │   │   │   ├── UserController.java
│   │   │   │   ├── ProductController.java
│   │   │   │   ├── OrderController.java
│   │   │   │   ├── PaymentController.java
│   │   │   │   ├── TrackingController.java
│   │   │   │   └── RatingController.java
│   │   │   │
│   │   │   ├── dto/                 # Data Transfer Objects
│   │   │   │   ├── LoginRequest.java
│   │   │   │   ├── OtpRequest.java
│   │   │   │   ├── ProductDTO.java
│   │   │   │   ├── OrderDTO.java
│   │   │   │   └── ...
│   │   │   │
│   │   │   ├── security/            # Security Configuration
│   │   │   │   ├── JwtUtil.java
│   │   │   │   ├── SecurityConfig.java
│   │   │   │   └── JwtAuthFilter.java
│   │   │   │
│   │   │   ├── config/              # Application Config
│   │   │   │   ├── RedisConfig.java
│   │   │   │   ├── TwilioConfig.java
│   │   │   │   ├── RazorpayConfig.java
│   │   │   │   └── SwaggerConfig.java
│   │   │   │
│   │   │   ├── exception/           # Exception Handling
│   │   │   │   ├── GlobalExceptionHandler.java
│   │   │   │   ├── ResourceNotFoundException.java
│   │   │   │   └── ...
│   │   │   │
│   │   │   ├── util/                # Utility Classes
│   │   │   │   ├── OtpGenerator.java
│   │   │   │   ├── DateUtil.java
│   │   │   │   └── ...
│   │   │   │
│   │   │   └── AgroMartApplication.java  # Main Class
│   │   │
│   │   └── resources/
│   │       ├── application.yml      # Main config
│   │       ├── application-dev.yml  # Dev config
│   │       ├── application-prod.yml # Prod config
│   │       └── data.sql            # Test data
│   │
│   ├── src/test/                    # Unit Tests
│   ├── pom.xml                      # Maven dependencies
│   ├── Dockerfile                   # Docker image
│   └── README.md
│
├── frontend/                        # Angular Web Application
│   ├── src/
│   │   ├── app/
│   │   │   ├── components/         # UI Components
│   │   │   │   ├── login/
│   │   │   │   ├── product-list/
│   │   │   │   ├── cart/
│   │   │   │   ├── checkout/
│   │   │   │   ├── orders/
│   │   │   │   └── ...
│   │   │   │
│   │   │   ├── services/           # API Services
│   │   │   │   ├── auth.service.ts
│   │   │   │   ├── product.service.ts
│   │   │   │   ├── order.service.ts
│   │   │   │   ├── payment.service.ts
│   │   │   │   └── ...
│   │   │   │
│   │   │   ├── models/             # TypeScript Models
│   │   │   │   ├── user.model.ts
│   │   │   │   ├── product.model.ts
│   │   │   │   ├── order.model.ts
│   │   │   │   └── ...
│   │   │   │
│   │   │   ├── guards/             # Route Guards
│   │   │   │   ├── auth.guard.ts
│   │   │   │   └── role.guard.ts
│   │   │   │
│   │   │   ├── interceptors/       # HTTP Interceptors
│   │   │   │   ├── auth.interceptor.ts
│   │   │   │   └── error.interceptor.ts
│   │   │   │
│   │   │   ├── app.component.ts    # Root component
│   │   │   ├── app.module.ts       # Main module
│   │   │   └── app-routing.module.ts # Routes
│   │   │
│   │   ├── assets/                 # Static assets
│   │   ├── environments/           # Environment configs
│   │   │   ├── environment.ts      # Dev
│   │   │   └── environment.prod.ts # Prod
│   │   │
│   │   └── index.html              # Entry HTML
│   │
│   ├── package.json                # NPM dependencies
│   ├── angular.json                # Angular config
│   ├── tsconfig.json               # TypeScript config
│   ├── Dockerfile                  # Docker image
│   └── README.md
│
└── mobile-app/                      # Ionic Mobile Application
    ├── src/
    │   ├── app/
    │   │   ├── pages/              # Mobile Pages
    │   │   │   ├── login/
    │   │   │   ├── home/
    │   │   │   ├── products/
    │   │   │   ├── cart/
    │   │   │   ├── orders/
    │   │   │   └── ...
    │   │   │
    │   │   ├── services/           # API Services
    │   │   ├── models/             # TypeScript Models
    │   │   ├── guards/             # Route Guards
    │   │   ├── app.component.ts
    │   │   ├── app.module.ts
    │   │   └── app-routing.module.ts
    │   │
    │   ├── assets/                 # Assets
    │   ├── environments/           # Environments
    │   └── index.html
    │
    ├── package.json
    ├── ionic.config.json
    ├── capacitor.config.ts
    └── README.md
```

---

## 🚀 QUICK START

### **Prerequisites:**
- Java 17+
- Node.js 16+
- MySQL 8.0
- Redis 7.0
- Maven 3.8+

### **Backend Setup:**

```bash
cd backend

# Build project
mvn clean install

# Run application
mvn spring-boot:run

# Or with profile
mvn spring-boot:run -Dspring-boot.run.profiles=dev

# API will be available at: http://localhost:8080
```

### **Frontend Setup:**

```bash
cd frontend

# Install dependencies
npm install

# Run development server
ng serve

# App will be available at: http://localhost:4200
```

### **Mobile App Setup:**

```bash
cd mobile-app

# Install dependencies
npm install

# Run in browser
ionic serve

# Build for Android
ionic capacitor build android

# Build for iOS
ionic capacitor build ios
```

---

## 📦 KEY DEPENDENCIES

### **Backend (pom.xml):**

```xml
<!-- Spring Boot Starter Parent -->
<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>3.2.0</version>
</parent>

<dependencies>
    <!-- Web -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>

    <!-- JPA/Hibernate -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-jpa</artifactId>
    </dependency>

    <!-- MySQL -->
    <dependency>
        <groupId>mysql</groupId>
        <artifactId>mysql-connector-java</artifactId>
    </dependency>

    <!-- Redis -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-redis</artifactId>
    </dependency>

    <!-- Security + JWT -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-security</artifactId>
    </dependency>
    <dependency>
        <groupId>io.jsonwebtoken</groupId>
        <artifactId>jjwt</artifactId>
        <version>0.9.1</version>
    </dependency>

    <!-- Twilio (SMS) -->
    <dependency>
        <groupId>com.twilio.sdk</groupId>
        <artifactId>twilio</artifactId>
        <version>9.2.0</version>
    </dependency>

    <!-- Razorpay (Payments) -->
    <dependency>
        <groupId>com.razorpay</groupId>
        <artifactId>razorpay-java</artifactId>
        <version>1.4.3</version>
    </dependency>

    <!-- Lombok -->
    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
    </dependency>

    <!-- Validation -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-validation</artifactId>
    </dependency>

    <!-- Swagger/OpenAPI -->
    <dependency>
        <groupId>org.springdoc</groupId>
        <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
        <version>2.2.0</version>
    </dependency>
</dependencies>
```

### **Frontend (package.json):**

```json
{
  "dependencies": {
    "@angular/animations": "^16.0.0",
    "@angular/common": "^16.0.0",
    "@angular/compiler": "^16.0.0",
    "@angular/core": "^16.0.0",
    "@angular/forms": "^16.0.0",
    "@angular/platform-browser": "^16.0.0",
    "@angular/router": "^16.0.0",
    "rxjs": "^7.8.0",
    "tslib": "^2.5.0",
    "zone.js": "^0.13.0"
  }
}
```

### **Mobile (package.json):**

```json
{
  "dependencies": {
    "@angular/core": "^16.0.0",
    "@capacitor/android": "^5.0.0",
    "@capacitor/core": "^5.0.0",
    "@capacitor/ios": "^5.0.0",
    "@ionic/angular": "^7.0.0",
    "ionic": "^7.0.0"
  }
}
```

---

## 🔧 CONFIGURATION FILES

### **application.yml (Backend):**

```yaml
spring:
  application:
    name: vmv-agromart

  datasource:
    url: jdbc:mysql://localhost:3306/vmv_agromart
    username: agromart_user
    password: ${DB_PASSWORD}
    driver-class-name: com.mysql.cj.jdbc.Driver

  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
    properties:
      hibernate:
        dialect: org.hibernate.dialect.MySQL8Dialect

  redis:
    host: localhost
    port: 6379
    password: ${REDIS_PASSWORD}

# JWT Configuration
jwt:
  secret: ${JWT_SECRET}
  expiration: 86400000  # 24 hours

# Twilio Configuration
twilio:
  account-sid: ${TWILIO_SID}
  auth-token: ${TWILIO_TOKEN}
  phone-number: ${TWILIO_PHONE}

# Razorpay Configuration
razorpay:
  key-id: ${RAZORPAY_KEY}
  key-secret: ${RAZORPAY_SECRET}

# Google Maps Configuration
google:
  maps:
    api-key: ${GOOGLE_MAPS_KEY}

server:
  port: 8080
```

### **environment.ts (Frontend):**

```typescript
export const environment = {
  production: false,
  apiUrl: 'http://localhost:8080/api',
  razorpayKey: 'YOUR_RAZORPAY_KEY',
  googleMapsKey: 'YOUR_GOOGLE_MAPS_KEY'
};
```

### **environment.prod.ts (Frontend):**

```typescript
export const environment = {
  production: true,
  apiUrl: 'https://api.agromart.com/api',
  razorpayKey: 'YOUR_RAZORPAY_KEY',
  googleMapsKey: 'YOUR_GOOGLE_MAPS_KEY'
};
```

---

## 🎯 API ENDPOINTS

### **Authentication:**
```
POST   /api/auth/send-otp        # Send OTP to phone
POST   /api/auth/verify-otp      # Verify OTP & login
POST   /api/auth/logout           # Logout user
GET    /api/auth/me              # Get current user
```

### **Products:**
```
GET    /api/products             # List all products
GET    /api/products/{id}        # Get product details
POST   /api/products             # Create product (Farmer)
PUT    /api/products/{id}        # Update product
DELETE /api/products/{id}        # Delete product
GET    /api/products/search      # Search products
```

### **Orders:**
```
GET    /api/orders               # List user orders
GET    /api/orders/{id}          # Get order details
POST   /api/orders               # Create order
PUT    /api/orders/{id}/status   # Update order status
DELETE /api/orders/{id}          # Cancel order
```

### **Payments:**
```
POST   /api/payments/create      # Create Razorpay order
POST   /api/payments/verify      # Verify payment
GET    /api/payments/{id}        # Get payment details
```

### **Tracking:**
```
GET    /api/tracking/{orderId}   # Get tracking info
POST   /api/tracking/update      # Update location
POST   /api/tracking/complete    # Complete delivery
```

---

## 📱 MOBILE APP FEATURES

1. **Authentication:**
   - OTP-based login
   - Phone number verification
   - Auto-login with saved token

2. **Product Browsing:**
   - Browse by category
   - Search products
   - Filter & sort
   - Product details with images

3. **Shopping Cart:**
   - Add/remove items
   - Update quantities
   - Save for later

4. **Checkout:**
   - Address selection
   - Payment integration
   - Order confirmation

5. **Order Tracking:**
   - Real-time GPS tracking
   - Status updates
   - Delivery notifications

6. **User Profile:**
   - Edit profile
   - View order history
   - Manage addresses
   - Settings

---

## 🧪 TESTING

### **Backend Tests:**

```bash
# Run all tests
mvn test

# Run specific test
mvn test -Dtest=UserServiceTest

# Generate coverage report
mvn jacoco:report
```

### **Frontend Tests:**

```bash
# Run unit tests
ng test

# Run e2e tests
ng e2e

# Generate coverage
ng test --code-coverage
```

---

## 🐳 DOCKER DEPLOYMENT

### **Build Images:**

```bash
# Backend
cd backend
docker build -t vmv-backend:latest .

# Frontend
cd frontend
docker build -t vmv-frontend:latest .
```

### **Run with Docker Compose:**

```bash
docker-compose up -d
```

---

## 📚 ADDITIONAL RESOURCES

- **API Documentation:** http://localhost:8080/swagger-ui.html
- **Health Check:** http://localhost:8080/actuator/health
- **Database:** See `database/vmv-agromart-schema.sql`
- **Test Users:** See `Documentation/TEST-CREDENTIALS.md`

---

## 🚀 DEPLOYMENT

See complete deployment guides in `Documentation/` folder:
- `STEP-BY-STEP-DEPLOYMENT-GUIDE.md` - Oracle Free Tier (DIY)
- `EKS-PRODUCTION-DEPLOYMENT.md` - AWS EKS (Production)
- `DOCKER-DEPLOYMENT-GUIDE.md` - Docker deployment

---

## 💡 DEVELOPMENT WORKFLOW

1. **Create Feature Branch:**
   ```bash
   git checkout -b feature/new-feature
   ```

2. **Make Changes & Test:**
   ```bash
   # Backend
   mvn test

   # Frontend
   ng test
   ```

3. **Commit & Push:**
   ```bash
   git add .
   git commit -m "Add new feature"
   git push origin feature/new-feature
   ```

4. **Create Pull Request**

5. **Deploy:**
   ```bash
   # Build new images
   ./build-and-push.sh

   # Deploy to server
   ssh server "cd /opt/vmv-agromart && docker-compose pull && docker-compose up -d"
   ```

---

## 📧 SUPPORT

For questions or issues:
- Check documentation in `Documentation/` folder
- Review test credentials in `TEST-CREDENTIALS.md`
- Consult deployment guides

---

## ✅ CHECKLIST

### **Before Development:**
- [ ] Java 17+ installed
- [ ] Node.js 16+ installed
- [ ] MySQL 8.0 running
- [ ] Redis 7.0 running
- [ ] Environment variables configured

### **Before Deployment:**
- [ ] All tests passing
- [ ] Database migrated
- [ ] Environment configs updated
- [ ] Docker images built
- [ ] SSL certificates configured

---

**🎉 You're all set! Start building amazing features!**

© 2026 VMV AgroMart
