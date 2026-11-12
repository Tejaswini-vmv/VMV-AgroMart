# VMV AgroMart - Mobile App Local Testing Guide

## Complete Guide to Testing Mobile App on Local Development Environment

**Document Version:** 1.0.0
**Last Updated:** May 15, 2026

---

## Table of Contents

1. [Overview](#overview)
2. [Prerequisites](#prerequisites)
3. [Setup Options](#setup-options)
4. [Method 1: Testing on Real Devices](#method-1-testing-on-real-devices)
5. [Method 2: Android Emulator](#method-2-android-emulator)
6. [Method 3: iOS Simulator](#method-3-ios-simulator)
7. [Method 4: Browser Testing](#method-4-browser-testing)
8. [Network Configuration](#network-configuration)
9. [Troubleshooting](#troubleshooting)
10. [Testing Checklist](#testing-checklist)

---

## Overview

VMV AgroMart mobile app can be tested locally in multiple ways:
- ✅ **Real Android/iOS devices** (Recommended)
- ✅ **Android Emulator** (Android Studio)
- ✅ **iOS Simulator** (Xcode on macOS)
- ✅ **Browser** (Chrome DevTools mobile view)

---

## Prerequisites

### Required Software

#### For All Methods
```bash
✅ Docker & Docker Compose (backend services)
✅ Node.js 18+ (for Ionic)
✅ Ionic CLI
```

#### Platform-Specific
```bash
Android Testing:
✅ Android Studio (for emulator)
✅ Android SDK
✅ Java JDK 11+

iOS Testing (macOS only):
✅ Xcode 14+
✅ CocoaPods
✅ iOS SDK
```

### Installation Commands

```bash
# Install Node.js (if not installed)
# Download from: https://nodejs.org

# Install Ionic CLI globally
npm install -g @ionic/cli

# Install Capacitor CLI
npm install -g @capacitor/cli

# Verify installations
ionic --version
npx cap --version
```

---

## Setup Options

### Quick Comparison

| Method | Pros | Cons | Best For |
|--------|------|------|----------|
| **Real Device** | Real user experience, all features work | Requires physical device | Final testing |
| **Android Emulator** | No device needed, easy debugging | Slow, resource heavy | Development |
| **iOS Simulator** | Fast, built-in Xcode | macOS only, no camera | Development |
| **Browser** | Fastest, instant reload | Limited native features | UI testing |

---

## Method 1: Testing on Real Devices

### 🎯 Recommended Method - Best for realistic testing

### Step 1: Start Backend Services

```bash
# Navigate to project directory
cd vmv-agromart

# Start all backend services
docker-compose up -d

# Verify services are running
docker-compose ps

# Expected output:
# vmv-backend     running
# vmv-mysql       running
# vmv-redis       running
# vmv-nginx       running
```

### Step 2: Find Your Computer's IP Address

#### On Windows:
```cmd
# Open Command Prompt
ipconfig

# Look for "IPv4 Address" under your active network
# Example: 192.168.1.100
```

#### On macOS/Linux:
```bash
# Method 1: ifconfig
ifconfig | grep "inet "

# Method 2: ip command
ip addr show

# Look for IP address (usually 192.168.x.x)
# Example: 192.168.1.100
```

**Common IP Patterns:**
- WiFi: `192.168.1.x` or `192.168.0.x`
- Ethernet: `192.168.1.x` or `10.0.0.x`

### Step 3: Configure Mobile App

```bash
# Navigate to mobile app directory
cd mobile-app

# Install dependencies
npm install

# Create environment file
cat > src/environments/environment.ts << 'EOF'
export const environment = {
  production: false,
  apiUrl: 'http://YOUR_COMPUTER_IP:8080/api'
};
EOF

# Replace YOUR_COMPUTER_IP with actual IP
# Example: http://192.168.1.100:8080/api
```

### Step 4: Start Development Server

```bash
# Start Ionic development server
ionic serve --external --port 8100

# Output will show:
# Local: http://localhost:8100
# External: http://192.168.1.100:8100
#
# Use External URL for mobile devices!
```

### Step 5: Connect Mobile Device

#### For Android:

**A. Enable Developer Options**
1. Go to Settings → About Phone
2. Tap "Build Number" 7 times
3. Developer Options enabled!

**B. Enable USB Debugging**
1. Settings → Developer Options
2. Enable "USB Debugging"
3. Connect phone via USB

**C. Connect to Same WiFi**
1. Ensure phone and computer on same WiFi network
2. Open browser on phone
3. Navigate to: `http://192.168.1.100:8100`

**Visual Guide:**
```
Your Computer (192.168.1.100)
  ├─ Docker (Backend: 8080)
  └─ Ionic Dev Server (Frontend: 8100)
         │
         │ WiFi Network
         ▼
Your Phone (192.168.1.105)
  └─ Browser: http://192.168.1.100:8100
```

#### For iOS:

**A. Requirements**
1. iPhone with iOS 12+
2. Same WiFi network
3. Safari browser

**B. Access App**
1. Open Safari on iPhone
2. Navigate to: `http://192.168.1.100:8100`
3. Tap Share button → "Add to Home Screen"
4. App appears on home screen!

### Step 6: Testing Features

**Test OTP Login:**
```
1. Open app on phone
2. Enter test number: +919823458001
3. Tap "Send OTP"
4. Enter test OTP: 123456
5. Should login successfully
```

**Test Order Placement:**
```
1. Browse products
2. Add items to cart
3. Proceed to checkout
4. Place order
5. Track delivery
```

**Test GPS Features:**
```
1. Grant location permission
2. View nearby farms (if implemented)
3. Track delivery on map
```

**Test Camera:**
```
1. Add product photo (farmer)
2. Upload feedback images (customer)
```

**Test Push Notifications:**
```
1. Grant notification permission
2. Place an order
3. Receive status updates
```

### Step 7: Live Reload Testing

**Automatic Updates:**
- Change code in your editor
- Save file
- App automatically reloads on phone
- See changes instantly!

**Example Workflow:**
```bash
# Edit a component
nano src/app/pages/home/home.page.ts

# Save changes
# Phone automatically refreshes
# See updates immediately
```

---

## Method 2: Android Emulator

### Step 1: Install Android Studio

```bash
# Download Android Studio
# https://developer.android.com/studio

# Install Android SDK
# Open Android Studio → SDK Manager
# Install:
#   - Android SDK Platform 33
#   - Android SDK Build-Tools
#   - Android Emulator
```

### Step 2: Create Android Virtual Device (AVD)

```bash
# Open Android Studio
# Tools → AVD Manager → Create Virtual Device

# Choose device:
# - Pixel 6 (Recommended)
# - Medium Phone with Google Play

# Select System Image:
# - Release Name: Tiramisu
# - API Level: 33
# - Target: Android 13.0

# Configure AVD:
# - RAM: 2048 MB
# - Internal Storage: 2048 MB
# - Enable Device Frame: Yes

# Finish and launch emulator
```

### Step 3: Build for Android

```bash
# Navigate to mobile app
cd mobile-app

# Install dependencies
npm install

# Configure environment
export API_URL=http://10.0.2.2:8080/api
# Note: 10.0.2.2 is emulator's way to access host machine

# Update environment file
cat > src/environments/environment.ts << 'EOF'
export const environment = {
  production: false,
  apiUrl: 'http://10.0.2.2:8080/api'
};
EOF

# Build the app
ionic build

# Add Android platform
npx cap add android

# Sync with Capacitor
npx cap sync android

# Open in Android Studio
npx cap open android
```

### Step 4: Run on Emulator

**In Android Studio:**
```
1. Select your AVD from device dropdown
2. Click Run button (▶️)
3. App installs and launches on emulator
4. Test all features
```

**Command Line Alternative:**
```bash
# Start emulator
emulator -avd Pixel_6_API_33

# Install and run app
npx cap run android
```

### Step 5: Enable Developer Tools

**Chrome DevTools for Android:**
```
1. Open Chrome on computer
2. Go to: chrome://inspect
3. Find your app in device list
4. Click "Inspect"
5. Debug JavaScript, view console, network
```

### Step 6: Testing Emulator Features

**GPS Location:**
```
In emulator:
1. Click "..." (Extended Controls)
2. Go to "Location"
3. Enter coordinates or use map
4. Send location to app
```

**Camera:**
```
Emulator uses your webcam
Or displays default test images
```

**Notifications:**
```
1. Pull down notification shade
2. See app notifications
3. Test notification clicks
```

---

## Method 3: iOS Simulator (macOS Only)

### Step 1: Install Xcode

```bash
# Install Xcode from Mac App Store
# https://apps.apple.com/app/xcode

# Install Command Line Tools
xcode-select --install

# Install CocoaPods
sudo gem install cocoapods
```

### Step 2: Build for iOS

```bash
# Navigate to mobile app
cd mobile-app

# Install dependencies
npm install

# Update environment for local testing
cat > src/environments/environment.ts << 'EOF'
export const environment = {
  production: false,
  apiUrl: 'http://localhost:8080/api'
};
EOF

# Build the app
ionic build

# Add iOS platform
npx cap add ios

# Install iOS dependencies
cd ios/App
pod install
cd ../..

# Sync with Capacitor
npx cap sync ios

# Open in Xcode
npx cap open ios
```

### Step 3: Run on Simulator

**In Xcode:**
```
1. Select simulator device (iPhone 14)
2. Click Run button (▶️)
3. Simulator launches
4. App installs and opens
5. Test features
```

**Available Simulators:**
- iPhone 14 Pro Max (large screen)
- iPhone 14 (standard size)
- iPhone SE (small screen)
- iPad Pro (tablet)

### Step 4: Simulator Features

**Location Testing:**
```
Debug → Location → Custom Location
Enter latitude/longitude
Or choose: Apple, City Bicycle Ride, City Run, etc.
```

**Network Conditions:**
```
Debug → Network Link Conditioner
Choose: 3G, LTE, WiFi, or custom
Test slow connections
```

**Screenshots:**
```
Cmd + S: Save screenshot
```

---

## Method 4: Browser Testing

### Fastest Method for UI Testing

### Step 1: Start Dev Server

```bash
# Start backend
docker-compose up -d

# Start mobile app
cd mobile-app
ionic serve --port 8100
```

### Step 2: Open in Browser

```
URL: http://localhost:8100
Browser: Chrome (Recommended)
```

### Step 3: Enable Mobile View

**Chrome DevTools:**
```
1. Press F12 (or Cmd+Option+I on Mac)
2. Click "Toggle Device Toolbar" (or Ctrl+Shift+M)
3. Select device:
   - iPhone 12 Pro
   - Pixel 5
   - iPad Pro
4. Test responsive design
```

**Responsive Dimensions:**
```
iPhone 14:     390 x 844
iPhone 14 Pro Max: 428 x 926
Pixel 5:       393 x 851
iPad Pro:      1024 x 1366
```

### Step 4: Test Features

**What Works:**
✅ All UI components
✅ Navigation
✅ Forms and validation
✅ API calls
✅ Responsive design
✅ Touch gestures

**What Doesn't Work:**
❌ Native camera
❌ Real GPS
❌ Push notifications
❌ Biometric auth
❌ Native file access

---

## Network Configuration

### Understanding Network Setup

#### Docker Network
```
Docker Internal Network: 172.20.0.0/16
  ├─ MySQL:    172.20.0.2:3306
  ├─ Redis:    172.20.0.3:6379
  ├─ Backend:  172.20.0.4:8080
  └─ Nginx:    172.20.0.5:80
```

#### Host Machine
```
Your Computer: 192.168.1.100
  ├─ Docker exposed ports:
  │   ├─ 3306 → MySQL
  │   ├─ 6379 → Redis
  │   ├─ 8080 → Backend API
  │   └─ 80 → Nginx
  └─ Ionic: 8100
```

#### Mobile Device Access

**Same WiFi Network:**
```
Phone (192.168.1.105)
  ↓
Router (192.168.1.1)
  ↓
Computer (192.168.1.100:8100)
  ↓
Docker Backend (172.20.0.4:8080)
```

### API URL Configuration

**For Different Scenarios:**

```typescript
// Real device on same WiFi
apiUrl: 'http://192.168.1.100:8080/api'

// Android Emulator
apiUrl: 'http://10.0.2.2:8080/api'

// iOS Simulator
apiUrl: 'http://localhost:8080/api'

// Browser testing
apiUrl: 'http://localhost:8080/api'

// Production
apiUrl: 'https://api.agromart.vmv.com/api'
```

### Firewall Configuration

**Windows Firewall:**
```cmd
# Allow incoming connections on port 8100
netsh advfirewall firewall add rule ^
  name="Ionic Dev Server" ^
  dir=in action=allow protocol=TCP localport=8100
```

**macOS Firewall:**
```bash
# System Preferences → Security & Privacy → Firewall
# Click "Firewall Options"
# Add "Node" and allow incoming connections
```

**Linux (UFW):**
```bash
sudo ufw allow 8100/tcp
sudo ufw reload
```

---

## Troubleshooting

### Common Issues & Solutions

#### Issue 1: Cannot Access from Phone

**Symptoms:**
- Browser shows "Can't reach this page"
- Connection timeout

**Solutions:**
```bash
# 1. Verify both devices on same WiFi
# Settings → WiFi → Check network name

# 2. Test computer IP
ping 192.168.1.100

# 3. Test Ionic server
curl http://localhost:8100

# 4. Check firewall
# Windows: Turn off temporarily to test
# macOS: System Preferences → Firewall → Off

# 5. Restart Ionic server
ionic serve --external --disableHostCheck
```

#### Issue 2: API Calls Failing

**Symptoms:**
- Login not working
- "Network Error" messages
- API requests timeout

**Solutions:**
```bash
# 1. Verify backend is running
docker-compose ps
# All services should show "running"

# 2. Test API directly
curl http://localhost:8080/api/actuator/health
# Should return: {"status":"UP"}

# 3. Check API URL in app
# src/environments/environment.ts
# Verify it matches your computer's IP

# 4. Test from phone browser
# Open: http://192.168.1.100:8080/api/actuator/health
# Should show health status

# 5. Check backend logs
docker-compose logs -f backend
```

#### Issue 3: CORS Errors

**Symptoms:**
- Console shows CORS errors
- "Access-Control-Allow-Origin" error

**Solutions:**
```bash
# 1. Add your IP to CORS allowed origins
# Edit backend application-testing.yaml

cors:
  allowed-origins:
    - http://localhost:8100
    - http://192.168.1.100:8100  # Add your IP
    - http://192.168.1.105:8100  # Add phone IP

# 2. Restart backend
docker-compose restart backend

# 3. Clear browser cache
# Chrome DevTools → Network → Disable cache
```

#### Issue 4: Slow Performance

**Symptoms:**
- App loads slowly
- Laggy interactions
- Images not loading

**Solutions:**
```bash
# 1. Use production build
ionic build --prod

# 2. Reduce hot reload overhead
ionic serve --no-open --no-livereload

# 3. Optimize Docker
docker system prune

# 4. Check network speed
# Both devices should be on 5GHz WiFi

# 5. Use wired connection for computer
```

#### Issue 5: Android Emulator Issues

**Symptoms:**
- Emulator won't start
- App crashes immediately
- Very slow performance

**Solutions:**
```bash
# 1. Enable hardware acceleration
# BIOS → Enable VT-x (Intel) or AMD-V

# 2. Increase emulator RAM
# AVD Manager → Edit → RAM: 4096 MB

# 3. Use x86_64 system image
# Not ARM (ARM is slow)

# 4. Cold boot emulator
# AVD Manager → Cold Boot Now

# 5. Wipe emulator data
# AVD Manager → Wipe Data
```

#### Issue 6: iOS Simulator Issues

**Symptoms:**
- Build fails
- Simulator not found
- App crashes

**Solutions:**
```bash
# 1. Update Xcode
# App Store → Updates

# 2. Clean build
cd ios/App
pod deintegrate
pod install
cd ../..
ionic build
npx cap sync ios

# 3. Reset simulator
# Device → Erase All Content and Settings

# 4. Check iOS deployment target
# Xcode → Project → Deployment Info → iOS 12.0+
```

---

## Testing Checklist

### Pre-Testing Setup
```
□ Backend services running (docker-compose ps)
□ Database initialized with dummy data
□ Computer IP address identified
□ Firewall configured
□ Mobile app dependencies installed (npm install)
□ Environment configured with correct API URL
```

### Real Device Testing
```
□ Phone connected to same WiFi
□ Can access dev server (http://IP:8100)
□ Login with test account works
□ Browse products loads correctly
□ Add to cart functions
□ Checkout process works
□ Payment integration (if configured)
□ GPS location (if used)
□ Camera for photos (if used)
□ Push notifications (if configured)
```

### Feature Testing
```
□ Authentication Flow
  □ Send OTP
  □ Receive OTP (check SMS)
  □ Verify OTP
  □ Login successful

□ Product Browsing
  □ Load product list
  □ Search products
  □ Filter by category
  □ View product details

□ Shopping Cart
  □ Add products
  □ Update quantities
  □ Remove items
  □ View total price

□ Checkout
  □ Enter delivery address
  □ Select time slot
  □ Choose payment method
  □ Place order

□ Order Tracking
  □ View order list
  □ Track specific order
  □ See delivery status
  □ View on map (if implemented)

□ Profile Management
  □ View profile
  □ Edit details
  □ Update address
  □ View order history
```

### Performance Testing
```
□ App loads in < 3 seconds
□ Product images load quickly
□ Smooth scrolling
□ No freezing or crashes
□ API responses < 2 seconds
```

### Cross-Device Testing
```
□ Test on Android phone
□ Test on iPhone
□ Test on Android tablet
□ Test on iPad
□ Test on different screen sizes
```

---

## Quick Reference Commands

### Start Everything
```bash
# In project root
docker-compose up -d

# In mobile-app directory
ionic serve --external
```

### Stop Everything
```bash
# Stop Docker services
docker-compose down

# Stop Ionic (Ctrl+C in terminal)
```

### View Logs
```bash
# Backend logs
docker-compose logs -f backend

# All logs
docker-compose logs -f

# Ionic dev server logs
# (shown in terminal where you ran ionic serve)
```

### Rebuild Mobile App
```bash
cd mobile-app
rm -rf node_modules
npm install
ionic build
npx cap sync
```

### Test API Connectivity
```bash
# From computer
curl http://localhost:8080/api/actuator/health

# From phone browser
http://192.168.1.100:8080/api/actuator/health
```

---

## Best Practices

### Development Workflow

**1. Start with Browser Testing**
- Fastest feedback loop
- Debug with Chrome DevTools
- Get UI right first

**2. Move to Real Device**
- Test actual user experience
- Verify native features
- Check performance

**3. Test on Multiple Devices**
- Different screen sizes
- Different OS versions
- Different network conditions

### Code Changes Workflow

```bash
# 1. Make code changes in editor

# 2. Save file
# App auto-reloads on device

# 3. If issues, check console
# Chrome DevTools or Safari Inspector

# 4. If major changes:
ionic build
npx cap sync
# Rerun app
```

### Network Testing

**Test Different Conditions:**
- Fast WiFi (5GHz)
- Slow WiFi (2.4GHz)
- Mobile data (4G/5G)
- Slow connection (3G simulation)
- Offline mode

---

## Additional Resources

### Official Documentation
- Ionic Framework: https://ionicframework.com/docs
- Capacitor: https://capacitorjs.com/docs
- Android Studio: https://developer.android.com
- Xcode: https://developer.apple.com/xcode

### Video Tutorials
- Ionic Dev Server: [YouTube link]
- Android Emulator Setup: [YouTube link]
- iOS Simulator Guide: [YouTube link]

### Community Support
- Ionic Forum: https://forum.ionicframework.com
- Stack Overflow: #ionic-framework
- Discord: Ionic Community

---

## Summary

**Recommended Testing Flow:**

```
1. Start Docker Services
   ↓
2. Get Computer IP Address
   ↓
3. Configure API URL in Mobile App
   ↓
4. Start Ionic Dev Server
   ↓
5. Connect Phone to Same WiFi
   ↓
6. Open App in Phone Browser
   ↓
7. Test All Features
   ↓
8. Debug Issues with DevTools
```

**Quick Start:**
```bash
# One-command start
docker-compose up -d && \
cd mobile-app && \
ionic serve --external

# Then access from phone:
# http://YOUR_IP:8100
```

---

**Need Help?**
- Check logs: `docker-compose logs -f`
- Verify network: `ping YOUR_IP`
- Test API: `curl http://YOUR_IP:8080/api/actuator/health`
- Support: support@vmvagromart.com

---

© 2026 VMV AgroMart - Mobile App Testing Guide
