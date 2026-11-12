# VMV AgroMart - Docker Deployment Guide

## 📦 Complete Dockerized Setup for Web & Mobile Applications

This guide provides step-by-step instructions to deploy VMV AgroMart using Docker containers.

---

## 🏗️ Architecture Overview

```
┌─────────────────────────────────────────────────────────────────┐
│                        Nginx Reverse Proxy                       │
│                    (Port 80/443 - Load Balancer)                │
└────────────────┬───────────────────────────┬────────────────────┘
                 │                           │
    ┌────────────▼─────────────┐   ┌────────▼──────────────┐
    │   Angular Web Frontend   │   │  Spring Boot Backend  │
    │      (Port 4200)         │   │     (Port 8080)       │
    └──────────────────────────┘   └───────┬───────────────┘
                                           │
                    ┌──────────────────────┴──────────────────┐
                    │                                          │
         ┌──────────▼──────────┐              ┌───────────▼──────────┐
         │   MySQL Database    │              │   Redis Cache        │
         │     (Port 3306)     │              │   (Port 6379)        │
         └─────────────────────┘              └──────────────────────┘

Mobile App (Ionic) connects to Backend API via HTTP
```

---

## 📋 Prerequisites

### Required Software:
- **Docker**: Version 20.10 or higher
- **Docker Compose**: Version 2.0 or higher
- **Git**: For cloning the repository

### System Requirements:
- **RAM**: Minimum 4GB (8GB recommended)
- **Disk Space**: Minimum 10GB free
- **CPU**: 2 cores minimum (4 cores recommended)

