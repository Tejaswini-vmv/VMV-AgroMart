# VMV AgroMart - Complete DIY Deployment Guide
## Oracle Cloud Free Tier - Step-by-Step with Copy-Paste Commands

**Total Time: 8-10 hours over 2-3 days**
**Total Cost: ₹19,200/year**
**Difficulty: Easy (I'm with you at every step!)**

---

## 📋 BEFORE YOU START - CHECKLIST

### ✅ What You Need:

**Required Accounts (Create if you don't have):**
- [ ] Email address (Gmail works fine)
- [ ] Phone number (for verification)
- [ ] Credit/Debit card (for ID verification only, won't be charged)

**Services to Sign Up (Free/Paid):**
- [ ] Oracle Cloud Account (We'll create together)
- [ ] Domain name (₹1,000 - from GoDaddy/Namecheap)
- [ ] Twilio account (SMS - ₹250/month for 100 SMS)
- [ ] Razorpay account (Payments - signup at razorpay.com)
- [ ] Google Cloud Platform (Maps API - ₹400/month)

**On Your Computer:**
- [ ] Windows 10/11, Mac, or Linux
- [ ] Internet connection (stable)
- [ ] Web browser (Chrome/Firefox)
- [ ] Text editor (Notepad++, VS Code, or even Notepad)

**Optional (for mobile apps):**
- [ ] Android Studio (for Android APK)
- [ ] Xcode (for iOS IPA - Mac only)
- [ ] Google Play Developer account (₹2,100)
- [ ] Apple Developer account (₹8,300/year)

---

## 🎯 DEPLOYMENT ROADMAP

```
DAY 1 (4-5 hours):
├─ Step 1: Oracle Cloud Setup (1 hour)
├─ Step 2: Create Server Instance (30 min)
├─ Step 3: Connect to Server (30 min)
├─ Step 4: Install Docker (30 min)
├─ Step 5: Deploy Application (1.5 hours)
└─ Step 6: Configure Domain (30 min)

DAY 2 (3-4 hours):
├─ Step 7: Setup SSL Certificate (30 min)
├─ Step 8: Test Everything (1 hour)
├─ Step 9: Setup Database (1 hour)
└─ Step 10: Final Testing (1 hour)

DAY 3 (3-4 hours):
├─ Step 11: Build Android App (2 hours)
├─ Step 12: Build iOS App (2 hours - optional)
└─ Step 13: Submit to Stores (1 hour)

✅ DONE! Your app is live!
```

---

## 📅 DAY 1: SERVER SETUP & DEPLOYMENT

---

### STEP 1: CREATE ORACLE CLOUD ACCOUNT (1 hour)

**Time: 1 hour**
**Difficulty: Very Easy**

#### 1.1 Sign Up

```
1. Open browser, go to: https://www.oracle.com/cloud/free/

2. Click "Start for free" (big button)

3. Fill the form:
   ├─ Country: India
   ├─ Name: Your name
   ├─ Email: Your email
   └─ Password: Create strong password (save it!)

4. Click "Verify my email"

5. Check your email, click verification link

6. Complete additional information:
   ├─ Company name: Can use your name
   ├─ Cloud account name: Choose unique name (e.g., vmvagromart)
   │  (This becomes: vmvagromart.cloud.oracle.com)
   └─ Home region: India West (Mumbai) or India South (Hyderabad)
      ⚠️ IMPORTANT: Cannot change later!

7. Click "Continue"

8. Payment verification:
   ├─ Enter credit/debit card details
   │  ⚠️ This is for verification only
   │  ⚠️ They will charge ₹2 temporarily (refunded)
   │  ⚠️ You won't be charged for free tier services!
   └─ Complete verification

9. Wait 2-5 minutes for account setup

10. You'll receive email: "Your Oracle Cloud Account is Ready"

✅ Done! You now have Oracle Cloud account
```

**Troubleshooting:**
- If card declined: Try different card or use virtual card (e.g., PayTM)
- If email not received: Check spam folder
- If region selection unclear: Choose "India West (Mumbai)"

---

### STEP 2: CREATE SERVER INSTANCE (30 min)

**Time: 30 minutes**
**Difficulty: Easy**

#### 2.1 Login to Oracle Cloud

```
1. Go to: https://cloud.oracle.com

2. Click "Sign in to Cloud"

3. Enter your Cloud Account Name (from Step 1.6)

4. Click "Continue"

5. Enter email and password

6. Click "Sign In"

✅ You should see Oracle Cloud Console dashboard
```

#### 2.2 Create Compute Instance

```
1. In the Oracle Cloud Console:
   Click ≡ (hamburger menu, top-left)

2. Navigate to:
   Compute → Instances

3. Click "Create Instance" (blue button)

4. Configure instance:

   Name: vmv-agromart-prod

   Compartment: (root) - leave default

   Placement: Leave default

   Image and Shape:
   ├─ Click "Change Image"
   ├─ Select: Canonical Ubuntu 22.04
   ├─ Click "Select Image"
   │
   └─ Click "Change Shape"
      ├─ Select: Ampere (ARM-based)
      ├─ Select: VM.Standard.A1.Flex
      ├─ Number of OCPUs: 4
      ├─ Amount of memory (GB): 24
      └─ Click "Select Shape"

   Networking:
   ├─ Create new virtual cloud network: (checked)
   ├─ VCN name: vmv-agromart-vcn
   ├─ Subnet name: vmv-agromart-subnet
   └─ Assign a public IPv4 address: (checked) ✅ IMPORTANT!

   Add SSH Keys:
   ├─ Select: "Generate a key pair for me"
   ├─ Click "Save Private Key" → Save to Downloads
   │  File: ssh-key-YYYY-MM-DD.key
   │  ⚠️ IMPORTANT: Keep this file safe!
   └─ Click "Save Public Key" → Save to Downloads (optional)

   Boot volume:
   └─ Leave default (50 GB)

5. Click "Create" (blue button at bottom)

6. Wait 2-3 minutes...

7. Status changes: Provisioning → Running ✅

8. ⚠️ IMPORTANT: Note down your Public IP address
   (Shows on instance details page)

   Example: 132.145.xxx.xxx

   Write it here: ________________
```

**⚠️ IMPORTANT FILES:**
- SSH Private Key: `ssh-key-YYYY-MM-DD.key` (in Downloads)
- Public IP Address: Write it down!

---

### STEP 3: CONFIGURE FIREWALL (15 min)

**Time: 15 minutes**
**Difficulty: Easy**

```
1. On the instance details page, find:
   "Primary VNIC" section

2. Click on the subnet link:
   (something like: vmv-agromart-subnet)

3. Under "Security Lists", click:
   "Default Security List for vmv-agromart-vcn"

4. Click "Add Ingress Rules" (blue button)

5. Add Rule #1 (HTTP):
   ├─ Source CIDR: 0.0.0.0/0
   ├─ IP Protocol: TCP
   ├─ Destination Port Range: 80
   ├─ Description: HTTP
   └─ Click "Add Ingress Rules"

6. Click "Add Ingress Rules" again

7. Add Rule #2 (HTTPS):
   ├─ Source CIDR: 0.0.0.0/0
   ├─ IP Protocol: TCP
   ├─ Destination Port Range: 443
   ├─ Description: HTTPS
   └─ Click "Add Ingress Rules"

8. Click "Add Ingress Rules" again

9. Add Rule #3 (Backend API):
   ├─ Source CIDR: 0.0.0.0/0
   ├─ IP Protocol: TCP
   ├─ Destination Port Range: 8080
   ├─ Description: Backend API
   └─ Click "Add Ingress Rules"

✅ Done! Firewall configured
```

---

### STEP 4: CONNECT TO SERVER (30 min)

**Time: 30 minutes**
**Difficulty: Medium**

#### 4.1 Prepare SSH Key (Windows)

**If you're on Windows:**

```
1. Open File Explorer
2. Navigate to Downloads folder
3. Find: ssh-key-YYYY-MM-DD.key
4. Right-click → Properties
5. Note the full path (e.g., C:\Users\YourName\Downloads\ssh-key-YYYY-MM-DD.key)

6. Open Command Prompt:
   ├─ Press Windows + R
   ├─ Type: cmd
   └─ Press Enter

7. Move SSH key to better location:

   mkdir %USERPROFILE%\.ssh
   move "%USERPROFILE%\Downloads\ssh-key-*.key" "%USERPROFILE%\.ssh\oracle-key.key"

8. Set permissions (using Git Bash or WSL):

   chmod 400 ~/.ssh/oracle-key.key

   (If you don't have Git Bash, skip this - we'll use PuTTY instead)
```

**Install PuTTY (Easier for Windows):**

```
1. Download PuTTY:
   https://www.putty.org/

2. Install PuTTY (Next → Next → Install)

3. Download PuTTYgen (comes with PuTTY)

4. Open PuTTYgen

5. Click "Load"

6. Navigate to your SSH key:
   C:\Users\YourName\.ssh\oracle-key.key

7. Change file filter to "All Files (*.*)"

8. Select your key file

9. Click "Save private key"
   ├─ Yes to "without passphrase"
   └─ Save as: oracle-key.ppk

✅ Now you have: oracle-key.ppk (PuTTY format)
```

#### 4.2 Prepare SSH Key (Mac/Linux)

**If you're on Mac or Linux:**

```bash
# Open Terminal

# Move key to .ssh folder
mkdir -p ~/.ssh
mv ~/Downloads/ssh-key-*.key ~/.ssh/oracle-key.key

# Set correct permissions
chmod 400 ~/.ssh/oracle-key.key

✅ Done!
```

#### 4.3 Connect to Server

**Windows (using PuTTY):**

```
1. Open PuTTY

2. In "Host Name" field, enter:
   ubuntu@YOUR_PUBLIC_IP

   Example: ubuntu@132.145.xxx.xxx

3. Port: 22 (default)

4. Connection type: SSH (selected)

5. On left menu:
   ├─ Expand "SSH"
   ├─ Click "Auth"
   └─ Click "Credentials"

6. Under "Private key file for authentication":
   ├─ Click "Browse"
   └─ Select: oracle-key.ppk

7. Go back to "Session" (top of left menu)

8. Under "Saved Sessions":
   ├─ Type: Oracle-VMV
   └─ Click "Save"

9. Click "Open" (bottom)

10. First time connecting:
    ├─ You'll see security alert
    └─ Click "Accept" or "Yes"

11. You should see:
    ubuntu@vmv-agromart-prod:~$

✅ Connected!
```

**Mac/Linux (using Terminal):**

```bash
# Open Terminal

# Connect to server
ssh -i ~/.ssh/oracle-key.key ubuntu@YOUR_PUBLIC_IP

# Replace YOUR_PUBLIC_IP with your actual IP
# Example: ssh -i ~/.ssh/oracle-key.key ubuntu@132.145.xxx.xxx

# First time: Type "yes" when asked about fingerprint

# You should see:
# ubuntu@vmv-agromart-prod:~$

✅ Connected!
```

**Troubleshooting:**
- "Permission denied": Check SSH key path and permissions
- "Connection refused": Check if instance is running
- "Connection timeout": Check firewall rules
- "Host key verification failed": Delete ~/.ssh/known_hosts and try again

---

### STEP 5: INSTALL DOCKER (30 min)

**Time: 30 minutes**
**Difficulty: Easy (Copy-Paste Commands)**

**⚠️ You should be connected to your Oracle server via SSH**

```bash
# Copy and paste each command, press Enter, wait for completion

# 1. Update system
sudo apt update
sudo apt upgrade -y

# This takes 5-10 minutes... wait for it to complete
# You'll see: "ubuntu@vmv-agromart-prod:~$" when done

# 2. Install Docker
curl -fsSL https://get.docker.com -o get-docker.sh
sudo sh get-docker.sh

# This takes 2-3 minutes...

# 3. Add user to docker group
sudo usermod -aG docker ubuntu

# 4. Install Docker Compose
sudo curl -L "https://github.com/docker/compose/releases/download/v2.24.0/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
sudo chmod +x /usr/local/bin/docker-compose

# 5. Install Git
sudo apt install -y git

# 6. Logout and login again (to apply docker group)
exit

# 7. Connect again (same as Step 4.3)
# Windows: Open PuTTY, select "Oracle-VMV", click "Open"
# Mac/Linux: ssh -i ~/.ssh/oracle-key.key ubuntu@YOUR_PUBLIC_IP

# 8. Verify Docker installation
docker --version
docker-compose --version

# You should see:
# Docker version 24.x.x
# Docker Compose version v2.24.0

✅ Docker installed successfully!
```

---

### STEP 6: DEPLOY APPLICATION (1.5 hours)

**Time: 1.5 hours**
**Difficulty: Medium (But I've prepared everything)**

#### 6.1 Prepare Application Files

**On your Oracle server (via SSH):**

```bash
# Create working directory
cd /opt
sudo mkdir vmv-agromart
sudo chown ubuntu:ubuntu vmv-agromart
cd vmv-agromart

# Create directory structure
mkdir -p backend frontend database nginx

✅ Directories created
```

#### 6.2 Create Docker Compose File

```bash
# Create docker-compose.yml
nano docker-compose.yml

# Copy and paste this ENTIRE content:
# (Press Ctrl+Shift+V to paste in terminal)
```

```yaml
version: '3.8'

services:
  # MySQL Database
  mysql:
    image: mysql:8.0
    container_name: vmv-mysql
    restart: always
    environment:
      MYSQL_ROOT_PASSWORD: ${DB_ROOT_PASSWORD}
      MYSQL_DATABASE: vmv_agromart
      MYSQL_USER: agromart_user
      MYSQL_PASSWORD: ${DB_PASSWORD}
    ports:
      - "3306:3306"
    volumes:
      - mysql-data:/var/lib/mysql
      - ./database:/docker-entrypoint-initdb.d
    networks:
      - vmv-network
    healthcheck:
      test: ["CMD", "mysqladmin", "ping", "-h", "localhost"]
      interval: 10s
      timeout: 5s
      retries: 5

  # Redis Cache
  redis:
    image: redis:7-alpine
    container_name: vmv-redis
    restart: always
    command: redis-server --appendonly yes --requirepass ${REDIS_PASSWORD}
    ports:
      - "6379:6379"
    volumes:
      - redis-data:/data
    networks:
      - vmv-network
    healthcheck:
      test: ["CMD", "redis-cli", "ping"]
      interval: 10s
      timeout: 5s
      retries: 5

  # Backend API
  backend:
    image: YOUR_DOCKER_USERNAME/vmv-backend:latest
    container_name: vmv-backend
    restart: always
    depends_on:
      mysql:
        condition: service_healthy
      redis:
        condition: service_healthy
    environment:
      SPRING_PROFILES_ACTIVE: production
      DB_HOST: mysql
      DB_PORT: 3306
      DB_NAME: vmv_agromart
      DB_USERNAME: agromart_user
      DB_PASSWORD: ${DB_PASSWORD}
      REDIS_HOST: redis
      REDIS_PORT: 6379
      REDIS_PASSWORD: ${REDIS_PASSWORD}
      JWT_SECRET: ${JWT_SECRET}
      TWILIO_ACCOUNT_SID: ${TWILIO_SID}
      TWILIO_AUTH_TOKEN: ${TWILIO_TOKEN}
      TWILIO_PHONE_NUMBER: ${TWILIO_PHONE}
      RAZORPAY_KEY_ID: ${RAZORPAY_KEY}
      RAZORPAY_KEY_SECRET: ${RAZORPAY_SECRET}
      GOOGLE_MAPS_API_KEY: ${GOOGLE_MAPS_KEY}
    ports:
      - "8080:8080"
    networks:
      - vmv-network
    healthcheck:
      test: ["CMD", "curl", "-f", "http://localhost:8080/api/actuator/health"]
      interval: 30s
      timeout: 10s
      retries: 3

  # Frontend
  frontend:
    image: YOUR_DOCKER_USERNAME/vmv-frontend:latest
    container_name: vmv-frontend
    restart: always
    depends_on:
      - backend
    ports:
      - "4200:80"
    networks:
      - vmv-network

  # Nginx Reverse Proxy
  nginx:
    image: nginx:alpine
    container_name: vmv-nginx
    restart: always
    depends_on:
      - backend
      - frontend
    ports:
      - "80:80"
      - "443:443"
    volumes:
      - ./nginx/nginx.conf:/etc/nginx/nginx.conf:ro
      - ./nginx/ssl:/etc/nginx/ssl:ro
      - certbot-data:/var/www/certbot:ro
      - certbot-conf:/etc/letsencrypt:ro
    networks:
      - vmv-network

  # Certbot for SSL
  certbot:
    image: certbot/certbot
    container_name: vmv-certbot
    volumes:
      - certbot-data:/var/www/certbot
      - certbot-conf:/etc/letsencrypt
    entrypoint: "/bin/sh -c 'trap exit TERM; while :; do certbot renew; sleep 12h & wait $${!}; done;'"

networks:
  vmv-network:
    driver: bridge

volumes:
  mysql-data:
  redis-data:
  certbot-data:
  certbot-conf:
```

```bash
# Save file:
# Press Ctrl+X
# Press Y
# Press Enter

✅ docker-compose.yml created
```

#### 6.3 Create Environment File

```bash
# Create .env file
nano .env

# Copy and paste this:
```

```bash
# Database Credentials
DB_ROOT_PASSWORD=YourStrongRootPassword123!
DB_PASSWORD=YourStrongDBPassword123!

# Redis
REDIS_PASSWORD=YourStrongRedisPassword123!

# JWT Secret (generate random string)
JWT_SECRET=YourSuperSecretJWTKeyThatIsVeryLong123456789!

# Twilio (SMS)
TWILIO_SID=your_twilio_account_sid
TWILIO_TOKEN=your_twilio_auth_token
TWILIO_PHONE=+1234567890

# Razorpay (Payments)
RAZORPAY_KEY=your_razorpay_key_id
RAZORPAY_SECRET=your_razorpay_key_secret

# Google Maps
GOOGLE_MAPS_KEY=your_google_maps_api_key
```

```bash
# Save file:
# Ctrl+X → Y → Enter

⚠️ IMPORTANT: Replace all "your_..." values with real credentials
# We'll get these in next steps

✅ .env file created
```

#### 6.4 Create Nginx Configuration

```bash
# Create nginx directory and file
mkdir -p nginx
nano nginx/nginx.conf

# Copy and paste this:
```

```nginx
events {
    worker_connections 1024;
}

http {
    upstream backend {
        server backend:8080;
    }

    upstream frontend {
        server frontend:80;
    }

    # Redirect HTTP to HTTPS
    server {
        listen 80;
        server_name agromart.com www.agromart.com api.agromart.com;

        location /.well-known/acm e-challenge/ {
            root /var/www/certbot;
        }

        location / {
            return 301 https://$host$request_uri;
        }
    }

    # Frontend (Web App)
    server {
        listen 443 ssl http2;
        server_name agromart.com www.agromart.com;

        ssl_certificate /etc/letsencrypt/live/agromart.com/fullchain.pem;
        ssl_certificate_key /etc/letsencrypt/live/agromart.com/privkey.pem;

        location / {
            proxy_pass http://frontend;
            proxy_set_header Host $host;
            proxy_set_header X-Real-IP $remote_addr;
        }
    }

    # Backend API
    server {
        listen 443 ssl http2;
        server_name api.agromart.com;

        ssl_certificate /etc/letsencrypt/live/agromart.com/fullchain.pem;
        ssl_certificate_key /etc/letsencrypt/live/agromart.com/privkey.pem;

        location / {
            proxy_pass http://backend;
            proxy_set_header Host $host;
            proxy_set_header X-Real-IP $remote_addr;
            proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
            proxy_set_header X-Forwarded-Proto $scheme;
        }
    }
}
```

```bash
# Save: Ctrl+X → Y → Enter

✅ Nginx config created
```

---

### STEP 7: CONFIGURE DOMAIN (30 min)

**Time: 30 minutes**
**Difficulty: Easy**

#### 7.1 Point Domain to Server

**You need to do this in your domain registrar (GoDaddy/Namecheap/etc.):**

```
1. Login to your domain registrar

2. Go to DNS Management / DNS Settings

3. Add these A Records:

   Record 1:
   ├─ Type: A
   ├─ Name: @ (or leave blank)
   ├─ Value: YOUR_ORACLE_PUBLIC_IP
   └─ TTL: 600 (or default)

   Record 2:
   ├─ Type: A
   ├─ Name: www
   ├─ Value: YOUR_ORACLE_PUBLIC_IP
   └─ TTL: 600

   Record 3:
   ├─ Type: A
   ├─ Name: api
   ├─ Value: YOUR_ORACLE_PUBLIC_IP
   └─ TTL: 600

4. Save changes

5. Wait 5-10 minutes for DNS propagation

6. Test: Open Command Prompt/Terminal

   ping agromart.com
   ping api.agromart.com

   Should return your Oracle IP

✅ Domain configured!
```

---

### STEP 8: SETUP SSL CERTIFICATE (30 min)

**Time: 30 minutes**
**Difficulty: Medium**

**On your Oracle server:**

```bash
# 1. First, let's get SSL certificates manually
cd /opt/vmv-agromart

# 2. Stop nginx if running
docker-compose stop nginx || true

# 3. Install certbot on host
sudo snap install --classic certbot
sudo ln -s /snap/bin/certbot /usr/bin/certbot

# 4. Get SSL certificate
sudo certbot certonly --standalone \
  -d agromart.com \
  -d www.agromart.com \
  -d api.agromart.com \
  --non-interactive \
  --agree-tos \
  --email your-email@example.com

# Replace your-email@example.com with your real email

# You should see:
# Successfully received certificate.

# 5. Copy certificates to nginx directory
sudo mkdir -p nginx/ssl
sudo cp /etc/letsencrypt/live/agromart.com/fullchain.pem nginx/ssl/
sudo cp /etc/letsencrypt/live/agromart.com/privkey.pem nginx/ssl/
sudo chown -R ubuntu:ubuntu nginx/ssl

# 6. Update nginx config to use these certificates
nano nginx/nginx.conf

# Change these two lines (in the HTTPS server blocks):
# FROM:
ssl_certificate /etc/letsencrypt/live/agromart.com/fullchain.pem;
ssl_certificate_key /etc/letsencrypt/live/agromart.com/privkey.pem;

# TO:
ssl_certificate /etc/nginx/ssl/fullchain.pem;
ssl_certificate_key /etc/nginx/ssl/privkey.pem;

# Save: Ctrl+X → Y → Enter

# 7. Update docker-compose.yml nginx volumes
nano docker-compose.yml

# Find the nginx service, update volumes section:
    volumes:
      - ./nginx/nginx.conf:/etc/nginx/nginx.conf:ro
      - ./nginx/ssl:/etc/nginx/ssl:ro

# Save: Ctrl+X → Y → Enter

✅ SSL certificates configured!
```

---

### STEP 9: GET THIRD-PARTY CREDENTIALS (30 min)

**Time: 30 minutes**
**Difficulty: Easy**

You need to sign up for these services and get API keys:

#### 9.1 Twilio (SMS)

```
1. Go to: https://www.twilio.com/try-twilio

2. Sign up with email

3. Verify phone number

4. In Dashboard, find:
   ├─ Account SID: ACxxxxxxxxxxxxxxxxxxxx
   ├─ Auth Token: Click "Show" to reveal
   └─ Phone Number: Get a trial number

5. Write these down or copy to notepad:
   TWILIO_SID=ACxxxxxxxxxxxxxxxxxxxx
   TWILIO_TOKEN=your_auth_token
   TWILIO_PHONE=+1234567890
```

#### 9.2 Razorpay (Payments)

```
1. Go to: https://razorpay.com/

2. Click "Sign Up" → "Get Started"

3. Fill business details

4. After verification, go to:
   Settings → API Keys

5. Generate Test Key (for testing)

6. Copy:
   RAZORPAY_KEY=rzp_test_xxxxxxxxxxxxx
   RAZORPAY_SECRET=xxxxxxxxxxxxxxxxxxxxx
```

#### 9.3 Google Maps API

```
1. Go to: https://console.cloud.google.com/

2. Create new project: "VMV AgroMart"

3. Enable APIs:
   ├─ Maps JavaScript API
   ├─ Places API
   └─ Geocoding API

4. Create API Key:
   ├─ Credentials → Create Credentials → API Key
   └─ Restrict key to your domain

5. Copy:
   GOOGLE_MAPS_KEY=AIzaSyxxxxxxxxxxxxxxxxxxxxx
```

#### 9.4 Update .env File

```bash
# On Oracle server
cd /opt/vmv-agromart
nano .env

# Update these values with real credentials:
TWILIO_SID=AC... (from step 9.1)
TWILIO_TOKEN=... (from step 9.1)
TWILIO_PHONE=+1... (from step 9.1)
RAZORPAY_KEY=rzp_test_... (from step 9.2)
RAZORPAY_SECRET=... (from step 9.2)
GOOGLE_MAPS_KEY=AIzaSy... (from step 9.3)

# Save: Ctrl+X → Y → Enter

✅ All credentials configured!
```

---

### STEP 10: PREPARE DATABASE FILES (15 min)

**Time: 15 minutes**
**Difficulty: Easy**

```bash
# On Oracle server
cd /opt/vmv-agromart

# You need to upload your SQL files here
# database/vmv-agromart-schema.sql
# database/vmv-agromart-dummy-data.sql

# Option 1: Using SCP (from your computer)
# Open NEW terminal/command prompt on YOUR COMPUTER (not Oracle server):

# Windows (Command Prompt):
scp -i %USERPROFILE%\.ssh\oracle-key.key C:\path\to\vmv-agromart-schema.sql ubuntu@YOUR_IP:/opt/vmv-agromart/database/
scp -i %USERPROFILE%\.ssh\oracle-key.key C:\path\to\vmv-agromart-dummy-data.sql ubuntu@YOUR_IP:/opt/vmv-agromart/database/

# Mac/Linux:
scp -i ~/.ssh/oracle-key.key /path/to/vmv-agromart-schema.sql ubuntu@YOUR_IP:/opt/vmv-agromart/database/
scp -i ~/.ssh/oracle-key.key /path/to/vmv-agromart-dummy-data.sql ubuntu@YOUR_IP:/opt/vmv-agromart/database/

# Option 2: Create files directly on server
cd /opt/vmv-agromart/database
nano vmv-agromart-schema.sql
# Copy-paste SQL schema content
# Save: Ctrl+X → Y → Enter

nano vmv-agromart-dummy-data.sql
# Copy-paste dummy data SQL content
# Save: Ctrl+X → Y → Enter

✅ Database files ready!
```

---

### STEP 11: BUILD & PUSH DOCKER IMAGES (1 hour)

**Time: 1 hour**
**Difficulty: Medium**

⚠️ **This step requires your application source code**

#### Option A: If you have Docker images ready

```bash
# Just update docker-compose.yml with your image names
nano docker-compose.yml

# Update these lines:
backend:
  image: yourdockerhub/vmv-backend:latest

frontend:
  image: yourdockerhub/vmv-frontend:latest

# Save and continue to Step 12
```

#### Option B: Build images from source

```bash
# On your LOCAL computer (not Oracle server)

# 1. Build backend
cd backend
docker build -t yourdockerhub/vmv-backend:latest .
docker push yourdockerhub/vmv-backend:latest

# 2. Build frontend
cd ../frontend
docker build -t yourdockerhub/vmv-frontend:latest .
docker push yourdockerhub/vmv-frontend:latest

# 3. Update docker-compose.yml on Oracle server
# (as shown in Option A)
```

---

### STEP 12: START APPLICATION (15 min)

**Time: 15 minutes**
**Difficulty: Easy**

```bash
# On Oracle server
cd /opt/vmv-agromart

# 1. Pull latest images
docker-compose pull

# 2. Start all services
docker-compose up -d

# This starts:
# ✅ MySQL database
# ✅ Redis cache
# ✅ Backend API
# ✅ Frontend web app
# ✅ Nginx reverse proxy

# 3. Check status
docker-compose ps

# All services should show "Up"

# 4. Check logs
docker-compose logs -f

# Press Ctrl+C to stop viewing logs

# 5. Test endpoints
curl http://localhost:8080/api/actuator/health
# Should return: {"status":"UP"}

curl http://localhost:4200
# Should return HTML

✅ Application is running!
```

---

### STEP 13: FINAL TESTING (1 hour)

**Time: 1 hour**
**Difficulty: Easy**

```bash
# 1. Test from browser
# Open: https://agromart.com
# You should see your app!

# 2. Test API
# Open: https://api.agromart.com/api/actuator/health
# Should show: {"status":"UP"}

# 3. Test login
# Go to: https://agromart.com
# Click Login
# Enter: +919823458001
# OTP: 123456 (test mode)

# 4. Browse products
# Check if products load

# 5. Test order flow
# Add items to cart
# Proceed to checkout
# Complete order

✅ If everything works, you're DONE with Day 1!
```

---

## 🎉 END OF DAY 1

**What you've accomplished:**
- ✅ Created Oracle Cloud account
- ✅ Deployed server instance
- ✅ Installed Docker
- ✅ Deployed application
- ✅ Configured domain
- ✅ Setup SSL/HTTPS
- ✅ Application is LIVE!

**Your app is now accessible at:**
- Website: https://agromart.com
- API: https://api.agromart.com

**Tomorrow:** We'll build mobile apps!

---

## 📱 DAY 2: MOBILE APP BUILD (Continued in next section...)

**I'll provide the mobile app build guide separately.**

---

## 🆘 TROUBLESHOOTING

### Common Issues & Solutions:

**1. Cannot connect to server via SSH**
```
Problem: Connection timeout
Solution:
- Check if instance is running (Oracle Console)
- Check Security List has port 22 open
- Check your internet connection
- Try from different network
```

**2. Docker commands fail**
```
Problem: Permission denied
Solution:
sudo usermod -aG docker ubuntu
exit
# Reconnect to server
```

**3. Domain not resolving**
```
Problem: Domain doesn't point to server
Solution:
- Wait 10-30 minutes for DNS propagation
- Check A records in domain settings
- Use: nslookup agromart.com
- Try: ping agromart.com
```

**4. SSL certificate fails**
```
Problem: Certbot cannot verify domain
Solution:
- Ensure domain points to your IP
- Stop nginx before running certbot
- Check port 80 is open in firewall
```

**5. Application won't start**
```
Problem: docker-compose up fails
Solution:
- Check logs: docker-compose logs
- Verify .env file has all values
- Check docker-compose.yml syntax
- Ensure images exist: docker images
```

---

## 💬 QUESTIONS? I'M HERE TO HELP!

**At any point, if you get stuck:**

1. **Copy the error message** exactly
2. **Tell me which step** you're on
3. **Share what you tried**
4. **I'll help you fix it!**

**Common questions I can help with:**
- "How do I paste in terminal?"
- "What does this error mean?"
- "Can you explain this step again?"
- "Is this normal?"
- "Did I do it right?"

**Don't hesitate to ask! No question is too small!** 😊

---

## ✅ CHECKLIST PROGRESS

Track your progress:

**Day 1:**
- [ ] Oracle account created
- [ ] Server instance running
- [ ] Can connect via SSH
- [ ] Docker installed
- [ ] Application files uploaded
- [ ] Domain configured
- [ ] SSL certificate working
- [ ] App accessible via HTTPS
- [ ] Can login to app
- [ ] All features working

**Day 2:**
- [ ] Android Studio installed
- [ ] APK built successfully
- [ ] iOS app built (if Mac)
- [ ] Mobile apps tested
- [ ] Apps submitted to stores

---

## 🎯 YOU'RE DOING GREAT!

Remember:
- Take breaks when needed
- Don't rush
- Ask questions
- Test each step
- Celebrate small wins!

**You've got this!** 💪

---

**Ready to start? Let me know when you begin, and I'll guide you through each step!** 🚀

© 2026 VMV AgroMart - DIY Deployment Guide
