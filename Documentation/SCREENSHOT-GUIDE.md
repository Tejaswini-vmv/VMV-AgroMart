# 📸 How to Add Screenshots to Your User Manual PDF

## Overview

I've created **HTML mockups** of key app screens that you can open in a browser, screenshot, and add to your user manual PDF.

---

## 🎯 Quick Process (3 Steps)

### Step 1: Open Mockup in Browser
```bash
# Navigate to mockups folder
cd mockups

# Open any mockup in browser
# Example: Double-click 01-login-screen.html
```

### Step 2: Take Screenshot
- **Windows:** Press `Windows + Shift + S`
- **Mac:** Press `Cmd + Shift + 4`
- **Linux:** Press `Print Screen`

### Step 3: Save Screenshot
- Save as PNG or JPG
- Name it clearly (e.g., `login-screen.png`)

---

## 📱 Available Mockups

I've created these screens for you:

| File | Screen | Description |
|------|--------|-------------|
| `01-login-screen.html` | Login | Phone number entry |
| `02-otp-verification.html` | OTP | OTP verification screen |
| `03-product-listing.html` | Products | Product catalog view |

---

## 🖼️ Method 1: Using HTML Mockups (Recommended)

### Step-by-Step:

#### 1. Open Mockup
```bash
cd mockups
# Windows
start 01-login-screen.html
# Mac
open 01-login-screen.html
# Linux
xdg-open 01-login-screen.html
```

#### 2. Take Screenshot

**On Windows:**
```
1. Press: Windows + Shift + S
2. Drag to select the phone frame
3. Screenshot copied to clipboard
4. Open Paint/Word
5. Paste (Ctrl+V)
6. Save as PNG
```

**On Mac:**
```
1. Press: Cmd + Shift + 4
2. Drag to select the phone frame
3. Screenshot saved to Desktop
4. Rename file
```

**On Linux:**
```
1. Press: Print Screen
2. Select area
3. Save screenshot
```

#### 3. Insert into PDF

**Option A: During PDF Generation**

Edit the markdown file to include actual images:

```markdown
Before:
[Screenshot: Login screen]

After:
![Login Screen](screenshots/login-screen.png)
```

Then regenerate PDF:
```bash
./generate-pdf.sh
```

**Option B: Edit Existing PDF**

Use PDF editor tools:
- Adobe Acrobat
- PDF-XChange Editor
- PDFescape (online)

1. Open PDF
2. Click "Add Image"
3. Select screenshot
4. Position where placeholder is
5. Save PDF

---

## 📸 Method 2: Screenshot Real Running App

### From Running Mobile App:

```bash
# 1. Start the app
docker-compose up -d
cd mobile-app
ionic serve --external

# 2. Open on phone: http://YOUR_IP:8100

# 3. On Phone:
# - Navigate to screen
# - Take screenshot (Power + Volume Down)
# - Transfer to computer
# - Add to PDF
```

### From Browser (Chrome DevTools):

```bash
# 1. Start app
ionic serve

# 2. Open Chrome: http://localhost:8100

# 3. Open DevTools (F12)

# 4. Toggle Device Toolbar (Ctrl+Shift+M)

# 5. Select device: iPhone 12 Pro

# 6. Take screenshot:
Chrome DevTools → Three dots (⋮) → Capture screenshot
```

---

## 🎨 Method 3: Create Custom Screenshots

### Using Browser Screenshot Extensions:

**Recommended Extensions:**
1. **Awesome Screenshot** (Chrome/Firefox)
2. **Nimbus Screenshot** (Chrome/Firefox)
3. **Fireshot** (Chrome/Firefox)

**Steps:**
1. Install extension
2. Open mockup HTML
3. Click extension icon
4. Select "Capture Visible Part"
5. Download image

---

## 📝 Adding Screenshots to Markdown

### Find Placeholder Text

In `COMPLETE-USER-MANUAL.md`, find lines like:
```markdown
![Login Screen]
[Screenshot: Login screen]
**[SCREENSHOT: OTP Verification]**
```

### Replace with Image:

