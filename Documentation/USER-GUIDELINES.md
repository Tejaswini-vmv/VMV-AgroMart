# VMV AgroMart - User Guidelines

**Version:** 1.0.0
**Date:** May 15, 2026
**Document Type:** User Manual

---

## Table of Contents

1. [Introduction](#introduction)
2. [Getting Started](#getting-started)
3. [User Roles & Access](#user-roles--access)
4. [Login & Authentication](#login--authentication)
5. [Role-Specific Guides](#role-specific-guides)
6. [Common Features](#common-features)
7. [Troubleshooting](#troubleshooting)
8. [FAQs](#faqs)
9. [Support](#support)

---

## 1. Introduction

### 1.1 About VMV AgroMart

VMV AgroMart is a comprehensive agricultural marketplace platform that connects farmers directly to storage facilities, retailers, and end customers. The platform facilitates the complete supply chain from farm to consumer with features including:

- ✅ **OTP-Based Authentication** - Secure, password-less login
- 📦 **Order Management** - Complete order lifecycle tracking
- 🗺️ **Real-Time Tracking** - GPS-based delivery monitoring
- 💳 **Secure Payments** - Integrated payment gateway
- ⭐ **Feedback System** - Rate and review products
- 📱 **Mobile & Web** - Access from any device

### 1.2 Who Can Use VMV AgroMart?

| User Type | Description | Primary Functions |
|-----------|-------------|-------------------|
| **Farmer** | Agricultural producers | List products, manage inventory |
| **City Admin** | City-level managers | Approve farmers, oversee operations |
| **Storage Admin** | Warehouse managers | Manage central inventory, assign deliveries |
| **Super Admin** | System administrators | Full system access and management |
| **Retailer** | Bulk buyers | Place daily/bulk orders |
| **Flat Owner** | End consumers | Purchase fresh produce on-demand |
| **Provider** | Delivery personnel | Accept and complete deliveries |

---

## 2. Getting Started

### 2.1 System Requirements

**For Web Application:**
- Modern web browser (Chrome, Firefox, Safari, Edge)
- Internet connection (minimum 2 Mbps)
- Screen resolution: 1024x768 or higher

**For Mobile Application:**
- Android 8.0+ or iOS 12+
- 50 MB free storage space
- Active internet connection
- GPS enabled (for delivery tracking)

### 2.2 Accessing the Application

**Web Application:**
```
URL: https://agromart.vmv.com
```

**Mobile Application:**
- Download from Google Play Store (Android)
- Download from Apple App Store (iOS)
- Search for "VMV AgroMart"

**[SCREENSHOT: Landing page showing login screen]**

### 2.3 First-Time Setup

1. Open the application
2. Enter your registered mobile number
3. Receive OTP via SMS
4. Enter the 6-digit OTP
5. Complete your profile information
6. Start using the platform

---

## 3. User Roles & Access

### 3.1 Role-Based Access Control

VMV AgroMart implements strict role-based access control to ensure data security and proper workflow:

#### Farmer Access
- ✅ Add and manage own products
- ✅ View product listings
- ✅ Update inventory levels
- ✅ View sales statistics
- ❌ Cannot view other farmers' data
- ❌ Cannot approve other farmers

#### City Admin Access
- ✅ Approve/reject farmers in their city
- ✅ View all farmers in their jurisdiction
- ✅ Generate city-level reports
- ✅ Manage city-specific operations
- ❌ Cannot access other cities' data
- ❌ Cannot modify system settings

#### Storage Admin Access
- ✅ Manage central inventory
- ✅ Track incoming/outgoing products
- ✅ Assign orders to delivery providers
- ✅ View all orders and deliveries
- ✅ Generate warehouse reports
- ❌ Cannot modify farmer data

#### Retailer/Customer Access
- ✅ Browse products
- ✅ Place orders
- ✅ Track deliveries
- ✅ Make payments
- ✅ Rate and review products
- ❌ Cannot view wholesale prices (customers)
- ❌ Cannot access admin functions

---

## 4. Login & Authentication

### 4.1 OTP-Based Login Process

VMV AgroMart uses a secure, password-less authentication system:

**Step 1: Enter Mobile Number**
1. Open the application
2. Click "Login" or "Get Started"
3. Enter your 10-digit mobile number with +91 prefix
   - Format: +919876543210
4. Click "Send OTP"

**[SCREENSHOT: Mobile number entry screen]**

**Step 2: Receive OTP**
- You will receive a 6-digit OTP via SMS
- OTP is valid for 5 minutes
- Format: "Your VMV AgroMart code is: 123456"

**[SCREENSHOT: SMS notification with OTP]**

**Step 3: Enter OTP**
1. Enter the 6-digit code received via SMS
2. Click "Verify OTP"
3. You will be logged in automatically

**[SCREENSHOT: OTP verification screen]**

### 4.2 Test Login Credentials

For testing purposes, use these credentials:

#### Super Admin
```
Phone: +919876543210
Name: Rajesh Kumar
Email: admin1@vmvagromart.com
```

#### City Admin (Ahmednagar)
```
Phone: +919823456701
Name: Amit Patil
Email: ahmednagar.admin@vmvagromart.com
```

#### Farmer
```
Phone: +919823456901
Name: Ramesh Pawar
Email: farmer1@gmail.com
```

#### Retailer
```
Phone: +919823457001
Name: Sagar Enterprises
Email: retailer1@shopmail.com
```

#### Customer
```
Phone: +919823458001
Name: Neha Kulkarni
Email: customer1@gmail.com
```

### 4.3 Login Troubleshooting

**Problem:** Not receiving OTP
- **Solution:** Check if you entered the correct mobile number
- Ensure good network connectivity
- Wait 60 seconds before requesting a new OTP
- Check SMS inbox and spam folder

**Problem:** OTP expired
- **Solution:** Click "Resend OTP" to get a new code
- Enter the new OTP within 5 minutes

**Problem:** Invalid OTP error
- **Solution:** Ensure you entered all 6 digits correctly
- Don't add spaces or special characters
- Try resending OTP if issue persists

---

## 5. Role-Specific Guides

### 5.1 Farmer Guide

#### Adding Products

**Step 1: Navigate to Products**
1. Login to your farmer account
2. Click "My Products" from dashboard
3. Click "+ Add Product" button

**[SCREENSHOT: Farmer dashboard with "Add Product" button highlighted]**

**Step 2: Fill Product Details**
- **Product Name:** Enter product name (e.g., "Fresh Tomatoes")
- **Category:** Select from dropdown (Vegetables, Fruits, etc.)
- **Description:** Detailed description (optional)
- **Unit of Measure:** Select KG, GRAM, PIECE, etc.
- **Price per Unit:** Enter price in INR
- **Available Quantity:** Enter available stock
- **Is Organic:** Check if organically grown
- **Upload Photo:** Add product image (optional)

**[SCREENSHOT: Add product form with all fields]**

**Step 3: Submit Product**
1. Review all entered information
2. Click "Submit Product"
3. Product will be added to your listing
4. Wait for city admin approval

**[SCREENSHOT: Success message after product submission]**

#### Managing Inventory

**Update Stock Levels:**
1. Go to "My Products"
2. Find the product
3. Click "Update Stock"
4. Enter new quantity
5. Click "Save"

**[SCREENSHOT: Update stock dialog]**

**Mark Product Unavailable:**
1. Find the product
2. Toggle "Available" switch to OFF
3. Product will be hidden from buyers

#### Viewing Sales

**Check Sales Statistics:**
1. Go to "Dashboard"
2. View "Total Sales" section
3. See breakdown by product
4. View date-wise sales graph

**[SCREENSHOT: Farmer sales dashboard with graphs]**

---

### 5.2 Storage Admin Guide

#### Managing Incoming Products

**Step 1: View Incoming Shipments**
1. Login to storage admin account
2. Go to "Incoming Products"
3. View list of expected deliveries

**[SCREENSHOT: Incoming products list]**

**Step 2: Receive Products**
1. Click on incoming shipment
2. Verify product details
3. Enter actual received quantity
4. Assign batch number
5. Set storage location
6. Set quality grade (A/B/C)
7. Click "Confirm Receipt"

**[SCREENSHOT: Product receipt form]**

**Step 3: Update Inventory**
- Products are automatically added to inventory
- View in "Storage Inventory" section

#### Assigning Delivery Orders

**Step 1: View Pending Orders**
1. Go to "Orders" → "Pending"
2. View all unassigned orders

**[SCREENSHOT: Pending orders list]**

**Step 2: Assign Provider**
1. Click on an order
2. Click "Assign Provider"
3. Select available delivery personnel
4. Add special instructions (if any)
5. Click "Assign Order"

**[SCREENSHOT: Provider assignment dialog]**

#### Generating Reports

**Daily Inventory Report:**
1. Go to "Reports"
2. Select "Inventory Report"
3. Choose date range
4. Click "Generate"
5. Download PDF or Excel

**[SCREENSHOT: Report generation interface]**

---

### 5.3 Retailer/Customer Guide

#### Browsing Products

**Step 1: View Product Catalog**
1. Login to your account
2. Go to "Browse Products"
3. View all available products

**[SCREENSHOT: Product catalog with filters]**

**Step 2: Filter Products**
- **By Category:** Select Vegetables, Fruits, etc.
- **By Price:** Use price range slider
- **Organic Only:** Toggle organic filter
- **Sort By:** Price, Rating, Newest

**[SCREENSHOT: Product filters panel]**

#### Placing an Order

**Step 1: Add to Cart**
1. Browse products
2. Click on a product for details
3. Enter quantity
4. Click "Add to Cart"

**[SCREENSHOT: Product detail page]**

**Step 2: Review Cart**
1. Click cart icon
2. Review all items
3. Update quantities if needed
4. Remove unwanted items
5. Click "Proceed to Checkout"

**[SCREENSHOT: Shopping cart]**

**Step 3: Enter Delivery Details**
- **Delivery Address:** Enter or select saved address
- **Delivery Date:** Select required date
- **Time Slot:** Choose preferred time
- **Special Instructions:** Add any notes
- **Payment Method:** Select Online/COD

**[SCREENSHOT: Checkout form]**

**Step 4: Make Payment**

**For Online Payment:**
1. Enter card details or select UPI
2. Click "Pay Now"
3. Complete payment on gateway
4. Wait for confirmation

**[SCREENSHOT: Payment gateway]**

**For Cash on Delivery:**
1. Select "Cash on Delivery"
2. Click "Place Order"
3. Order confirmed immediately

**Step 5: Order Confirmation**
- Order number will be displayed
- Confirmation SMS sent to mobile
- Email confirmation (if email provided)

**[SCREENSHOT: Order confirmation screen]**

#### Tracking Orders

**Step 1: View Your Orders**
1. Go to "My Orders"
2. View list of all orders

**[SCREENSHOT: Orders list]**

**Step 2: Track Specific Order**
1. Click on an order
2. View order status timeline
3. See current location on map (when out for delivery)
4. View estimated delivery time

**[SCREENSHOT: Order tracking with map]**

**Order Status Explained:**
- 🔵 **Pending:** Order received, awaiting confirmation
- 🟡 **Confirmed:** Order confirmed by storage admin
- 🟠 **Processing:** Order being prepared for delivery
- 🟣 **Out for Delivery:** Delivery person on the way
- 🟢 **Delivered:** Order delivered successfully
- 🔴 **Cancelled:** Order was cancelled

#### Rating & Feedback

**After Delivery:**
1. Go to completed order
2. Click "Rate Order"
3. Rate product quality (1-5 stars)
4. Rate delivery experience (1-5 stars)
5. Write feedback (optional)
6. Upload photos (optional)
7. Click "Submit Review"

**[SCREENSHOT: Rating and review form]**

---

### 5.4 Delivery Provider Guide

#### Accepting Orders

**Step 1: View Available Orders**
1. Login to provider account
2. Go to "Available Orders"
3. View orders assigned to you

**[SCREENSHOT: Provider dashboard]**

**Step 2: Accept Order**
1. Click on an order
2. Review delivery details
3. Check route on map
4. Click "Accept Order"

**[SCREENSHOT: Order acceptance screen]**

#### Updating Delivery Status

**During Pickup:**
1. Reach storage warehouse
2. Click "Confirm Pickup"
3. Verify items with warehouse staff
4. Take photo of loaded items
5. Start navigation to delivery address

**[SCREENSHOT: Pickup confirmation]**

**During Delivery:**
1. App automatically tracks your location
2. Customer can see live location
3. Call customer if needed

**On Delivery:**
1. Reach delivery address
2. Click "Mark as Delivered"
3. Collect payment if COD
4. Get customer signature (on phone)
5. Take delivery photo
6. Click "Complete Delivery"

**[SCREENSHOT: Delivery completion form]**

---

## 6. Common Features

### 6.1 Profile Management

**Updating Profile:**
1. Click profile icon
2. Go to "Edit Profile"
3. Update information
4. Click "Save Changes"

**[SCREENSHOT: Profile edit screen]**

**Profile Information:**
- Full Name
- Email Address
- Profile Photo
- Address Details
- Bank Details (for farmers)
- Business Details (for retailers)

### 6.2 Notifications

**Viewing Notifications:**
1. Click bell icon
2. View all notifications
3. Click to see details

**[SCREENSHOT: Notifications panel]**

**Notification Types:**
- Order updates
- Payment confirmations
- Product approvals
- Delivery status
- System messages

### 6.3 Search Functionality

**Global Search:**
1. Click search icon
2. Enter keywords
3. View results across:
   - Products
   - Orders
   - Users (admin only)

**[SCREENSHOT: Search results page]**

---

## 7. Troubleshooting

### 7.1 Common Issues

#### Cannot Login
**Symptoms:** OTP not received or invalid
**Solutions:**
1. Check mobile number format (+91XXXXXXXXXX)
2. Ensure good network connection
3. Wait 60 seconds before resending
4. Contact support if issue persists

#### Payment Failed
**Symptoms:** Payment not completing
**Solutions:**
1. Check internet connection
2. Verify card details
3. Ensure sufficient balance
4. Try different payment method
5. Contact your bank if needed

#### Order Not Visible
**Symptoms:** Cannot see placed order
**Solutions:**
1. Refresh the page
2. Check "My Orders" section
3. Verify order confirmation SMS
4. Contact support with order number

#### App Crashes
**Symptoms:** Mobile app closes unexpectedly
**Solutions:**
1. Clear app cache
2. Update to latest version
3. Restart device
4. Reinstall app if needed

### 7.2 Error Messages

| Error Message | Meaning | Solution |
|---------------|---------|----------|
| "Invalid phone number" | Wrong format | Use +91XXXXXXXXXX format |
| "OTP expired" | OTP older than 5 min | Request new OTP |
| "Maximum attempts exceeded" | Too many wrong OTPs | Wait 1 hour or contact support |
| "Product unavailable" | Stock depleted | Choose different product |
| "Payment gateway error" | Payment failed | Retry or use different method |
| "Session expired" | Logged out due to inactivity | Login again |

---

## 8. FAQs

### General Questions

**Q: Is VMV AgroMart free to use?**
A: Yes, registration and browsing are free. Standard delivery charges apply to orders.

**Q: What payment methods are accepted?**
A: We accept:
- Credit/Debit Cards
- UPI (Google Pay, PhonePe, Paytm)
- Net Banking
- Cash on Delivery (COD)

**Q: What is the minimum order value?**
A: Minimum order value is ₹200.

**Q: Are delivery charges extra?**
A: Yes, ₹50 for orders below ₹1000. Free delivery for orders above ₹1000.

**Q: Can I cancel my order?**
A: Yes, orders can be cancelled before they are out for delivery.

**Q: How long does delivery take?**
A: Same-day or next-day delivery depending on your selected slot.

### For Farmers

**Q: How do I become an approved farmer?**
A: Register with your details, get approval from your city admin, and start listing products.

**Q: When will I receive payment?**
A: Payments are processed within 7 working days of delivery.

**Q: Can I update product prices?**
A: Yes, you can update prices anytime from your products dashboard.

### For Customers

**Q: Are products organic?**
A: Products marked "Organic" are certified organic produce.

**Q: Can I return products?**
A: Yes, returns accepted within 24 hours of delivery for quality issues.

**Q: How do I track my delivery?**
A: Use the "Track Order" feature to see real-time location of your delivery.

---

## 9. Support

### 9.1 Contact Information

**Customer Support:**
- 📞 Phone: 1800-XXX-XXXX (Toll-Free)
- 📧 Email: support@vmvagromart.com
- 💬 Live Chat: Available in app
- ⏰ Hours: 9 AM - 9 PM (Mon-Sat)

**Technical Support:**
- 📧 Email: tech@vmvagromart.com
- For app issues and technical problems

**Business Inquiries:**
- 📧 Email: business@vmvagromart.com
- For partnerships and collaborations

### 9.2 Feedback

We value your feedback! Share your experience:
1. Go to "Feedback" in menu
2. Rate your experience
3. Share suggestions
4. Submit

**[SCREENSHOT: Feedback form]**

### 9.3 Social Media

Stay connected:
- 📘 Facebook: /vmvagromart
- 📸 Instagram: @vmvagromart
- 🐦 Twitter: @vmvagromart
- 🔗 LinkedIn: /company/vmv-agromart

---

## Appendix A: Keyboard Shortcuts (Web)

| Shortcut | Action |
|----------|--------|
| Ctrl + / | Open search |
| Ctrl + B | Browse products |
| Ctrl + O | My orders |
| Ctrl + P | My profile |
| Ctrl + N | Notifications |
| Esc | Close modals |

---

## Appendix B: Mobile App Gestures

| Gesture | Action |
|---------|--------|
| Swipe Right | Go back |
| Pull Down | Refresh |
| Long Press | Show options |
| Pinch Zoom | Zoom product image |

---

## Document Version History

| Version | Date | Changes |
|---------|------|---------|
| 1.0.0 | May 15, 2026 | Initial release |

---

**© 2026 VMV AgroMart. All rights reserved.**

For the latest version of this document, visit: https://docs.vmvagromart.com
