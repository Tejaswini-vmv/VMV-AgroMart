# 📱 Mobile App Testing - ONE PAGE GUIDE

## 🚀 Test VMV AgroMart Mobile App on Your Phone in 5 Minutes

---

## Step 1️⃣: Start Backend (30 seconds)

```bash
cd vmv-agromart
docker-compose up -d
```
✅ **Done!** Backend is running.

---

## Step 2️⃣: Get Your Computer's IP (10 seconds)

**Windows:** Open Command Prompt → Type:
```cmd
ipconfig
```
Find **IPv4 Address**: `192.168.1.100` ← Your IP

**Mac/Linux:** Open Terminal → Type:
```bash
ifconfig | grep "inet " | grep -v 127.0.0.1
```
Find: `192.168.1.100` ← Your IP

**✏️ Write your IP here:** `___.___.___.___`

---

## Step 3️⃣: Start Mobile App (20 seconds)

```bash
cd mobile-app
ionic serve --external
```

**You'll see:**
```
External: http://192.168.1.100:8100  ← USE THIS!
```

---

## Step 4️⃣: Open on Your Phone (1 minute)

### ✅ Prerequisites:
- Phone and computer on **SAME WiFi** ✓
- Both devices have internet ✓

### 📱 On Your Phone:
1. Open **Chrome** or **Safari** browser
2. Type in address bar:
   ```
   http://192.168.1.100:8100
   ```
   (Replace `192.168.1.100` with YOUR computer's IP)
3. Press **Go** / **Enter**

### 🎉 App Should Load!

---

## Step 5️⃣: Test Login (30 seconds)

**On phone screen:**

1. **Enter Phone:** `+919823458001`
2. **Tap:** "Send OTP"
3. **Enter OTP:** `123456`
4. **Tap:** "Verify"

**✨ You're logged in!**

---

## 🎯 Complete Visual Flow

```
┌─────────────────────────┐
│  YOUR COMPUTER          │
│  192.168.1.100          │
│                         │
│  ┌─────────────┐        │
│  │ Docker      │        │
│  │ Backend:8080│        │
│  └─────────────┘        │
│                         │
│  ┌─────────────┐        │
│  │ Ionic :8100 │        │
│  └─────────────┘        │
└────────┬────────────────┘
         │
         │ WiFi Router
         │ (Same Network)
         │
┌────────▼────────────────┐
│  YOUR PHONE             │
│  192.168.1.105          │
│                         │
│  📱 Browser             │
│  192.168.1.100:8100     │
└─────────────────────────┘
```

---

## ✅ Verification Checklist

| Check | Command | Expected Result |
|-------|---------|-----------------|
| Backend Running | `docker-compose ps` | All "running" |
| API Works | `curl localhost:8080/api/actuator/health` | `{"status":"UP"}` |
| Same WiFi | Phone Settings → WiFi | Same network name |
| Can Access | Phone Browser | App loads |

---

## ⚠️ Quick Troubleshooting

### Problem: Can't access from phone

**Solution:**
```bash
# 1. Check same WiFi
# Phone Settings → WiFi (check name)
# Computer → WiFi settings (check name)

# 2. Turn off firewall (temporarily)
# Windows: Settings → Firewall → Off
# Mac: System Preferences → Firewall → Off

# 3. Restart services
docker-compose restart
```

### Problem: Page not loading

**Solution:**
```bash
# Clear and restart
docker-compose down
docker-compose up -d
cd mobile-app
ionic serve --external --disableHostCheck
```

---

## 🧪 What to Test

| Feature | Test Steps | Expected |
|---------|-----------|----------|
| **Login** | Enter phone + OTP | ✅ Dashboard opens |
| **Browse** | Tap "Products" | ✅ Product list shows |
| **Search** | Type "tomato" | ✅ Tomato products appear |
| **Cart** | Add item → Cart icon | ✅ Shows (1) badge |
| **Order** | Checkout → Place Order | ✅ Order confirmation |

---

## 📊 Test Accounts (All use OTP: 123456)

| Role | Phone | Name |
|------|-------|------|
| **Customer** | +919823458001 | Neha Kulkarni |
| **Farmer** | +919823456901 | Ramesh Pawar |
| **Retailer** | +919823457001 | Sagar Enterprises |
| **Admin** | +919876543210 | Rajesh Kumar |

---

## 🎓 Pro Tips

### Live Reload
Change code → Save → Phone auto-refreshes! ✨

### Debug on Computer
```
Chrome → chrome://inspect
→ Find device → Inspect
```

### Test Different Screens
- Rotate phone (portrait/landscape)
- Try on tablet
- Test on different phones

---

## 🔄 Quick Commands

### Start Everything
```bash
docker-compose up -d && cd mobile-app && ionic serve --external
```

### Stop Everything
```bash
# Press Ctrl+C (stops Ionic)
docker-compose down  # (stops Docker)
```

### View Logs
```bash
docker-compose logs -f backend
```

---

## 📱 Alternative: Browser Testing

No phone? Test in Chrome:

```bash
# 1. Start services
docker-compose up -d
cd mobile-app
ionic serve

# 2. Open Chrome → http://localhost:8100
# 3. Press F12 (DevTools)
# 4. Click phone icon (Toggle Device Toolbar)
# 5. Select "iPhone 12 Pro"
```

✨ Test in browser mobile view!

---

## 🎯 Success Criteria

- ✅ Can access app from phone
- ✅ Login works with test OTP
- ✅ Products load correctly
- ✅ Can add items to cart
- ✅ Navigation works smoothly
- ✅ Images display properly

**All checked?** You're successfully testing! 🎉

---

## 📞 Need More Help?

- **Full Guide:** `MOBILE-APP-TESTING-GUIDE.md`
- **All Credentials:** `TEST-CREDENTIALS.md`
- **User Manual:** `COMPLETE-USER-MANUAL.md`

---

## 💡 Common Issues & Fixes

| Error | Fix |
|-------|-----|
| "Connection refused" | Check same WiFi + firewall |
| "CORS error" | Add phone IP to backend config |
| "Page not found" | Verify IP address is correct |
| "OTP failed" | Check backend logs |

---

## 🔑 The 3 Most Important Things

1. **Same WiFi Network** (Phone + Computer)
2. **Use Computer's IP** (Not localhost)
3. **Port 8100** (Mobile UI) + **8080** (API)

---

**That's it!** Follow these 5 steps and you'll have the mobile app running on your phone! 📱✨

© 2026 VMV AgroMart