```markdown
![Login Screen](screenshots/01-login-screen.png)
```

### Image Syntax:
```markdown
![Alt Text](path/to/image.png)
```

### Example:

**Before:**
```markdown
**Step 2: Enter Mobile Number**
![Mobile Number Entry]

1. Open the application
2. Click "Login" or "Get Started"
```

**After:**
```markdown
**Step 2: Enter Mobile Number**
![Mobile Number Entry](screenshots/login-screen.png)

1. Open the application
2. Click "Login" or "Get Started"
```

---

## 📁 Organize Your Screenshots

### Recommended Structure:

```
vmv-agromart/
├── COMPLETE-USER-MANUAL.md
├── screenshots/
│   ├── 01-login-screen.png
│   ├── 02-otp-verification.png
│   ├── 03-product-listing.png
│   ├── 04-shopping-cart.png
│   ├── 05-checkout.png
│   ├── 06-order-tracking.png
│   └── 07-profile.png
└── mockups/
    ├── 01-login-screen.html
    ├── 02-otp-verification.html
    └── 03-product-listing.html
```

### Create Screenshots Folder:
```bash
mkdir -p screenshots
```

---

## 🔄 Complete Workflow

### Full Process:

```bash
# 1. Create screenshots folder
mkdir screenshots

# 2. Open first mockup
open mockups/01-login-screen.html

# 3. Take screenshot
# (Use Windows+Shift+S or Cmd+Shift+4)

# 4. Save to screenshots folder
# Name: 01-login-screen.png

# 5. Repeat for all mockups

# 6. Edit markdown file
nano COMPLETE-USER-MANUAL.md

# 7. Replace placeholders with:
# ![Description](screenshots/filename.png)

# 8. Generate PDF
./generate-pdf.sh

# 9. Check output
open pdfs/VMV-AgroMart-User-Manual.pdf
```

---

## 🎯 Screenshot Checklist

### Essential Screenshots Needed:

#### Login Flow:
- [ ] Login screen (mockup available)
- [ ] OTP verification (mockup available)
- [ ] Welcome dashboard

#### Shopping:
- [ ] Product listing (mockup available)
- [ ] Product details
- [ ] Shopping cart
- [ ] Checkout form

#### Orders:
- [ ] Order confirmation
- [ ] Order tracking
- [ ] Delivery status

#### User Profile:
- [ ] Profile view
- [ ] Edit profile
- [ ] Order history

---

## 💡 Tips for Better Screenshots

### Quality Tips:

1. **Use High Resolution**
   - Minimum: 1080p
   - Recommended: 2K or higher

2. **Clean Browser**
   - Hide bookmarks bar
   - Close unnecessary tabs
   - Use Incognito mode (no extensions)

3. **Consistent Size**
   - Use same device/viewport for all screenshots
   - Mockups already have consistent 375x812 frame

4. **Good Lighting**
   - Use light background for phone mockups
   - Ensure text is readable

5. **No Personal Data**
   - Use test accounts
   - Use dummy data only

---

## 🛠️ Tools for Screenshot Editing

### Free Tools:

**For Basic Editing:**
1. **Windows:** Paint, Snip & Sketch
2. **Mac:** Preview
3. **Linux:** GIMP, Shutter

**For Advanced Editing:**
1. **GIMP** (Free, all platforms)
2. **Paint.NET** (Windows)
3. **Pixlr** (Online, free)

### What to Edit:

- Crop to exact size
- Add arrows/annotations (optional)
- Blur sensitive info
- Adjust brightness/contrast
- Save as PNG for quality

---

## 📊 Batch Screenshot Process

### For Multiple Screens:

```bash
# 1. Open all mockups in tabs
open mockups/*.html

# 2. Use screenshot tool in batch mode
# (Many tools support multi-capture)

# 3. Rename files sequentially
mv Screenshot1.png screenshots/01-login.png
mv Screenshot2.png screenshots/02-otp.png
mv Screenshot3.png screenshots/03-products.png

# 4. Batch update markdown
# Use find-and-replace in text editor
```

---

