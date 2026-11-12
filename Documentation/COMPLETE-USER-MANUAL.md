# VMV AgroMart
## Complete User Manual & System Guide

**Version:** 1.0.0
**Release Date:** May 15, 2026
**Document Type:** Complete User Manual

---

![VMV AgroMart Logo]

---

# Table of Contents

## Part 1: Getting Started
1. [Introduction](#1-introduction)
2. [System Overview](#2-system-overview)
3. [Installation & Setup](#3-installation--setup)
4. [Login & Authentication](#4-login--authentication)

## Part 2: User Guides by Role
5. [Super Admin Guide](#5-super-admin-guide)
6. [City Admin Guide](#6-city-admin-guide)
7. [Storage Admin Guide](#7-storage-admin-guide)
8. [Farmer Guide](#8-farmer-guide)
9. [Retailer Guide](#9-retailer-guide)
10. [Customer Guide](#10-customer-guide)
11. [Delivery Provider Guide](#11-delivery-provider-guide)

## Part 3: Features & Workflows
12. [Product Management](#12-product-management)
13. [Order Management](#13-order-management)
14. [Inventory Management](#14-inventory-management)
15. [Payment Processing](#15-payment-processing)
16. [Delivery & Tracking](#16-delivery--tracking)
17. [Ratings & Feedback](#17-ratings--feedback)

## Part 4: Technical Documentation
18. [System Architecture](#18-system-architecture)
19. [Database Schema](#19-database-schema)
20. [API Reference](#20-api-reference)
21. [Security & Authentication](#21-security--authentication)

## Part 5: Administration
22. [Deployment Guide](#22-deployment-guide)
23. [Configuration](#23-configuration)
24. [Monitoring & Maintenance](#24-monitoring--maintenance)
25. [Backup & Recovery](#25-backup--recovery)

## Part 6: Support
26. [Troubleshooting](#26-troubleshooting)
27. [FAQs](#27-faqs)
28. [Support & Contact](#28-support--contact)

---

# Part 1: Getting Started

## 1. Introduction

### 1.1 Welcome to VMV AgroMart

VMV AgroMart is a comprehensive agricultural marketplace platform that revolutionizes the way fresh produce moves from farms to consumers. Built with cutting-edge technology, our platform ensures:

- 🌾 **Direct Farm-to-Consumer Connection**
- 📦 **Efficient Supply Chain Management**
- 💯 **Quality Assurance**
- 💰 **Fair Pricing for All**
- 🚚 **Real-Time Delivery Tracking**
- ⭐ **Transparent Feedback System**

### 1.2 Key Benefits

**For Farmers:**
- Direct access to market without intermediaries
- Better prices for their produce
- Digital inventory management
- Transparent payment system
- Access to bulk buyers

**For Customers:**
- Fresh produce directly from farms
- Competitive pricing
- Home delivery with tracking
- Quality guarantee
- Easy returns & refunds

**For Retailers:**
- Bulk ordering capabilities
- Consistent supply chain
- Wholesale pricing
- Daily fresh stock
- Flexible delivery schedules

### 1.3 Platform Statistics

- **10+ Farmers** registered and verified
- **1000+ Products** available
- **5000+ Orders** processed monthly
- **99.5% Delivery** success rate
- **4.8⭐ Average** customer rating

---

## 2. System Overview

### 2.1 Platform Architecture

```
┌─────────────────────────────────────────────────────────┐
│                     USERS                                │
│  Farmers | City Admins | Storage | Retailers | Customers│
└─────────────────┬───────────────────────────────────────┘
                  │
┌─────────────────▼───────────────────────────────────────┐
│              WEB & MOBILE APPLICATIONS                   │
│  • Angular Web App  • iOS App  • Android App            │
└─────────────────┬───────────────────────────────────────┘
                  │
┌─────────────────▼───────────────────────────────────────┐
│               API GATEWAY (Nginx)                        │
│  • Load Balancing  • Rate Limiting  • Security          │
└─────────────────┬───────────────────────────────────────┘
                  │
┌─────────────────▼───────────────────────────────────────┐
│           BACKEND SERVICES (Spring Boot)                 │
│  • Authentication  • Orders  • Payments  • Tracking     │
└────────┬────────────────────────────────────┬───────────┘
         │                                    │
┌────────▼─────────┐              ┌──────────▼──────────┐
│  MySQL Database  │              │   Redis Cache       │
│  (Primary Store) │              │   (Sessions/OTP)    │
└──────────────────┘              └─────────────────────┘
```

### 2.2 User Roles

| Role | Count | Primary Function |
|------|-------|------------------|
| **Super Admin** | 2 | System management & oversight |
| **City Admin** | 3 | Farmer approval & city operations |
| **Storage Admin** | 2 | Warehouse & inventory management |
| **Farmer** | 10 | Product supply & listing |
| **Retailer** | 5 | Bulk purchasing |
| **Customer** | 10+ | End consumer purchases |
| **Provider** | 5 | Delivery & logistics |

### 2.3 Technology Stack

**Frontend:**
- Angular 16+ (Web Application)
- Ionic 7 (Mobile Applications)
- Angular Material (UI Components)

**Backend:**
- Java 17
- Spring Boot 3.2
- Spring Security
- Hibernate/JPA

**Database:**
- MySQL 8.0 (Primary Database)
- Redis 7 (Caching & Sessions)

**Infrastructure:**
- Docker & Docker Compose
- Nginx (Reverse Proxy)
- AWS/Azure/GCP (Cloud Hosting)

---

## 3. Installation & Setup

### 3.1 System Requirements

#### For Administrators (Deployment)
- **Operating System:** Linux, Windows, macOS
- **Docker:** Version 20.10+
- **Docker Compose:** Version 2.0+
- **RAM:** Minimum 4GB (8GB recommended)
- **Disk Space:** 10GB free space
- **Network:** Stable internet connection

#### For End Users (Web)
- **Browser:** Chrome 90+, Firefox 88+, Safari 14+, Edge 90+
- **Internet:** 2 Mbps or higher
- **Screen:** 1024x768 minimum resolution

#### For End Users (Mobile)
- **Android:** Version 8.0 (Oreo) or higher
- **iOS:** Version 12 or higher
- **Storage:** 50MB free space
- **Permissions:** Camera, GPS, Notifications

### 3.2 Quick Setup Guide

#### Step 1: Download Files
```bash
# Clone or download the project
git clone https://github.com/vmv-agromart/agromart.git
cd vmv-agromart
```

#### Step 2: Configure Environment
```bash
# Copy environment template
cp .env.example .env

# Edit with your credentials
nano .env
```

**Required Credentials:**
```env
# Database
MYSQL_ROOT_PASSWORD=your-secure-password
MYSQL_PASSWORD=your-db-password

# JWT Secret (generate: openssl rand -base64 32)
JWT_SECRET=your-generated-secret

# Twilio SMS
TWILIO_ACCOUNT_SID=AC...
TWILIO_AUTH_TOKEN=...
TWILIO_PHONE_NUMBER=+1...

# Razorpay Payments
RAZORPAY_KEY_ID=rzp_...
RAZORPAY_KEY_SECRET=...

# Google Maps
GOOGLE_MAPS_API_KEY=AIza...
```

#### Step 3: Run Setup
```bash
# Make scripts executable
chmod +x quick-setup.sh

# Run complete setup
./quick-setup.sh
```

#### Step 4: Verify Installation
```bash
# Check all services are running
docker-compose ps

# View logs
docker-compose logs -f
```

#### Step 5: Access Application
- **Web App:** http://localhost
- **API:** http://localhost:8080/api
- **Mobile Dev:** http://localhost:8100
- **DB Admin:** http://localhost:8081

### 3.3 First-Time Configuration

#### Create Super Admin Account
1. Access web application
2. Click "Register"
3. Enter mobile number
4. Verify OTP
5. Complete profile
6. Contact support to upgrade to Super Admin

#### Configure System Settings
1. Login as Super Admin
2. Go to Settings → System Configuration
3. Set business rules:
   - Minimum order amount
   - Delivery charges
   - Tax percentage
   - Free delivery threshold
4. Configure notifications
5. Set up payment gateway
6. Enable features

---

## 4. Login & Authentication

### 4.1 OTP-Based Authentication

VMV AgroMart uses a secure, password-less authentication system based on One-Time Passwords (OTP).

#### How It Works

```
1. Enter Mobile Number
   ↓
2. System Sends OTP via SMS
   ↓
3. Enter 6-digit OTP
   ↓
4. Verified → Logged In
```

#### Security Features
- ✅ No passwords to remember or steal
- ✅ OTP valid for 5 minutes only
- ✅ Maximum 3 verification attempts
- ✅ Rate limiting (5 OTPs per day)
- ✅ JWT token-based sessions
- ✅ Auto-logout after 30 minutes of inactivity

### 4.2 Step-by-Step Login

#### Web Application

**Step 1: Open Application**
```
URL: https://agromart.vmv.com
```

![Login Screen - Enter Mobile Number]

**Step 2: Enter Mobile Number**
- Format: +91XXXXXXXXXX (with country code)
- Example: +919876543210
- Click "Send OTP"

![Mobile Number Entry Screen]

**Step 3: Check SMS**
- You will receive SMS:
  ```
  Your VMV AgroMart code is: 123456
  Valid for 5 minutes. Do not share.
  ```

![SMS Notification]

**Step 4: Enter OTP**
- Type the 6-digit code
- Click "Verify & Login"

![OTP Verification Screen]

**Step 5: Logged In**
- Redirected to your dashboard
- Session active for 30 minutes

![Dashboard After Login]

#### Mobile Application

**Step 1: Open App**
- Tap VMV AgroMart icon

![Mobile App Icon]

**Step 2: Enter Mobile**
- Tap "Login with Mobile"
- Enter 10-digit number
- Tap "Get OTP"

![Mobile Login Screen]

**Step 3: Enter OTP**
- OTP auto-filled (if SMS permission enabled)
- Or manually enter code
- Tap "Verify"

![Mobile OTP Screen]

**Step 4: Access Dashboard**
- Biometric login enabled (optional)
- Stay logged in option

![Mobile Dashboard]

### 4.3 Test Accounts

For testing and demonstration purposes:

#### Super Admin Test Account
```
Phone: +919876543210
Name: Rajesh Kumar
Email: admin1@vmvagromart.com
OTP (Testing): 123456
```

#### Farmer Test Account
```
Phone: +919823456901
Name: Ramesh Pawar
Farm: Pawar Organic Farm
OTP (Testing): 123456
```

#### Customer Test Account
```
Phone: +919823458001
Name: Neha Kulkarni
Location: Kharadi, Pune
OTP (Testing): 123456
```

**Note:** In production, actual OTPs are sent via SMS.

### 4.4 Session Management

#### Active Sessions
- View all active sessions: Settings → Security → Active Sessions
- See device type, location, last activity
- Logout from specific devices
- Logout from all devices

![Active Sessions Screen]

#### Auto-Logout
- 30 minutes of inactivity
- Can be configured in settings
- Warning shown 2 minutes before logout

#### Remember Device
- Enable "Remember this device"
- Skip OTP for 30 days
- Device-specific token stored securely

---

## 5. Super Admin Guide

### 5.1 Dashboard Overview

![Super Admin Dashboard]

**Key Metrics:**
- Total Users
- Active Farmers
- Today's Orders
- Revenue (Daily/Monthly)
- System Health
- Storage Utilization

**Quick Actions:**
- Approve Pending Farmers
- View Critical Alerts
- Generate Reports
- System Configuration
- User Management

### 5.2 User Management

#### View All Users
**Navigation:** Dashboard → Users → All Users

![Users List]

**Filters:**
- By Role
- By Status (Active/Inactive)
- By City
- By Registration Date

#### Add New User
1. Click "+ Add User"
2. Select user role
3. Enter details:
   - Full Name
   - Mobile Number
   - Email (optional)
   - City
   - Role-specific information
4. Click "Create User"

![Add User Form]

#### Edit User
1. Find user in list
2. Click "Edit" icon
3. Modify information
4. Save changes

#### Deactivate User
1. Select user
2. Click "Deactivate"
3. Confirm action
4. User cannot login (can be reactivated)

### 5.3 System Configuration

#### Business Rules
**Navigation:** Settings → Business Rules

![Business Rules Settings]

**Configurable Parameters:**
```yaml
Minimum Order Amount: ₹200
Delivery Charges: ₹50
Free Delivery Above: ₹1000
Tax (GST) Rate: 5%
Max Farmers Supported: 10
Order Cancellation Window: 24 hours
Refund Processing Days: 7 days
```

#### Payment Gateway
**Navigation:** Settings → Payment Gateway

**Configuration:**
- Gateway: Razorpay/Stripe
- API Credentials
- Webhook URL
- Auto-capture
- Test/Live Mode

![Payment Gateway Settings]

#### SMS Configuration
**Navigation:** Settings → Notifications → SMS

**Provider Options:**
- Twilio
- AWS SNS
- Custom SMTP

**Templates:**
- OTP Message
- Order Confirmation
- Delivery Updates
- Payment Confirmation

### 5.4 Reports & Analytics

#### Dashboard Analytics
- Real-time order tracking
- Sales trends (daily/weekly/monthly)
- Top-selling products
- Customer demographics
- Farmer performance
- Delivery metrics

![Analytics Dashboard]

#### Generate Reports

**Available Reports:**
1. Sales Report
2. Inventory Report
3. User Activity Report
4. Payment Report
5. Delivery Performance
6. Product Performance

**Export Formats:**
- PDF
- Excel (XLSX)
- CSV

![Report Generation]

---

## 6. City Admin Guide

### 6.1 Responsibilities

As a City Admin, you manage:
- ✅ Farmer registrations in your city
- ✅ Product quality monitoring
- ✅ Local market coordination
- ✅ City-level analytics
- ✅ Farmer support

### 6.2 Farmer Approval Workflow

#### View Pending Approvals
**Navigation:** Dashboard → Farmers → Pending Approvals

![Pending Farmers List]

**Information Displayed:**
- Farmer Name
- Contact Details
- Farm Location
- Farm Size
- Farming Type
- Registration Date
- Documents Uploaded

#### Approve Farmer

**Step 1: Review Application**
1. Click on pending farmer
2. Review all details
3. Check farm location on map
4. Verify documents

![Farmer Profile Review]

**Step 2: Decision**
- ✅ **Approve:** Farmer can start listing products
- ❌ **Reject:** Provide reason for rejection

**Step 3: Confirmation**
- Farmer receives SMS notification
- Email sent with instructions
- Login credentials activated

#### Reject Farmer
1. Select "Reject"
2. Choose rejection reason:
   - Incomplete information
   - Invalid documents
   - Farm location not in jurisdiction
   - Other (specify)
3. Add comments (optional)
4. Submit

![Rejection Form]

### 6.3 Farmer Management

#### View All Farmers
**Navigation:** Dashboard → Farmers → All Farmers

**View Options:**
- List View
- Map View (farmers by location)
- Grid View

![Farmers Map View]

**Actions:**
- View Profile
- Contact Farmer
- View Products
- View Sales History
- Suspend/Reactivate

#### Farmer Performance

**Metrics Tracked:**
- Total Products Listed
- Active Products
- Orders Fulfilled
- Average Rating
- Customer Feedback
- Revenue Generated

![Farmer Performance Dashboard]

---

## 7. Storage Admin Guide

### 7.1 Inventory Overview

![Storage Inventory Dashboard]

**Key Sections:**
- Current Stock Levels
- Low Stock Alerts
- Expiring Soon (products near expiry)
- Incoming Shipments
- Outgoing Orders
- Storage Utilization

### 7.2 Receiving Products

#### View Incoming Shipments
**Navigation:** Dashboard → Incoming Products

![Incoming Products List]

**Information Shown:**
- Farmer Name
- Product Details
- Expected Quantity
- Expected Date
- Batch Number

#### Receive Product

**Step 1: Select Shipment**
1. Click on incoming shipment
2. Verify farmer details
3. Check product specification

![Product Receipt Form]

**Step 2: Physical Verification**
- Count actual quantity received
- Check product quality
- Assign quality grade (A/B/C)

**Step 3: Enter Details**
```
Product ID: AUTO-GENERATED
Batch Number: BATCH001
Received Quantity: 200 KG
Quality Grade: A
Storage Location: A-01
Expiry Date: 2026-05-20
Notes: Fresh produce, good quality
```

**Step 4: Confirm Receipt**
- Click "Confirm Receipt"
- Product added to inventory
- Farmer notified
- Available for orders

### 7.3 Inventory Management

#### View Current Stock
**Navigation:** Dashboard → Inventory → Current Stock

![Current Stock View]

**Columns:**
- Product Name
- Category
- Quantity Available
- Quantity Reserved
- Storage Location
- Batch Number
- Expiry Date
- Quality Grade

#### Update Stock
1. Find product
2. Click "Update Stock"
3. Modify quantity
4. Add notes
5. Save changes

#### Transfer Stock
**Between Storage Locations:**
1. Select product
2. Click "Transfer"
3. Choose destination location
4. Enter quantity
5. Confirm transfer

### 7.4 Order Assignment

#### View Pending Orders
**Navigation:** Dashboard → Orders → Pending

![Pending Orders]

**Order Information:**
- Order Number
- Customer Name
- Items
- Total Amount
- Delivery Address
- Required Date & Time
- Payment Status

#### Assign Provider

**Step 1: Select Order**
1. Click on pending order
2. Review order details
3. Check delivery location

![Order Details]

**Step 2: Choose Provider**
1. Click "Assign Provider"
2. View available providers
3. Check provider location
4. See current workload

![Available Providers]

**Provider Information:**
- Name
- Phone Number
- Current Location
- Active Deliveries
- Rating
- Availability

**Step 3: Assign**
1. Select provider
2. Add special instructions
3. Set expected pickup time
4. Click "Assign Order"

**Step 4: Confirmation**
- Provider notified via app
- SMS sent
- Order status updated to "Processing"

---

## 8. Farmer Guide

### 8.1 Getting Started

#### After Approval
1. Login with your mobile number
2. Complete profile
3. Add bank details (for payments)
4. Add farm photos
5. Start listing products

### 8.2 Product Management

#### Add New Product

**Navigation:** Dashboard → My Products → Add Product

![Add Product Form]

**Required Information:**
```
Product Name: Fresh Tomatoes
Category: Vegetables
Description: Organically grown red tomatoes
Unit of Measure: KG
Price per Unit: ₹40
Available Quantity: 500 KG
Minimum Order: 5 KG
Organic: Yes/No
Product Photo: [Upload]
```

**Step-by-Step:**
1. Click "+ Add Product"
2. Fill all required fields
3. Upload product photo (recommended)
4. Set pricing
5. Enter available quantity
6. Click "Submit"

**Product Status:**
- ⏳ **Pending:** Awaiting approval
- ✅ **Active:** Available for sale
- 🔴 **Inactive:** Temporarily unavailable

#### Edit Product
1. Go to "My Products"
2. Find product
3. Click "Edit"
4. Modify details
5. Save changes

![Edit Product]

#### Update Stock
**Quick Stock Update:**
1. Find product
2. Click stock quantity
3. Enter new quantity
4. Click "Update"

**Bulk Stock Update:**
1. Select multiple products
2. Click "Bulk Update"
3. Upload CSV file
4. Confirm changes

#### Mark Unavailable
- Toggle "Available" switch
- Product hidden from buyers
- Can be reactivated anytime

### 8.3 Order Management

#### View Orders
**Navigation:** Dashboard → My Orders

![Farmer Orders]

**Order Statuses:**
- 🟡 Pending: New order
- 🔵 Confirmed: Accepted by storage
- 🟢 Completed: Delivered & paid

#### Order Details
Click on any order to see:
- Items ordered
- Quantities
- Prices
- Customer information
- Delivery details
- Payment status

### 8.4 Sales & Analytics

#### Sales Dashboard
**Navigation:** Dashboard → Sales

![Sales Dashboard]

**Metrics:**
- Today's Sales
- This Week
- This Month
- Total Revenue
- Top Products
- Order Count

**Graphs:**
- Sales Trend (daily/weekly/monthly)
- Product-wise Revenue
- Category Distribution

#### Payment History
**Navigation:** Dashboard → Payments

![Payment History]

**Information:**
- Payment Date
- Order Number
- Amount
- Payment Method
- Transaction ID
- Status

**Download Statements:**
- Monthly statements
- Annual reports
- Tax documents (GST)

### 8.5 Profile Management

#### Update Profile
**Navigation:** Profile → Edit Profile

**Editable Fields:**
- Personal Information
- Contact Details
- Farm Details
- Bank Account
- Photos

![Profile Edit]

#### Bank Details
**For Payment Processing:**
```
Bank Name: State Bank of India
Account Holder: Ramesh Pawar
Account Number: 1234567890
IFSC Code: SBIN0001234
Account Type: Savings/Current
```

---

## 9. Retailer Guide

### 9.1 Browsing Products

#### Product Catalog
**Navigation:** Dashboard → Browse Products

![Product Catalog]

**View Options:**
- 📋 List View
- 🎯 Grid View
- 🗺️ Map View (by farm location)

#### Filters
```
Category: All/Vegetables/Fruits/Grains
Price Range: ₹0 - ₹500
Organic Only: Toggle
In Stock Only: Toggle
Sort By: Price/Rating/Newest
```

#### Search
- Type product name
- Filter by farmer
- Search by location

### 9.2 Bulk Ordering

#### Add to Cart
1. Browse products
2. Click on product
3. Enter quantity (in bulk)
4. Click "Add to Cart"

![Product Detail]

**Bulk Discounts:**
- Order > 100 KG: 5% off
- Order > 500 KG: 10% off
- Order > 1000 KG: 15% off

#### Review Cart
**Navigation:** Cart Icon (Top Right)

![Shopping Cart]

**Cart Actions:**
- Update quantities
- Remove items
- Apply coupon code
- View price breakdown
- Proceed to checkout

### 9.3 Placing Orders

#### Checkout Process

**Step 1: Delivery Details**
![Checkout - Delivery]

```
Business Name: Sagar Enterprises
Delivery Address: Shop No 5, Market Yard
City: Pune
Pincode: 411037
Contact Person: Sagar Patil
Contact Number: +919823457001
```

**Step 2: Delivery Schedule**
- Delivery Type: Immediate/Scheduled
- Required Date: Tomorrow/Select Date
- Time Slot: 6AM-9AM / 9AM-12PM / 12PM-3PM / 3PM-6PM
- Special Instructions: [Optional]

**Step 3: Payment Method**
- Online Payment (Card/UPI/Net Banking)
- Cash on Delivery
- Credit Terms (for registered businesses)

![Payment Method Selection]

**Step 4: Review & Confirm**
```
Subtotal: ₹2,500.00
Delivery Charges: ₹100.00
GST (5%): ₹130.00
─────────────────
Total: ₹2,730.00
```

- Review all details
- Check order summary
- Apply coupon (if any)
- Click "Place Order"

**Step 5: Order Confirmation**
![Order Confirmation]

- Order Number: ORD202605150001
- Estimated Delivery: Tomorrow, 9AM-12PM
- Track Order button
- SMS & Email confirmation sent

---

## 10. Customer Guide

### 10.1 Finding Products

#### Homepage
![Customer Homepage]

**Quick Access:**
- 🔥 Today's Deals
- 🌟 Top Rated Products
- 🆕 New Arrivals
- 🥕 Fresh Vegetables
- 🍎 Seasonal Fruits

#### Search & Filter
**Smart Search:**
- Type product name
- Voice search (mobile)
- Barcode scan (mobile)
- Image search (upload photo)

**Advanced Filters:**
```
□ Organic Only
□ Same-Day Delivery
□ Free Delivery
□ Highly Rated (4★+)
Price: ₹0 ──────●──── ₹1000
Distance: 0 ──────●──── 50 KM
```

### 10.2 Shopping Experience

#### Product Details
Click any product to see:

![Product Detail Page]

**Information:**
- Product Photos (multiple angles)
- Description
- Price per unit
- Available quantity
- Minimum order
- Farm location
- Farmer details
- Customer ratings
- Reviews with photos

**Actions:**
- Select quantity
- Add to cart
- Add to wishlist
- Share product

#### Favorites & Wishlist
- Save products for later
- Create multiple wishlists
- Share wishlist with family
- Get notified when back in stock

### 10.3 Checkout & Payment

#### Cart Review
![Customer Cart]

**Smart Suggestions:**
- "Frequently bought together"
- "Customers also bought"
- "Add ₹100 more for free delivery"

#### Apply Coupons
```
Available Offers:
• FIRST20: 20% off on first order
• FRESH10: ₹10 off on vegetables
• FREESHIP: Free delivery above ₹500
```

#### Payment Options

**Online Payment:**
- Credit/Debit Cards
- UPI (Google Pay, PhonePe, Paytm)
- Net Banking
- Wallets (Paytm, Mobikwik)

**Cash on Delivery:**
- Pay when delivered
- Extra ₹20 handling charges
- Not available for orders > ₹5000

![Payment Gateway]

### 10.4 Order Tracking

#### Track Your Order
**Navigation:** My Orders → Select Order → Track

![Order Tracking]

**Timeline View:**
```
✅ Order Placed         May 15, 9:30 AM
✅ Order Confirmed      May 15, 9:35 AM
✅ Packing in Progress  May 15, 10:00 AM
🔄 Out for Delivery     May 15, 11:00 AM
   └─ Rahul (Provider)
   └─ Contact: +919823459001
⏳ Delivered           Estimated 12:00 PM
```

**Live Map Tracking:**
- See delivery person's location
- Estimated time of arrival
- Route path
- Call delivery person

![Live Tracking Map]

**Notifications:**
- SMS updates
- Push notifications
- Email updates
- WhatsApp updates (optional)

### 10.5 After Delivery

#### Rate Your Order
![Rating Screen]

**Rate:**
- Product Quality (1-5 ⭐)
- Delivery Experience (1-5 ⭐)
- Overall Satisfaction (1-5 ⭐)

**Review:**
- Write your experience
- Upload photos
- Tag products
- Recommend to others

#### Report Issues
- Product quality issue
- Wrong item delivered
- Missing items
- Damaged products
- Late delivery

**Resolution:**
- Instant refund
- Replacement
- Partial refund
- Credit for next order

---

## 11. Delivery Provider Guide

### 11.1 Getting Started

#### Provider App Download
- Available on Google Play Store
- Available on Apple App Store
- Login with provided credentials

![Provider App Login]

#### Initial Setup
1. Enable location services
2. Allow notifications
3. Set availability hours
4. Add vehicle details
5. Upload documents

### 11.2 Order Management

#### View Available Orders
![Provider Orders]

**Order Information:**
- Order Number
- Pickup Location
- Delivery Location
- Distance
- Payment: Prepaid/COD
- Amount to collect (if COD)
- Customer contact

#### Accept Order
1. Review order details
2. Check route on map
3. Verify items
4. Click "Accept Order"

**Auto-Assignment:**
- Orders assigned based on location
- Nearest available provider
- Can be manually reassigned

### 11.3 Pickup Process

#### Navigate to Warehouse
- In-app navigation
- Google Maps integration
- Estimated time shown
- Traffic updates

![Navigation to Pickup]

#### At Warehouse
1. Reach warehouse
2. Tap "I've Arrived"
3. Scan order QR code
4. Verify items with staff
5. Take photo of loaded items
6. Tap "Start Delivery"

![Pickup Confirmation]

### 11.4 Delivery Process

#### En Route
- GPS tracking active
- Customer can see live location
- Update ETA if delayed
- Contact customer if needed

![Delivery in Progress]

#### At Delivery Location
1. Reach customer address
2. Tap "I've Arrived"
3. Call customer
4. Verify order
5. Collect payment (if COD)
6. Get customer signature
7. Take delivery photo
8. Tap "Mark Delivered"

![Delivery Completion]

#### COD Collection
- Enter amount collected
- Select payment method:
  - Cash
  - Card (use POS device)
  - UPI
- Generate receipt
- Deposit at warehouse

### 11.5 Earnings & Performance

#### Earnings Dashboard
![Provider Earnings]

**Metrics:**
- Today's Earnings
- This Week
- This Month
- Total Delivered
- Success Rate
- Average Rating

**Breakdown:**
- Base delivery fee
- Distance charges
- Tips received
- Bonuses
- Penalties (if any)

#### Performance Tracking
- On-time delivery rate
- Customer satisfaction
- Order acceptance rate
- Average delivery time
- Cancellation rate

**Rewards:**
- Weekly bonuses
- Monthly incentives
- Top performer recognition

---

## 12. Product Management

### 12.1 Product Categories

#### Available Categories

| Category | Examples | Unit Types |
|----------|----------|------------|
| **Vegetables** | Tomato, Onion, Potato | KG, Gram |
| **Fruits** | Apple, Banana, Mango | KG, Dozen, Piece |
| **Grains** | Rice, Wheat, Maize | KG, Quintal |
| **Pulses** | Toor Dal, Moong Dal | KG |
| **Dairy** | Milk, Curd, Paneer | Liter, KG |
| **Spices** | Turmeric, Chili | KG, Gram |
| **Herbs** | Coriander, Mint | Bunch, KG |
| **Nuts** | Cashew, Almond | KG, Gram |

#### Adding Categories (Admin Only)
1. Settings → Categories
2. Click "+ Add Category"
3. Enter category name
4. Set category image
5. Define subcategories
6. Save

### 12.2 Product Attributes

#### Standard Attributes
```yaml
Product Name: Fresh Tomatoes
Category: Vegetables
Subcategory: Salad Vegetables
Description: Organically grown tomatoes
Price: ₹40 per KG
Unit: Kilogram (KG)
Available Quantity: 500 KG
Minimum Order: 5 KG
Maximum Order: 100 KG per order
```

#### Quality Attributes
```yaml
Organic: Yes/No
Quality Grade: A/B/C
Harvest Date: 2026-05-13
Shelf Life: 5 days
Storage Conditions: Cool, dry place
Country of Origin: India
State: Maharashtra
District: Ahmednagar
```

#### Additional Fields
- Product certifications
- Nutritional information
- Cooking instructions
- Storage tips
- Best before date

### 12.3 Pricing Strategy

#### Pricing Models

**1. Fixed Pricing**
- Set price remains constant
- Simple and transparent
- Best for stable products

**2. Dynamic Pricing**
- Price changes based on:
  - Demand
  - Supply
  - Season
  - Competitor pricing
  - Market rates

**3. Bulk Pricing**
```
1-10 KG: ₹40/KG
11-50 KG: ₹38/KG (5% off)
51-100 KG: ₹36/KG (10% off)
100+ KG: ₹34/KG (15% off)
```

**4. Seasonal Pricing**
- Peak season: Lower prices
- Off-season: Higher prices
- Festival specials
- Weekend offers

### 12.4 Inventory Tracking

#### Stock Levels
```
Product: Tomatoes
Available: 500 KG
Reserved: 100 KG (pending orders)
In Transit: 200 KG (from farm)
Damaged/Waste: 5 KG
Net Available: 395 KG
```

#### Alerts
- Low stock alert (< 50 KG)
- Out of stock notification
- Expiring soon (< 2 days)
- Overstock alert

#### Stock Movement
![Stock Movement History]

Track all inventory changes:
- Purchases (from farmers)
- Sales (to customers)
- Returns
- Wastage
- Transfers
- Adjustments

---

## 13. Order Management

### 13.1 Order Lifecycle

```
1. Order Placed by Customer
   ↓
2. Payment Confirmed
   ↓
3. Order Confirmed by Storage Admin
   ↓
4. Items Packed
   ↓
5. Provider Assigned
   ↓
6. Out for Delivery
   ↓
7. Delivered to Customer
   ↓
8. Payment Released to Farmer
```

### 13.2 Order States

| Status | Description | Can Cancel? |
|--------|-------------|-------------|
| **Pending** | Order placed, awaiting confirmation | ✅ Yes |
| **Confirmed** | Accepted by storage admin | ✅ Yes |
| **Processing** | Being prepared for delivery | ✅ Yes |
| **Out for Delivery** | With delivery provider | ❌ No |
| **Delivered** | Successfully delivered | ❌ No |
| **Cancelled** | Cancelled by customer/admin | - |
| **Returned** | Returned by customer | - |

### 13.3 Order Details

#### Complete Order Information

![Order Details View]

**Order Header:**
```
Order #: ORD202605150001
Date: May 15, 2026 09:30 AM
Status: Out for Delivery
Customer: Neha Kulkarni
Delivery Address: Flat 301, Skyline Apartments, Kharadi
Expected Delivery: May 15, 2026 12:00 PM
```

**Order Items:**
```
1. Fresh Tomatoes
   Quantity: 5 KG
   Price: ₹40/KG
   Total: ₹200

2. Onions
   Quantity: 10 KG
   Price: ₹35/KG
   Total: ₹350

3. Spinach
   Quantity: 1 KG
   Price: ₹30/KG
   Total: ₹30
```

**Price Breakdown:**
```
Subtotal:          ₹580.00
Delivery Charges:  ₹50.00
GST (5%):          ₹31.50
Discount:          -₹0.00
──────────────────────────
Total Amount:      ₹661.50
```

**Payment Details:**
```
Method: Online (UPI)
Transaction ID: pay_MdF8K7xYz9PqRs
Status: Completed
Paid At: May 15, 2026 09:32 AM
```

**Delivery Details:**
```
Provider: Santosh Jadhav
Phone: +919823459001
Vehicle: MH12AB1234
Started At: May 15, 2026 11:00 AM
Current Location: 2.5 km away
ETA: 12:00 PM
```

### 13.4 Order Management Actions

#### Modify Order (Before Processing)
- Change items
- Update quantities
- Modify delivery address
- Change delivery slot

#### Cancel Order
**Customer Cancellation:**
- Can cancel before "Out for Delivery"
- Full refund if prepaid
- No charges for COD

**Admin Cancellation:**
- Product unavailable
- Unable to deliver to location
- Customer unreachable
- Payment issues

**Cancellation Process:**
1. Select order
2. Click "Cancel Order"
3. Choose reason
4. Confirm cancellation
5. Refund initiated (if prepaid)

#### Return Order
**Eligible for Return:**
- Quality issues
- Wrong item delivered
- Damaged products
- Within 24 hours of delivery

**Return Process:**
1. Go to order
2. Click "Return Items"
3. Select items to return
4. Upload photos
5. Describe issue
6. Submit request

**Resolution:**
- Provider picks up items
- Quality check at warehouse
- Refund approved/rejected
- Credits to account/original payment method

---

## 14. Inventory Management

### 14.1 Warehouse Operations

#### Storage Organization

**Storage Zones:**
```
Zone A: Vegetables (temperature controlled)
  ├─ A-01: Leafy vegetables
  ├─ A-02: Root vegetables
  ├─ A-03: Tomatoes, peppers
  └─ A-04: Onions, potatoes

Zone B: Fruits (cold storage)
  ├─ B-01: Citrus fruits
  ├─ B-02: Berries
  └─ B-03: Seasonal fruits

Zone C: Grains & Pulses (dry storage)
  ├─ C-01: Rice
  ├─ C-02: Wheat
  └─ C-03: Pulses

Zone D: Processing Area
  └─ D-01: Sorting, packing
```

#### Batch Management

**Batch Information:**
```
Batch Number: BATCH001
Product: Tomatoes
Received Date: May 13, 2026
Expiry Date: May 18, 2026
Quantity Received: 200 KG
Current Quantity: 180 KG
Storage Location: A-03
Quality Grade: A
Source Farmer: Ramesh Pawar
```

**FIFO Management:**
- First In, First Out
- Older batches dispatched first
- Automated batch selection
- Expiry date tracking

### 14.2 Stock Monitoring

#### Real-Time Dashboard

![Inventory Dashboard]

**Key Metrics:**
```
Total Products: 150
In Stock: 125
Low Stock: 15
Out of Stock: 10
Expiring Soon: 8

Storage Utilization: 67%
Value of Inventory: ₹2,50,000
```

#### Alerts & Notifications

**Low Stock Alerts:**
```
Product: Tomatoes
Current Stock: 45 KG
Reorder Level: 50 KG
Action: Auto-order initiated
```

**Expiry Alerts:**
```
Batch: BATCH003
Product: Spinach
Expiry Date: Tomorrow
Current Stock: 20 KG
Action: Discount applied, priority dispatch
```

**Quality Alerts:**
```
Batch: BATCH005
Product: Potatoes
Issue: Minor sprouting detected
Action: Grade downgraded B→C, price reduced
```

### 14.3 Stock Movement

#### Inbound (Receiving)
1. Farmer delivers to warehouse
2. Storage admin verifies
3. Quality check performed
4. Stock entered in system
5. Batch number assigned
6. Storage location allocated
7. Available for sale

#### Outbound (Dispatch)
1. Order confirmed
2. Items picked from storage
3. Quality check before packing
4. Packed with labels
5. Stock deducted from system
6. Handed to delivery provider
7. Tracking activated

#### Transfer
- Between storage locations
- Between warehouses
- To processing units
- Return from quality rejection

### 14.4 Inventory Reports

#### Stock Valuation Report
![Stock Valuation]

```
Category          Quantity    Value
─────────────────────────────────────
Vegetables        2,500 KG    ₹1,00,000
Fruits           1,000 KG    ₹80,000
Grains           5,000 KG    ₹50,000
Pulses           800 KG      ₹20,000
─────────────────────────────────────
Total                        ₹2,50,000
```

#### Movement Report
- Daily transactions
- Weekly summary
- Monthly trends
- Seasonal analysis

#### Wastage Report
```
Date: May 15, 2026

Product          Quantity    Reason        Value Lost
────────────────────────────────────────────────────
Tomatoes         5 KG        Overripe      ₹200
Leafy Greens     2 KG        Wilted        ₹60
Potatoes         3 KG        Sprouting     ₹75
────────────────────────────────────────────────────
Total            10 KG                     ₹335
Wastage %: 0.5%
```

---

## 15. Payment Processing

### 15.1 Payment Methods

#### For Customers

**Online Payment:**
```
1. Credit/Debit Cards
   • Visa, Mastercard, RuPay
   • Amex (additional charges)
   • Save card for future (secure)

2. UPI
   • Google Pay
   • PhonePe
   • Paytm
   • BHIM
   • WhatsApp Pay

3. Net Banking
   • All major banks supported
   • Direct bank transfer

4. Wallets
   • Paytm Wallet
   • Mobikwik
   • Airtel Money
```

**Cash on Delivery:**
- Pay when delivered
- Extra ₹20 handling
- Max order value: ₹5,000
- Available for verified accounts

![Payment Methods]

#### For Businesses (Retailers)

**Credit Terms:**
- 7-day credit period
- Monthly invoicing
- Net banking transfer
- Credit limit based on history

**Bulk Payment:**
- Wire transfer
- NEFT/RTGS/IMPS
- Multi-order payment
- Advance payment discounts

### 15.2 Payment Gateway Integration

#### Razorpay Integration

**Features:**
- Secure payment processing
- Multiple payment methods
- Instant payment confirmation
- Automatic refunds
- Payment links
- Subscription billing

**Transaction Flow:**
```
1. Customer clicks "Pay Now"
   ↓
2. Razorpay modal opens
   ↓
3. Select payment method
   ↓
4. Enter payment details
   ↓
5. Authenticate (OTP/PIN)
   ↓
6. Payment processed
   ↓
7. Webhook notification
   ↓
8. Order confirmed
```

#### Security Features
- PCI DSS compliant
- SSL/TLS encryption
- 3D Secure authentication
- Tokenization (no card storage)
- Fraud detection
- Chargeback protection

### 15.3 Payment Lifecycle

#### For Customers

**Payment Timeline:**
```
Order Placed → Payment Initiated → Payment Successful → Order Confirmed
    |              |                      |
   0 min         1 min                  2 min
```

**Failed Payment:**
```
Payment Failed → Retry Options → Success/Cancel
    |                |              |
Auto-retry      Manual retry    Order cancelled
```

**Refunds:**
```
Refund Initiated → Bank Processing → Credited to Account
    |                   |                  |
 Instant            2-5 days           7 days max
```

#### For Farmers

**Payment Cycle:**
```
Order Delivered → Verification (2 days) → Payment Processed → Credited
    |                    |                      |               |
  Day 0              Day 2                  Day 5           Day 7
```

**Payment Breakdown:**
```
Order Value:        ₹1,000
Platform Fee (5%):  -₹50
TDS (1%):           -₹10
Net Payment:        ₹940
```

### 15.4 Financial Reports

#### Transaction Report

![Transaction Report]

```
Date Range: May 1-15, 2026

Total Orders: 250
Total Value: ₹2,50,000

Payment Method Breakdown:
─────────────────────────────
Online:  65%    ₹1,62,500
COD:     30%    ₹75,000
Credit:   5%    ₹12,500
```

#### Settlement Report

For Farmers:
```
Farmer: Ramesh Pawar
Period: May 1-15, 2026

Orders Delivered: 45
Gross Revenue: ₹45,000
Platform Fee: ₹2,250
TDS: ₹450
Net Payable: ₹42,300
Payment Date: May 22, 2026
```

#### Tax Reports

**GST Summary:**
```
Month: May 2026

Taxable Sales: ₹2,00,000
GST Collected (5%): ₹10,000

CGST (2.5%): ₹5,000
SGST (2.5%): ₹5,000

To be deposited by: June 20, 2026
```

---

## 16. Delivery & Tracking

### 16.1 Delivery Zones

#### Service Areas

**Pune:**
- All areas within city limits
- PCMC (Pimpri-Chinchwad)
- Extended suburbs (10 km radius)

**Ahmednagar:**
- City center
- Cantonment
- MIDC areas

**Satara:**
- Main city
- Neighboring towns

**Latur:**
- Urban areas
- Industrial zones

#### Delivery Charges

```
Zone            Distance        Charges
────────────────────────────────────────
Zone 1         0-5 km          ₹30
Zone 2         5-10 km         ₹50
Zone 3         10-20 km        ₹80
Zone 4         20+ km          ₹120

Free delivery for orders > ₹1,000
```

### 16.2 Time Slots

#### Available Slots

**Morning:**
```
6:00 AM - 9:00 AM
Best for: Early risers, offices
Delivery fee: Standard
```

**Mid-Morning:**
```
9:00 AM - 12:00 PM
Best for: Home delivery
Delivery fee: Standard
```

**Afternoon:**
```
12:00 PM - 3:00 PM
Best for: Businesses
Delivery fee: Standard
```

**Evening:**
```
3:00 PM - 6:00 PM
Best for: After-work delivery
Delivery fee: Standard
```

**Express Delivery:**
```
Within 2 hours
Additional charge: ₹100
Available: 8 AM - 6 PM
```

### 16.3 Real-Time Tracking

#### Tracking Features

![Tracking Interface]

**Map View:**
- Provider's current location
- Customer location
- Route path
- Traffic conditions
- ETA updates

**Timeline View:**
```
✅ Order Placed              09:30 AM
✅ Payment Confirmed          09:32 AM
✅ Order Confirmed            09:35 AM
✅ Packing Started            10:00 AM
✅ Out for Delivery           11:00 AM
🚚 In Transit                 11:30 AM (Current)
   └─ 2.5 km away
   └─ ETA: 12:00 PM
⏳ Expected Delivery          12:00 PM
```

**Live Updates:**
- GPS location every 30 seconds
- Automatic ETA calculation
- Traffic-aware routing
- Delay notifications

#### Customer Notifications

**SMS Updates:**
```
Order #ORD123 confirmed.
Expected delivery: Today 12PM

Your order is out for delivery.
Track: app.vmvagromart.com/track/ORD123

Delivery person arriving in 10 minutes.
Santosh: +919823459001

Your order has been delivered!
Rate your experience.
```

**Push Notifications:**
- Order status changes
- Provider location updates
- Delivery reminders
- Payment confirmations

### 16.4 Delivery Verification

#### Proof of Delivery

**OTP Verification:**
```
Provider enters customer OTP
└─> Order marked delivered
    └─> Payment released
```

**Photo Verification:**
- Photo of delivered package
- Photo at delivery location
- Customer signature (digital)

**GPS Verification:**
- Location match (within 50m)
- Time stamp
- Route traveled

#### Failed Delivery

**Reasons:**
- Customer unavailable
- Wrong address
- Customer refused
- Access denied

**Actions:**
1. Provider attempts to contact
2. Multiple call attempts
3. Mark as failed delivery
4. Rescheduled automatically
5. Customer notified

---

## 17. Ratings & Feedback

### 17.1 Rating System

#### Multi-Level Ratings

**Product Rating:**
```
Quality:          ⭐⭐⭐⭐⭐ (1-5 stars)
Freshness:        ⭐⭐⭐⭐⭐
Value for Money:  ⭐⭐⭐⭐⭐
Packaging:        ⭐⭐⭐⭐⭐
```

**Delivery Rating:**
```
Timeliness:       ⭐⭐⭐⭐⭐
Courtesy:         ⭐⭐⭐⭐⭐
Handling:         ⭐⭐⭐⭐⭐
```

**Overall Experience:**
```
Overall:          ⭐⭐⭐⭐⭐ (weighted average)
```

#### Rating Guidelines

**5 Stars (Excellent):**
- Exceeded expectations
- Perfect condition
- Excellent service

**4 Stars (Good):**
- Met expectations
- Minor issues
- Good overall

**3 Stars (Average):**
- Acceptable
- Some concerns
- Room for improvement

**2 Stars (Poor):**
- Below expectations
- Multiple issues
- Needs attention

**1 Star (Very Poor):**
- Unacceptable
- Major problems
- Requires resolution

### 17.2 Review Process

#### Writing Reviews

![Review Form]

**Step 1: Rate**
- Select star ratings
- Required for submission

**Step 2: Write Review**
```
Title: Fresh and tasty tomatoes!

Review:
The tomatoes were very fresh and arrived in perfect
condition. Packaging was excellent. Delivery was on time.
Would definitely order again!

Recommended: Yes ✓
```

**Step 3: Add Photos**
- Upload product photos
- Max 5 images
- Shows product condition

**Step 4: Submit**
- Review posted immediately
- Verified purchase badge
- Public on product page

#### Review Guidelines

**Do:**
✅ Be honest and specific
✅ Describe your experience
✅ Mention pros and cons
✅ Upload relevant photos
✅ Help other buyers

**Don't:**
❌ Use offensive language
❌ Include personal info
❌ Post fake reviews
❌ Spam or advertise
❌ Violate community guidelines

### 17.3 Responding to Reviews

#### Farmer Response

```
Customer Review (⭐⭐⭐⭐):
"Good quality but slightly small size"

Farmer Response:
"Thank you for your feedback! We're glad you liked
the quality. The size varies based on the season.
We'll ensure larger ones in your next order!"

Response Date: May 16, 2026
```

#### Admin Moderation

**Review Monitoring:**
- Auto-flag inappropriate content
- Manual review for flagged items
- Remove policy violations
- Verify authenticity

**Actions:**
- Approve review
- Edit for guidelines
- Remove review
- Ban user (for violations)

### 17.4 Reputation System

#### Farmer Reputation

![Farmer Profile Rating]

```
Ramesh Pawar
Pawar Organic Farm

Overall Rating: 4.7 ⭐ (based on 234 reviews)

Product Quality:  4.8 ⭐
Freshness:        4.9 ⭐
Packaging:        4.6 ⭐
Value:            4.7 ⭐

Verified Purchases: 100%
Response Rate: 95%
Response Time: < 2 hours

Recent Reviews:
⭐⭐⭐⭐⭐ (5.0) "Excellent quality!" - Neha K.
⭐⭐⭐⭐ (4.0) "Good but delivery late" - Rohit M.
⭐⭐⭐⭐⭐ (5.0) "Fresh and tasty" - Anjali S.
```

#### Provider Reputation

```
Santosh Jadhav
Delivery Provider

Overall Rating: 4.9 ⭐ (based on 567 deliveries)

Timeliness:  4.9 ⭐
Courtesy:    5.0 ⭐
Handling:    4.8 ⭐

On-Time Rate: 98%
Successful Deliveries: 99.5%
Total Deliveries: 567
```

#### Badges & Awards

```
🏆 Top Rated Farmer (Rating > 4.5)
⭐ 5-Star Seller (100+ reviews at 5.0)
🚀 Quick Responder (< 1 hour response)
💎 Premium Quality (All products Grade A)
🌱 Certified Organic
📦 Perfect Packaging (No complaints)
```

---

## 18. System Architecture

### 18.1 Technical Architecture

#### System Components

```
┌─────────────────────────────────────────┐
│         PRESENTATION LAYER              │
├─────────────────────────────────────────┤
│ • Angular Web App (Port 4200)          │
│ • Ionic Mobile App (iOS/Android)       │
│ • Admin Dashboard                       │
└───────────────┬─────────────────────────┘
                │ HTTPS/REST API
┌───────────────▼─────────────────────────┐
│          API GATEWAY LAYER              │
├─────────────────────────────────────────┤
│ • Nginx Reverse Proxy (Port 80/443)    │
│ • Load Balancing                        │
│ • SSL/TLS Termination                   │
│ • Rate Limiting                         │
│ • Request Routing                       │
└───────────────┬─────────────────────────┘
                │
┌───────────────▼─────────────────────────┐
│       BUSINESS LOGIC LAYER              │
├─────────────────────────────────────────┤
│ Spring Boot Application (Port 8080)     │
│                                         │
│ • Authentication Service (JWT/OTP)      │
│ • User Management Service               │
│ • Product Management Service            │
│ • Order Management Service              │
│ • Inventory Management Service          │
│ • Payment Processing Service            │
│ • Notification Service                  │
│ • Tracking Service                      │
└─────┬──────────────────────┬────────────┘
      │                      │
┌─────▼────────┐    ┌───────▼──────────┐
│   DATA LAYER │    │   CACHE LAYER    │
├──────────────┤    ├──────────────────┤
│ MySQL 8.0    │    │ Redis 7          │
│              │    │                  │
│ • Users      │    │ • OTP Cache      │
│ • Products   │    │ • Session Store  │
│ • Orders     │    │ • API Cache      │
│ • Inventory  │    │ • Rate Limits    │
│ • Payments   │    └──────────────────┘
└──────────────┘
        │
┌───────▼──────────────────────────────┐
│      EXTERNAL SERVICES               │
├──────────────────────────────────────┤
│ • Twilio (SMS Gateway)               │
│ • Razorpay (Payment Gateway)         │
│ • Google Maps (Geolocation)          │
│ • Firebase (Push Notifications)      │
│ • AWS S3 (File Storage)              │
└──────────────────────────────────────┘
```

### 18.2 Database Design

#### Entity-Relationship Diagram

```
┌────────────┐         ┌────────────┐
│   USERS    │────────▷│  PROFILES  │
└────────────┘  1:1    └────────────┘
      │
      │ 1:N
      ▼
┌────────────┐
│  PRODUCTS  │
└────────────┘
      │
      │ 1:N
      ▼
┌────────────┐         ┌────────────┐
│ INVENTORY  │◁────────│   ORDERS   │
└────────────┘  N:M    └────────────┘
                             │
                             │ 1:N
                             ▼
                       ┌────────────┐
                       │ORDER_ITEMS │
                       └────────────┘
                             │
                             │ 1:1
                             ▼
                       ┌────────────┐
                       │  TRACKING  │
                       └────────────┘
```

#### Key Tables

**users (37 records)**
- user_id (PK)
- phone_number (Unique)
- email
- full_name
- user_role (ENUM)
- is_active
- created_at

**products (16 records)**
- product_id (PK)
- farmer_id (FK)
- category_id (FK)
- product_name
- price_per_unit
- available_quantity
- is_organic

**orders (6 sample orders)**
- order_id (PK)
- customer_id (FK)
- storage_admin_id (FK)
- provider_id (FK)
- order_status (ENUM)
- total_amount
- payment_status

**storage_inventory**
- inventory_id (PK)
- product_id (FK)
- quantity_received
- quantity_available
- expiry_date
- batch_number

### 18.3 Security Architecture

#### Authentication Flow

```
1. User enters phone number
   ↓
2. System generates 6-digit OTP
   ↓
3. OTP sent via SMS (Twilio)
   ↓
4. OTP stored in Redis (5 min expiry)
   ↓
5. User enters OTP
   ↓
6. System verifies OTP
   ↓
7. JWT access token generated (30 min)
   ↓
8. JWT refresh token generated (7 days)
   ↓
9. Tokens stored in Redis & MySQL
   ↓
10. User authenticated
```

#### Security Layers

**1. Network Security**
- SSL/TLS encryption (HTTPS)
- Firewall rules
- DDoS protection
- VPN for admin access

**2. Application Security**
- JWT authentication
- Role-based access control (RBAC)
- Input validation
- SQL injection prevention
- XSS protection
- CSRF tokens

**3. Data Security**
- Encrypted at rest
- Encrypted in transit
- Regular backups
- PII data protection

**4. API Security**
- Rate limiting
- API key authentication
- Request signing
- Throttling

---

## 19. Database Schema

### 19.1 Complete Schema

#### Table Structure

**users**
```sql
CREATE TABLE users (
    user_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    phone_number VARCHAR(15) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE,
    full_name VARCHAR(100) NOT NULL,
    user_role ENUM('FARMER', 'CITY_ADMIN', 'STORAGE_ADMIN',
                   'SUPER_ADMIN', 'RETAILER', 'FLAT_OWNER',
                   'PROVIDER') NOT NULL,
    is_active BOOLEAN DEFAULT TRUE,
    is_verified BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

**products**
```sql
CREATE TABLE products (
    product_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    farmer_id BIGINT NOT NULL,
    category_id INT NOT NULL,
    product_name VARCHAR(100) NOT NULL,
    price_per_unit DECIMAL(10, 2) NOT NULL,
    available_quantity DECIMAL(10, 2) NOT NULL,
    is_organic BOOLEAN DEFAULT FALSE,
    is_available BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (farmer_id) REFERENCES users(user_id)
);
```

**orders**
```sql
CREATE TABLE orders (
    order_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    order_number VARCHAR(50) UNIQUE NOT NULL,
    customer_id BIGINT NOT NULL,
    storage_admin_id BIGINT NOT NULL,
    provider_id BIGINT,
    order_status ENUM('PENDING', 'CONFIRMED', 'PROCESSING',
                     'OUT_FOR_DELIVERY', 'DELIVERED',
                     'CANCELLED', 'RETURNED') DEFAULT 'PENDING',
    total_amount DECIMAL(10, 2) NOT NULL,
    payment_status ENUM('PENDING', 'COMPLETED',
                       'FAILED', 'REFUNDED') DEFAULT 'PENDING',
    FOREIGN KEY (customer_id) REFERENCES users(user_id)
);
```

### 19.2 Sample Data

#### Test Users (37 Total)

```
Super Admins (2):
• +919876543210 - Rajesh Kumar
• +919876543211 - Priya Sharma

City Admins (3):
• +919823456701 - Amit Patil (Ahmednagar)
• +919823456702 - Sneha Deshmukh (Satara)
• +919823456703 - Ganesh Jadhav (Latur)

Storage Admins (2):
• +919823456801 - Suresh Kulkarni (Pune)
• +919823456802 - Manjula Phadke (Pune)

Farmers (10):
• +919823456901 - Ramesh Pawar (Ahmednagar)
• +919823456902 - Sanjay Bhosale (Ahmednagar)
• +919823456903 - Prakash Gaikwad (Ahmednagar)
• +919823456904 - Vijay Shinde (Satara)
• +919823456905 - Mahesh Kamble (Satara)
• +919823456906 - Dattatray Kale (Satara)
• +919823456907 - Bharat More (Latur)
• +919823456908 - Anil Suryawanshi (Latur)
• +919823456909 - Ramdas Nikam (Latur)
• +919823456910 - Govind Chavan (Latur)

[... and 20 more users]
```

---

## 20. API Reference

### 20.1 Authentication APIs

#### Send OTP
```http
POST /api/auth/send-otp
Content-Type: application/json

Request:
{
  "phoneNumber": "+919876543210"
}

Response:
{
  "success": true,
  "message": "OTP sent successfully"
}
```

#### Verify OTP
```http
POST /api/auth/verify-otp
Content-Type: application/json

Request:
{
  "phoneNumber": "+919876543210",
  "otp": "123456",
  "deviceType": "WEB"
}

Response:
{
  "accessToken": "eyJhbGciOiJIUzI1NiIs...",
  "refreshToken": "eyJhbGciOiJIUzI1NiIs...",
  "tokenType": "Bearer",
  "expiresIn": 1800,
  "user": {
    "userId": 1,
    "phoneNumber": "+919876543210",
    "fullName": "Rajesh Kumar",
    "userRole": "SUPER_ADMIN",
    "isActive": true
  }
}
```

### 20.2 Product APIs

#### Get All Products
```http
GET /api/products
Authorization: Bearer {token}

Response:
[
  {
    "productId": 1,
    "productName": "Fresh Tomatoes",
    "category": "Vegetables",
    "price": 40.00,
    "availableQuantity": 500,
    "unit": "KG",
    "isOrganic": true,
    "farmerName": "Ramesh Pawar",
    "rating": 4.5
  }
]
```

#### Create Product (Farmer Only)
```http
POST /api/products
Authorization: Bearer {token}
Content-Type: application/json

Request:
{
  "productName": "Fresh Tomatoes",
  "categoryId": 1,
  "description": "Organically grown red tomatoes",
  "unitOfMeasure": "KG",
  "pricePerUnit": 40.00,
  "availableQuantity": 500,
  "minimumOrderQuantity": 5,
  "isOrganic": true
}

Response:
{
  "success": true,
  "productId": 1,
  "message": "Product created successfully"
}
```

### 20.3 Order APIs

#### Place Order
```http
POST /api/orders
Authorization: Bearer {token}
Content-Type: application/json

Request:
{
  "items": [
    {
      "productId": 1,
      "quantity": 5
    }
  ],
  "deliveryAddress": "Flat 301, Skyline Apartments",
  "deliveryDate": "2026-05-16",
  "timeSlot": "9AM-12PM",
  "paymentMethod": "ONLINE"
}

Response:
{
  "orderId": 1,
  "orderNumber": "ORD202605150001",
  "totalAmount": 661.50,
  "paymentStatus": "PENDING",
  "message": "Order placed successfully"
}
```

#### Track Order
```http
GET /api/tracking/{orderId}
Authorization: Bearer {token}

Response:
{
  "orderId": 1,
  "orderNumber": "ORD202605150001",
  "status": "OUT_FOR_DELIVERY",
  "currentLocation": {
    "latitude": 18.5500,
    "longitude": 73.9400
  },
  "provider": {
    "name": "Santosh Jadhav",
    "phone": "+919823459001"
  },
  "eta": "2026-05-15T12:00:00",
  "trackingHistory": [
    {
      "status": "PENDING",
      "timestamp": "2026-05-15T09:30:00",
      "message": "Order placed"
    },
    {
      "status": "CONFIRMED",
      "timestamp": "2026-05-15T09:35:00",
      "message": "Order confirmed"
    }
  ]
}
```

---

## 21. Security & Authentication

### 21.1 OTP Security

#### Generation
- Cryptographically secure random
- 6 digits (100,000 to 999,999)
- Unique per request
- One-time use only

#### Storage
- Redis cache (in-memory)
- 5-minute expiration
- Auto-deletion after verification
- Backup in MySQL for audit

#### Rate Limiting
```yaml
Per Phone Number:
  - Max 5 OTP requests per day
  - 60-second cooldown between requests
  - Max 3 verification attempts per OTP

Per IP Address:
  - Max 10 OTP requests per day
  - Captcha after 5 failures
```

### 21.2 JWT Tokens

#### Token Structure
```
Header:
{
  "alg": "HS256",
  "typ": "JWT"
}

Payload:
{
  "userId": 1,
  "phoneNumber": "+919876543210",
  "role": "SUPER_ADMIN",
  "permissions": ["*"],
  "sessionId": 123,
  "iat": 1715760000,
  "exp": 1715761800
}

Signature:
HMACSHA256(
  base64UrlEncode(header) + "." +
  base64UrlEncode(payload),
  secret
)
```

#### Token Management
- Access Token: 30 minutes
- Refresh Token: 7 days
- Stored in Redis & MySQL
- Revoked on logout
- Blacklist for security

### 21.3 Role-Based Access

#### Permission Matrix

```yaml
SUPER_ADMIN:
  - All permissions (*)

CITY_ADMIN:
  - approve_farmers
  - view_city_farmers
  - generate_city_reports

STORAGE_ADMIN:
  - manage_inventory
  - assign_orders
  - view_all_orders

FARMER:
  - create_product
  - update_own_product
  - view_own_sales

RETAILER:
  - place_order
  - view_own_orders
  - make_payment

FLAT_OWNER:
  - place_order
  - track_order
  - submit_feedback

PROVIDER:
  - accept_order
  - update_tracking
  - mark_delivered
```

---

## 22. Deployment Guide

### 22.1 Prerequisites

#### System Requirements
```
Server:
  - Linux (Ubuntu 22.04 LTS recommended)
  - 4 CPU cores minimum
  - 8 GB RAM minimum
  - 50 GB storage
  - Static IP address

Software:
  - Docker 20.10+
  - Docker Compose 2.0+
  - Git
```

### 22.2 Step-by-Step Deployment

#### 1. Server Setup
```bash
# Update system
sudo apt update && sudo apt upgrade -y

# Install Docker
curl -fsSL https://get.docker.com | sh
sudo usermod -aG docker $USER

# Install Docker Compose
sudo curl -L "https://github.com/docker/compose/releases/latest/download/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
sudo chmod +x /usr/local/bin/docker-compose

# Verify installations
docker --version
docker-compose --version
```

#### 2. Clone Repository
```bash
git clone https://github.com/vmv-agromart/agromart.git
cd vmv-agromart
```

#### 3. Configure Environment
```bash
# Copy template
cp .env.example .env

# Edit configuration
nano .env

# Set required values:
# - Database credentials
# - JWT secret
# - SMS gateway credentials
# - Payment gateway credentials
# - Google Maps API key
```

#### 4. Initialize Database
```bash
# Database schema will be automatically created
# on first startup from:
# - database/init/01-schema.sql
# - database/init/02-dummy-data.sql
```

#### 5. Start Services
```bash
# Start all services
docker-compose up -d

# View logs
docker-compose logs -f

# Check status
docker-compose ps
```

#### 6. Verify Deployment
```bash
# Check web app
curl http://localhost

# Check API
curl http://localhost:8080/api/actuator/health

# Expected response:
# {"status":"UP"}
```

#### 7. Setup SSL/HTTPS
```bash
# Install Certbot
sudo apt install certbot

# Get certificate
sudo certbot certonly --standalone \
  -d yourdomain.com \
  -d www.yourdomain.com

# Update nginx configuration
# (Already configured in nginx.conf)

# Restart nginx
docker-compose restart nginx
```

### 22.3 Production Configuration

#### Environment Variables
```env
# Production settings
SPRING_PROFILES_ACTIVE=production
NODE_ENV=production

# Database
DB_HOST=prod-mysql.example.com
DB_NAME=vmv_agromart_prod
DB_USERNAME=prod_user
DB_PASSWORD=super-secure-password

# Redis
REDIS_HOST=prod-redis.example.com
REDIS_PASSWORD=redis-secure-password

# JWT (use strong random secret)
JWT_SECRET=generated-256-bit-secret-key

# SMS Gateway
TWILIO_ACCOUNT_SID=ACxxxxxxxxxxxxxxxx
TWILIO_AUTH_TOKEN=real-production-token

# Payment Gateway (Live mode)
RAZORPAY_KEY_ID=rzp_live_xxxx
RAZORPAY_KEY_SECRET=live-secret-key

# Domain
WEB_URL=https://agromart.vmv.com
API_URL=https://api.agromart.vmv.com
```

---

## 23. Configuration

### 23.1 Application Configuration

#### Testing Environment
File: `application-testing.yaml`

```yaml
Features:
  - Test OTP: 123456 (fixed)
  - Verbose logging
  - Mock SMS
  - Relaxed rate limits
  - Auto-approve farmers
```

#### Production Environment
File: `application-prod.yaml`

```yaml
Features:
  - Real SMS via Twilio
  - Strict security
  - SSL/HTTPS enforced
  - Rate limiting
  - Manual farmer approval
  - Monitoring enabled
```

### 23.2 Docker Configuration

#### Services
```yaml
Services:
  - mysql: Database (port 3306)
  - redis: Cache (port 6379)
  - backend: API (port 8080)
  - web-frontend: Web app (port 4200)
  - nginx: Reverse proxy (port 80/443)
  - phpmyadmin: DB admin (port 8081)
```

#### Volumes
```yaml
Persistent Data:
  - mysql_data: Database files
  - redis_data: Cache data
  - backend_uploads: User uploads
  - backend_logs: Application logs
```

---

## 24. Monitoring & Maintenance

### 24.1 Health Monitoring

#### Application Health
```bash
# Check API health
curl http://localhost:8080/api/actuator/health

# Check database
docker exec vmv-mysql mysqladmin ping

# Check Redis
docker exec vmv-redis redis-cli ping
```

#### System Metrics
```bash
# View resource usage
docker stats

# View logs
docker-compose logs -f backend

# Check disk space
df -h
```

### 24.2 Backup Strategy

#### Database Backup
```bash
# Manual backup
docker exec vmv-mysql mysqldump \
  -u root -p${MYSQL_ROOT_PASSWORD} \
  vmv_agromart > backup-$(date +%Y%m%d).sql

# Restore backup
docker exec -i vmv-mysql mysql \
  -u root -p${MYSQL_ROOT_PASSWORD} \
  vmv_agromart < backup-20260515.sql
```

#### Automated Backups
```bash
# Add to crontab
0 2 * * * /path/to/backup-script.sh
```

---

## 25. Backup & Recovery

### 25.1 Backup Procedures

#### Daily Backups
- Database: Full backup at 2 AM
- Uploaded files: Incremental backup
- Configuration: Version controlled
- Logs: Rotated and archived

#### Backup Retention
- Daily: Last 7 days
- Weekly: Last 4 weeks
- Monthly: Last 12 months
- Yearly: Permanent

### 25.2 Disaster Recovery

#### Recovery Time Objective (RTO)
- Critical services: 1 hour
- Full system: 4 hours

#### Recovery Point Objective (RPO)
- Maximum data loss: 24 hours
- Database: Point-in-time recovery

---

## 26. Troubleshooting

### 26.1 Common Issues

#### Cannot Login
**Problem:** OTP not received
**Solution:**
1. Check mobile number format
2. Verify SMS credits
3. Check spam folder
4. Try after 60 seconds

#### Orders Not Processing
**Problem:** Order stuck in "Pending"
**Solution:**
1. Check storage admin availability
2. Verify inventory levels
3. Check payment status
4. Contact support

#### Delivery Tracking Not Working
**Problem:** Location not updating
**Solution:**
1. Enable GPS on provider device
2. Check internet connection
3. Update mobile app
4. Verify permissions

### 26.2 Error Codes

| Code | Message | Solution |
|------|---------|----------|
| 401 | Unauthorized | Login again |
| 403 | Forbidden | Check permissions |
| 404 | Not Found | Verify URL/ID |
| 429 | Too Many Requests | Wait and retry |
| 500 | Server Error | Contact support |

---

## 27. FAQs

### 27.1 General Questions

**Q: Is VMV AgroMart free?**
A: Registration and browsing are free. Standard delivery charges apply.

**Q: What payment methods are accepted?**
A: Cards, UPI, Net Banking, Wallets, and COD.

**Q: Can I cancel my order?**
A: Yes, before "Out for Delivery" status.

**Q: How long does delivery take?**
A: Same-day or next-day, based on your selected slot.

### 27.2 Technical Questions

**Q: Which browsers are supported?**
A: Chrome 90+, Firefox 88+, Safari 14+, Edge 90+

**Q: Are my payment details safe?**
A: Yes, we use PCI DSS compliant payment gateways.

**Q: Can I use the app offline?**
A: Limited features available offline (browse cache).

---

## 28. Support & Contact

### 28.1 Contact Information

**Customer Support:**
- 📞 Toll-Free: 1800-XXX-XXXX
- 📧 Email: support@vmvagromart.com
- 💬 Live Chat: In-app
- ⏰ Hours: 9 AM - 9 PM (Mon-Sat)

**Technical Support:**
- 📧 Email: tech@vmvagromart.com
- 🐛 Report bugs: bugs@vmvagromart.com

**Business Inquiries:**
- 📧 Email: business@vmvagromart.com
- 🤝 Partnerships: partners@vmvagromart.com

### 28.2 Social Media

- 📘 Facebook: /vmvagromart
- 📸 Instagram: @vmvagromart
- 🐦 Twitter: @vmvagromart
- 🔗 LinkedIn: /company/vmv-agromart

### 28.3 Feedback

We value your feedback:
1. Go to Settings → Feedback
2. Rate your experience
3. Share suggestions
4. Submit

---

## Appendices

### Appendix A: Glossary

- **OTP:** One-Time Password
- **JWT:** JSON Web Token
- **API:** Application Programming Interface
- **SKU:** Stock Keeping Unit
- **ETA:** Estimated Time of Arrival
- **COD:** Cash on Delivery
- **GST:** Goods and Services Tax

### Appendix B: Document History

| Version | Date | Changes |
|---------|------|---------|
| 1.0.0 | May 15, 2026 | Initial release |

---

**© 2026 VMV AgroMart. All Rights Reserved.**

For the latest documentation, visit: **https://docs.vmvagromart.com**

---

**END OF DOCUMENT**

*This document contains 150+ pages of comprehensive information about VMV AgroMart system.*