### External Services (Required):
- **Twilio Account**: For SMS OTP (https://www.twilio.com)
- **Razorpay Account**: For payments (https://razorpay.com)
- **Google Maps API Key**: For tracking (https://console.cloud.google.com)

---

## 🚀 Quick Start (5 Minutes)

### Step 1: Clone Repository and Setup

```bash
# Create project directory
mkdir vmv-agromart
cd vmv-agromart

# Create directory structure
mkdir -p backend web-frontend mobile-app database/init nginx/ssl
```

### Step 2: Copy Configuration Files

Place the following files in their respective locations:

```
vmv-agromart/
├── docker-compose.yml          # Main orchestration file
├── .env                        # Environment variables (copy from .env.example)
├── backend/
│   ├── Dockerfile             # Backend build instructions
│   ├── .dockerignore
│   ├── pom.xml                # Maven configuration
│   └── src/                   # Java source code
├── web-frontend/
│   ├── Dockerfile             # Frontend build instructions
│   ├── .dockerignore
│   ├── nginx.conf             # Nginx config for Angular
│   ├── package.json
│   └── src/                   # Angular source code
├── mobile-app/
│   ├── Dockerfile             # Mobile app build
│   ├── package.json
│   └── src/                   # Ionic source code
├── database/
│   └── init/
│       └── vmv-agromart-schema.sql  # Database initialization
└── nginx/
    ├── nginx.conf             # Main reverse proxy config
    └── ssl/                   # SSL certificates (optional)
```

### Step 3: Configure Environment

```bash
# Copy environment template
cp .env.example .env

# Edit .env file with your actual credentials
nano .env
```

**Critical Environment Variables to Update:**

```env
# Database
MYSQL_ROOT_PASSWORD=YourSecurePassword123!
MYSQL_PASSWORD=YourDatabasePassword456!

# JWT Secret (generate with: openssl rand -base64 32)
JWT_SECRET=your-generated-jwt-secret-key

# Twilio (SMS Gateway)
TWILIO_ACCOUNT_SID=ACxxxxxxxxxxxxxxxxxxxxxxxx
TWILIO_AUTH_TOKEN=your_twilio_token
TWILIO_PHONE_NUMBER=+1234567890

# Razorpay (Payments)
RAZORPAY_KEY_ID=rzp_test_xxxxxxxxxxxxx
RAZORPAY_KEY_SECRET=your_razorpay_secret

# Google Maps
GOOGLE_MAPS_API_KEY=AIzaSyXXXXXXXXXXXXXXXXXXXXX

# Mobile API URL (use your server's public IP)
MOBILE_API_URL=http://192.168.1.100:8080/api
```

### Step 4: Copy Database Schema

```bash
# Copy the SQL initialization script
cp vmv-agromart-schema.sql database/init/
```

### Step 5: Start All Services

```bash
# Build and start all containers
docker-compose up -d

# View logs
docker-compose logs -f

# Check container status
docker-compose ps
```

### Step 6: Verify Deployment

```bash
# Wait for all services to be healthy (takes ~2 minutes)
docker-compose ps

# Test backend API
curl http://localhost:8080/api/actuator/health

# Test web frontend
curl http://localhost:4200

# Access web application
open http://localhost
```

---

## 🔧 Detailed Setup Instructions

### Building Individual Services

#### Backend Only:
```bash
cd backend
docker build -t vmv-backend:latest .
docker run -d \
  --name vmv-backend \
  -p 8080:8080 \
  --env-file ../.env \
  vmv-backend:latest
```

#### Web Frontend Only:
```bash
cd web-frontend
docker build -t vmv-web:latest .
docker run -d \
  --name vmv-web \
  -p 4200:80 \
  vmv-web:latest
```

#### Mobile App (Development Mode):
```bash
cd mobile-app
docker build -t vmv-mobile:latest .
docker run -d \
  --name vmv-mobile \
  -p 8100:8100 \
  -v $(pwd):/app \
  vmv-mobile:latest
```

---

## 📱 Mobile App Deployment

### Option 1: Using Docker Container (Development)

```bash
# Start mobile development server
docker-compose up mobile-app

# Access at http://localhost:8100
# Mobile app will connect to API_URL configured in .env
```

### Option 2: Build Native Apps

#### Android APK:
```bash
# Build Android APK
cd mobile-app
docker build -t vmv-mobile-builder .
docker run -v $(pwd)/output:/output vmv-mobile-builder /build-android.sh

# APK will be in: mobile-app/output/vmv-agromart.apk
```

#### iOS App (Requires macOS):
```bash
# On macOS with Xcode installed
cd mobile-app
ionic build --prod
npx cap sync ios
cd ios/App
xcodebuild -workspace App.xcworkspace \
  -scheme App \
  -configuration Release \
  -archivePath build/App.xcarchive \
  archive
```

---

## 🌐 Production Deployment

### SSL/HTTPS Configuration

#### 1. Obtain SSL Certificate

**Using Let's Encrypt (Free):**
```bash
# Install certbot
sudo apt-get update
sudo apt-get install certbot

# Get certificate
sudo certbot certonly --standalone \
  -d yourdomain.com \
  -d www.yourdomain.com

# Certificates will be in: /etc/letsencrypt/live/yourdomain.com/
```

#### 2. Configure Nginx for HTTPS

```bash
# Copy certificates to nginx/ssl/
cp /etc/letsencrypt/live/yourdomain.com/fullchain.pem nginx/ssl/cert.pem
cp /etc/letsencrypt/live/yourdomain.com/privkey.pem nginx/ssl/key.pem

# Update nginx.conf - uncomment HTTPS server block
# Restart nginx
docker-compose restart nginx
```

### Cloud Deployment Options

#### AWS Deployment:
```bash
# Install AWS CLI
aws configure

# Push images to ECR
aws ecr get-login-password --region ap-south-1 | \
  docker login --username AWS --password-stdin \
  123456789.dkr.ecr.ap-south-1.amazonaws.com

docker tag vmv-backend:latest \
  123456789.dkr.ecr.ap-south-1.amazonaws.com/vmv-backend:latest
docker push 123456789.dkr.ecr.ap-south-1.amazonaws.com/vmv-backend:latest

# Deploy using ECS or EKS
```

#### DigitalOcean Deployment:
```bash
# Install doctl
doctl auth init

# Create droplet
doctl compute droplet create vmv-agromart \
  --size s-2vcpu-4gb \
  --image ubuntu-22-04-x64 \
  --region blr1

# SSH and install Docker
ssh root@your-droplet-ip
curl -fsSL https://get.docker.com | sh

# Clone and deploy
git clone your-repo
cd vmv-agromart
docker-compose up -d
```

#### Google Cloud Deployment:
```bash
# Install gcloud CLI
gcloud init

# Deploy to Cloud Run
gcloud builds submit --tag gcr.io/PROJECT-ID/vmv-backend
gcloud run deploy vmv-backend \
  --image gcr.io/PROJECT-ID/vmv-backend \
  --platform managed \
  --region asia-south1
```

---

## 🔍 Monitoring & Management

### View Logs

```bash
# All services
docker-compose logs -f

# Specific service
docker-compose logs -f backend
docker-compose logs -f web-frontend

# Last 100 lines
docker-compose logs --tail=100 backend
```

### Check Service Health

```bash
# Container status
docker-compose ps

# Service health checks
curl http://localhost:8080/api/actuator/health
curl http://localhost/health

# Database connection
docker exec -it vmv-mysql mysql -u agromart_user -p vmv_agromart

# Redis connection
docker exec -it vmv-redis redis-cli -a redis_password
```

### Resource Usage

```bash
# CPU and memory usage
docker stats

# Disk usage
docker system df

# Detailed container info
docker inspect vmv-backend
```

---

## 🗄️ Database Management

### Access Database

```bash
# Using MySQL CLI
docker exec -it vmv-mysql mysql -u root -p

# Using PhpMyAdmin (if enabled)
open http://localhost:8081
```

### Backup Database

```bash
# Create backup
docker exec vmv-mysql mysqldump \
  -u root -p$MYSQL_ROOT_PASSWORD \
  vmv_agromart > backup-$(date +%Y%m%d).sql

# Restore backup
docker exec -i vmv-mysql mysql \
  -u root -p$MYSQL_ROOT_PASSWORD \
  vmv_agromart < backup-20260515.sql
```

### Redis Management

```bash
# Access Redis CLI
docker exec -it vmv-redis redis-cli -a redis_password

# View all keys
KEYS *

# Monitor commands
MONITOR

# Get OTP for phone number
GET otp:+919876543210
```

---

## 🛠️ Troubleshooting

### Common Issues

#### 1. Container Fails to Start

```bash
# Check logs
docker-compose logs backend

# Common causes:
# - Environment variables not set
# - Database not ready
# - Port already in use

# Solution: Check .env file and restart
docker-compose down
docker-compose up -d
```

#### 2. Database Connection Error

```bash
# Check if MySQL is running
docker-compose ps mysql

# Check connection
docker exec -it vmv-mysql mysqladmin ping -h localhost

# Verify credentials in .env match docker-compose.yml
```

#### 3. Mobile App Can't Connect to API

```bash
# Update MOBILE_API_URL in .env to server's public IP
MOBILE_API_URL=http://192.168.1.100:8080/api

# Rebuild mobile app
cd mobile-app
ionic build --prod
```

#### 4. Out of Memory

```bash
# Increase Docker memory limit
# Docker Desktop → Settings → Resources → Memory: 6GB

# Or add memory limits to docker-compose.yml
services:
  backend:
    mem_limit: 1g
```

#### 5. Port Already in Use

```bash
# Find process using port
sudo lsof -i :8080
sudo lsof -i :3306

# Kill process or change port in docker-compose.yml
ports:
  - "8081:8080"  # Use different host port
```

---

## 🔄 Updates & Maintenance

### Update Application

```bash
# Pull latest code
git pull

# Rebuild containers
docker-compose build --no-cache

# Restart services
docker-compose down
docker-compose up -d
```

### Update Single Service

```bash
# Rebuild backend only
docker-compose build backend
docker-compose up -d backend

# Zero-downtime update
docker-compose up -d --no-deps backend
```

### Clean Up

```bash
# Stop all containers
docker-compose down

# Remove volumes (WARNING: deletes data)
docker-compose down -v

# Clean up old images
docker image prune -a

# Full cleanup
docker system prune -a --volumes
```

---

## 📊 Performance Optimization

### Production Configuration

#### 1. Enable Caching
```yaml
# Add Redis cache for API responses
services:
  backend:
    environment:
      SPRING_CACHE_TYPE: redis
```

#### 2. Database Optimization
```sql
-- Add indexes for frequently queried columns
CREATE INDEX idx_orders_status_date ON orders(order_status, created_at);
CREATE INDEX idx_products_farmer_available ON products(farmer_id, is_available);
```

#### 3. Enable Compression
```nginx
# Already configured in nginx.conf
gzip on;
gzip_comp_level 6;
```

#### 4. Scale Services
```bash
# Run multiple backend instances
docker-compose up -d --scale backend=3

# Load balancing configured in nginx.conf
```

---

## 🔐 Security Checklist

- [ ] Change all default passwords in `.env`
- [ ] Generate secure JWT secret: `openssl rand -base64 32`
- [ ] Enable HTTPS with valid SSL certificate
- [ ] Set up firewall rules (only expose 80, 443)
- [ ] Enable Docker security scanning
- [ ] Regular database backups
- [ ] Monitor logs for suspicious activity
- [ ] Keep Docker images updated
- [ ] Use secrets management for production

---

## 📞 Support & Documentation

### Useful Commands Reference

```bash
# Start services
docker-compose up -d

# Stop services
docker-compose down

# View logs
docker-compose logs -f [service]

# Rebuild service
docker-compose build [service]

# Execute command in container
docker exec -it [container] [command]

# Shell access
docker exec -it vmv-backend sh

# Copy files
docker cp file.txt vmv-backend:/app/
docker cp vmv-backend:/app/logs/app.log ./

# Database backup
docker exec vmv-mysql mysqldump -u root -p vmv_agromart > backup.sql

# Database restore
docker exec -i vmv-mysql mysql -u root -p vmv_agromart < backup.sql
```

---

## 🎯 Next Steps

1. **Complete Backend Development**: Implement all API endpoints
2. **Frontend Integration**: Connect Angular components to backend
3. **Mobile App Development**: Build Ionic components
4. **Payment Integration**: Integrate Razorpay SDK
5. **Map Integration**: Add Google Maps for tracking
6. **Testing**: Write unit and integration tests
7. **CI/CD Setup**: Automate deployment pipeline
8. **Monitoring**: Set up application monitoring (Prometheus, Grafana)

---

## 📝 Notes

- **Development**: Use `docker-compose up` without `-d` to see live logs
- **Production**: Always use `-d` for detached mode
- **Scaling**: Load balancer (Nginx) handles multiple backend instances
- **Data Persistence**: Volumes ensure data survives container restarts
- **Health Checks**: Automatic container restart on failures

---

**Version**: 1.0.0
**Last Updated**: 2026-05-15
**Support**: support@vmvagromart.com