## 🎨 Alternative: Use Design Tools

### Create Professional Mockups:

**Figma (Free):**
1. Import screenshots
2. Add device frames
3. Export high-quality images

**Canva (Free):**
1. Upload screenshots
2. Use phone mockup template
3. Download as PNG

**MockUPhone (Online, Free):**
1. Go to: https://mockuphone.com
2. Upload screenshot
3. Select device frame
4. Download mockup

---

## ✅ Verification

### Check Your Screenshots:

```bash
# List all screenshots
ls -lh screenshots/

# Should see:
# 01-login-screen.png
# 02-otp-verification.png
# 03-product-listing.png
# etc.
```

### Test in PDF:

```bash
# Generate PDF with screenshots
./generate-pdf.sh

# Open and verify
open pdfs/VMV-AgroMart-User-Manual.pdf

# Check:
# ✓ Images appear
# ✓ Images are clear
# ✓ Images are correct size
# ✓ No broken links
```

---

## 🚀 Quick Start Commands

### Complete Screenshot Workflow:

```bash
# 1. Setup
mkdir screenshots

# 2. Open mockup
open mockups/01-login-screen.html

# 3. Screenshot (varies by OS)
# Windows: Win+Shift+S
# Mac: Cmd+Shift+4
# Linux: Print Screen

# 4. Save to screenshots/

# 5. Update markdown
# Replace: [Screenshot: Login]
# With: ![Login](screenshots/01-login.png)

# 6. Generate PDF
./generate-pdf.sh

# 7. View result
open pdfs/VMV-AgroMart-User-Manual.pdf
```

---

## 📱 Real App Screenshots (Alternative)

If you want REAL screenshots from the actual running app:

### Method 1: From Phone
```bash
# 1. Start backend
docker-compose up -d

# 2. Start mobile
cd mobile-app
ionic serve --external

# 3. Access from phone
# http://YOUR_IP:8100

# 4. Navigate and screenshot
# Power + Volume Down (Android)
# Power + Volume Up (iPhone)

# 5. Transfer to computer
# Email/AirDrop/USB

# 6. Add to screenshots/ folder
```

### Method 2: From Chrome DevTools
```bash
# 1. Start app
ionic serve

# 2. Open Chrome
# http://localhost:8100

# 3. DevTools (F12)
# Toggle Device Toolbar

# 4. Capture screenshot
# DevTools → ⋮ → Capture screenshot

# 5. Save to screenshots/
```

---

## 🎯 Final Checklist

Before generating final PDF:

- [ ] All mockup HTMLs opened and screenshotted
- [ ] Screenshots saved in `screenshots/` folder
- [ ] Markdown file updated with image paths
- [ ] Image paths are correct (relative paths)
- [ ] Screenshots are clear and readable
- [ ] No personal/sensitive data in screenshots
- [ ] All placeholder text replaced
- [ ] PDF generated successfully
- [ ] PDF reviewed for quality

---

## 💾 Example: Complete Update

### Before (in markdown):
```markdown
## 4.2 Step-by-Step Login

**Step 1: Open Application**
[Screenshot: Landing page]

**Step 2: Enter Mobile Number**
[Screenshot: Mobile number entry]

**Step 3: Verify OTP**
[Screenshot: OTP verification]
```

### After (with screenshots):
```markdown
## 4.2 Step-by-Step Login

**Step 1: Open Application**
![Landing Page](screenshots/01-login-screen.png)

**Step 2: Enter Mobile Number**
![Mobile Number Entry](screenshots/01-login-screen.png)

**Step 3: Verify OTP**
![OTP Verification](screenshots/02-otp-verification.png)
```

### Generate PDF:
```bash
./generate-pdf.sh
```

### Result:
PDF now includes actual images instead of placeholder text!

---

## 🎉 You're Done!

Your user manual PDF will now have professional screenshots showing the actual app interface!

**Questions?**
- Check mockups folder for available screens
- All mockups are mobile-responsive
- Can be customized by editing HTML
- Open in any browser to screenshot

---

© 2026 VMV AgroMart - Screenshot Guide
