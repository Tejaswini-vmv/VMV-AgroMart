# 📱 How to Test Mobile App on Your Phone - SIMPLE GUIDE

## 🎯 Goal: Test the VMV AgroMart app on your actual phone

---

## ⚡ Super Quick Start (5 Minutes)

### Step 1: Start the Backend
```bash
docker-compose up -d
```
**What this does:** Starts MySQL, Redis, and API server

---

### Step 2: Find Your Computer's IP Address

**On Windows:**
```cmd
ipconfig
```
Look for: **IPv4 Address** → Example: `192.168.1.100`

**On Mac/Linux:**
```bash
ifconfig | grep "inet " | grep -v 127.0.0.1
```
Example output: `192.168.1.100`

**Write it down:** `MY IP: _______________`

---

### Step 3: Start Mobile Dev Server
```bash
cd mobile-app
ionic serve --external
```

You'll see:
```
Local:    http://localhost:8100
External: http://192.168.1.100:8100  ← Use this one!
```

---

### Step 4: Connect Your Phone

#### ✅ Make sure:
- [ ] Phone and computer on **SAME WiFi network**
- [ ] Both connected to WiFi (not mobile data)

#### 📱 On your phone:
1. Open browser (Chrome/Safari)
2. Type: `http://192.168.1.100:8100`
   (Replace with YOUR computer's IP)
3. Press Enter

**✨ The app should load!**

---

## 🧪 Test Login

1. **Phone Number:** `+919823458001`
2. **OTP:** `123456` (testing mode)
3. **Click:** Verify & Login

**You're in!** 🎉

---

## 📊 Visual Network Diagram

```
┌──────────────────────────┐
│   Your Computer          │
│   IP: 192.168.1.100      │
│                          │
│   ┌────────────────┐     │
│   │ Docker         │     │
│   │ - Backend:8080 │     │
│   │ - MySQL        │     │
│   │ - Redis        │     │
│   └────────────────┘     │
│                          │
│   ┌────────────────┐     │
│   │ Ionic Server   │     │
│   │ Port: 8100     │     │
│   └────────────────┘     │
└──────────┬───────────────┘
           │
           │ WiFi Network
           │ (Router)
           │
┌──────────▼───────────────┐
│   Your Phone             │
│   IP: 192.168.1.105      │
│                          │
│   📱 Browser             │
│   http://192.168.1.100   │
│        :8100             │
└──────────────────────────┘
```

---

## 🔧 Troubleshooting

### Problem: Can't access from phone

**Check 1: Same WiFi?**
```
Phone → Settings → WiFi → Check network name
Computer → Check WiFi network name
They must MATCH!
```

**Check 2: Firewall**
```
Windows: Turn off Windows Firewall temporarily
Mac: System Preferences → Firewall → Off (for testing)
```

**Check 3: Test from computer first**
```bash
curl http://localhost:8100
# Should return HTML
```

**Check 4: Ping test**
```bash
# On computer
ping 192.168.1.100

# Should reply
```

---

### Problem: Backend API not working

**Fix:**
```bash
# Check if running
docker-compose ps

# All should show "running"
# If not:
docker-compose restart

# Test API
curl http://localhost:8080/api/actuator/health
```

---

### Problem: CORS error in browser console

**Fix:**
```bash
# Edit backend config
nano backend/src/main/resources/application-testing.yaml

# Add your phone's IP to allowed origins
cors:
  allowed-origins:
    - http://localhost:8100
    - http://192.168.1.100:8100  # Computer
    - http://192.168.1.105:8100  # Phone

# Restart
docker-compose restart backend
```

---

## 📝 Quick Commands Reference

### Start Everything
```bash
# Terminal 1: Backend
docker-compose up -d

# Terminal 2: Mobile app
cd mobile-app
ionic serve --external
```

### Stop Everything
```bash
# Stop mobile (Ctrl+C in terminal)
# Stop backend
docker-compose down
```

### View Logs
```bash
docker-compose logs -f backend
```

### Check What's Running
```bash
docker-compose ps
```

---

## 🎨 What You Can Test

### ✅ Working Features:
- Login with OTP
- Browse products
- Add to cart
- Place orders
- View profile
- Search products
- View order history
- Track deliveries

### ❌ Need Real Device Build:
- Camera (requires native app)
- Push notifications (requires native app)
- GPS location (requires native app)
- Biometric login (requires native app)

For these features, you need to build the actual mobile app:
```bash
# Android
ionic build
npx cap add android
npx cap open android
# Then run from Android Studio

# iOS
ionic build
npx cap add ios
npx cap open ios
# Then run from Xcode
```

---

## 🚀 Advanced: Test on Multiple Devices

### Same Computer, Multiple Phones

**Phone 1:**
```
http://192.168.1.100:8100
Login: +919823458001
OTP: 123456
```

**Phone 2:**
```
http://192.168.1.100:8100
Login: +919823458002
OTP: 123456
```

**Tablet:**
```
http://192.168.1.100:8100
Login: +919823458003
OTP: 123456
```

All will work simultaneously! Each can test different user roles.

---

## 📸 Screenshot Guide

### 1. Computer Setup
```
[Screenshot: Terminal showing docker-compose up -d output]
[Screenshot: Terminal showing ionic serve output with External URL]
```

### 2. Phone WiFi Check
```
[Screenshot: Phone Settings → WiFi showing network name]
```

### 3. Phone Browser
```
[Screenshot: Phone browser with http://192.168.1.100:8100]
[Screenshot: App loading on phone]
```

### 4. App Running
```
[Screenshot: Login screen on phone]
[Screenshot: Product listing on phone]
[Screenshot: Cart view on phone]
```

---

## 🎓 Understanding the Setup

### Why Same WiFi?
```
Your Phone needs to "see" your Computer
Same WiFi = Same network = Can communicate
Different WiFi = Can't reach each other
```

### Why External URL?
```
localhost = Only your computer
192.168.1.100 = Your computer on network
Anyone on same WiFi can access
```

### Why Port 8100?
```
Port 8080 = Backend API
Port 8100 = Mobile app UI
Port 3306 = Database
Port 6379 = Redis
```

---

## 🔍 Testing Different User Types

### Test as Farmer
```
Phone: +919823456901
Name: Ramesh Pawar
Role: Can add products, view sales
```

### Test as Customer
```
Phone: +919823458001
Name: Neha Kulkarni
Role: Can browse, order, track
```

### Test as Retailer
```
Phone: +919823457001
Name: Sagar Enterprises
Role: Can place bulk orders
```

### Test as Admin
```
Phone: +919876543210
Name: Rajesh Kumar
Role: Can manage everything
```

All use OTP: **123456** (testing mode)

---

## 💡 Pro Tips

### 1. Live Reload
**Change code → Save → Phone auto-refreshes**
No need to reload manually!

### 2. Debug with Chrome
```
Chrome on computer → chrome://inspect
Find your device → Click "Inspect"
Debug JavaScript like a website!
```

### 3. Test Offline
```
1. Load app on phone
2. Turn off WiFi on phone
3. Test what works offline
4. Turn WiFi back on
5. App syncs changes
```

### 4. Test Different Screens
```
Portrait mode
Landscape mode
Different screen sizes
Zoom in/out
```

### 5. Performance Testing
```
Slow WiFi: Test on 2.4GHz
Fast WiFi: Test on 5GHz
Mobile data: Use phone's hotspot
```

---

## ✅ Success Checklist

- [ ] Backend running (`docker-compose ps` shows "running")
- [ ] Know computer IP address (`192.168.x.x`)
- [ ] Mobile server running (`ionic serve --external`)
- [ ] Phone on same WiFi as computer
- [ ] Can access app in phone browser
- [ ] Can login with test credentials
- [ ] Can browse products
- [ ] Can add items to cart
- [ ] Can place test order

**All checked? You're testing successfully!** 🎉

---

## 📱 Alternative: Browser Mobile View

If you don't have a phone handy:

```bash
# Start backend
docker-compose up -d

# Start mobile
cd mobile-app
ionic serve

# Open Chrome
# Press F12 (DevTools)
# Click "Toggle Device Toolbar" (phone icon)
# Select "iPhone 12 Pro" or "Pixel 5"
# Test in browser!
```

---

## 🎯 Summary

**3 Simple Steps:**
1. `docker-compose up -d` (start backend)
2. `ionic serve --external` (start mobile UI)
3. Open phone browser → `http://YOUR_IP:8100`

**Done!** Your mobile app is running on your phone! 📱✨

---

## Need Help?

**Can't find IP?**
```bash
# Windows
ipconfig | findstr IPv4

# Mac/Linux
ifconfig | grep "inet " | grep -v 127.0.0.1
```

**Port already in use?**
```bash
# Use different port
ionic serve --external --port 8101
# Then access: http://YOUR_IP:8101
```

**Still stuck?**
Check the complete guide: `MOBILE-APP-TESTING-GUIDE.md`

---

© 2026 VMV AgroMart - Simple Mobile Testing Guide
