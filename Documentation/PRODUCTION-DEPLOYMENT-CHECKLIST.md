# VMV AgroMart - Production Deployment Checklist

## ✅ Step-by-Step Production Deployment

---

## 📋 PHASE 1: AWS Setup (Day 1)

### 1.1 Create AWS Account & Configure CLI
```bash
# Install AWS CLI
curl "https://awscli.amazonaws.com/awscli-exe-linux-x86_64.zip" -o "awscliv2.zip"
unzip awscliv2.zip
sudo ./aws/install

# Configure
aws configure
# Enter: Access Key, Secret Key, Region (us-east-1), Format (json)

# Verify
aws sts get-caller-identity
```
**Time:** 15 minutes
**Status:** [ ] Complete

---

### 1.2 Create ECR Repositories
```bash
aws ecr create-repository --repository-name vmv-agromart/backend
aws ecr create-repository --repository-name vmv-agromart/web-frontend

# Save ECR URLs:
# Backend: _____________________.dkr.ecr.us-east-1.amazonaws.com/vmv-agromart/backend
# Frontend: _____________________.dkr.ecr.us-east-1.amazonaws.com/vmv-agromart/web-frontend
```
**Time:** 5 minutes
**Status:** [ ] Complete

---

### 1.3 Request SSL Certificate
```bash
# Go to AWS Certificate Manager (ACM)
# Request certificate for:
# - agromart.com
# - *.agromart.com

# Validate via DNS (add CNAME records)

# Certificate ARN: arn:aws:acm:us-east-1:___________:certificate/_________
```
**Time:** 30 minutes (including DNS validation)
**Status:** [ ] Complete

---

## 📋 PHASE 2: Build Docker Images (Day 1)

### 2.1 Build Backend Image
```bash
cd backend

# Build
docker build -t vmv-agromart/backend:v1.0.0 .

# Login to ECR
aws ecr get-login-password --region us-east-1 | docker login --username AWS --password-stdin YOUR_ACCOUNT_ID.dkr.ecr.us-east-1.amazonaws.com

# Tag
docker tag vmv-agromart/backend:v1.0.0 \
  YOUR_ACCOUNT_ID.dkr.ecr.us-east-1.amazonaws.com/vmv-agromart/backend:v1.0.0

# Push
docker push YOUR_ACCOUNT_ID.dkr.ecr.us-east-1.amazonaws.com/vmv-agromart/backend:v1.0.0
```
**Time:** 20 minutes
**Status:** [ ] Complete

---

### 2.2 Build Frontend Image
```bash
cd web-frontend

# Build
docker build -t vmv-agromart/web-frontend:v1.0.0 .

# Tag
docker tag vmv-agromart/web-frontend:v1.0.0 \
  YOUR_ACCOUNT_ID.dkr.ecr.us-east-1.amazonaws.com/vmv-agromart/web-frontend:v1.0.0

# Push
docker push YOUR_ACCOUNT_ID.dkr.ecr.us-east-1.amazonaws.com/vmv-agromart/web-frontend:v1.0.0
```
**Time:** 15 minutes
**Status:** [ ] Complete

---

## 📋 PHASE 3: Create EKS Cluster (Day 1-2)

### 3.1 Install Tools
```bash
# Install eksctl
curl --silent --location "https://github.com/weaveworks/eksctl/releases/latest/download/eksctl_$(uname -s)_amd64.tar.gz" | tar xz -C /tmp
sudo mv /tmp/eksctl /usr/local/bin

# Install kubectl
curl -LO "https://dl.k8s.io/release/$(curl -L -s https://dl.k8s.io/release/stable.txt)/bin/linux/amd64/kubectl"
sudo install -o root -g root -m 0755 kubectl /usr/local/bin/kubectl

# Verify
eksctl version
kubectl version --client
```
**Time:** 10 minutes
**Status:** [ ] Complete

---

### 3.2 Create EKS Cluster
```bash
# Create cluster (15-20 minutes)
eksctl create cluster \
  --name vmv-agromart-prod \
  --version 1.28 \
  --region us-east-1 \
  --nodegroup-name standard-workers \
  --node-type t3.medium \
  --nodes 3 \
  --nodes-min 2 \
  --nodes-max 6 \
  --managed

# Verify
kubectl get nodes
```
**Time:** 20 minutes
**Status:** [ ] Complete

---

## 📋 PHASE 4: Deploy Database (Day 2)

### Option A: AWS RDS (Recommended)
```bash
# Create via AWS Console:
# 1. Go to RDS → Create database
# 2. Engine: MySQL 8.0
# 3. Template: Production
# 4. DB instance: db.t3.medium
# 5. Storage: 100GB GP3
# 6. Multi-AZ: Yes
# 7. VPC: Same as EKS
# 8. Create database

# Note endpoint: ___________.rds.amazonaws.com
```
**Time:** 15 minutes
**Status:** [ ] Complete

### Option B: In-Cluster MySQL (For Testing)
```bash
kubectl apply -f mysql-statefulset.yaml
kubectl get pods -n vmv-agromart-prod
```
**Time:** 5 minutes
**Status:** [ ] Complete

---

## 📋 PHASE 5: Deploy Application (Day 2)

### 5.1 Create Namespace
```bash
kubectl create namespace vmv-agromart-prod
kubectl config set-context --current --namespace=vmv-agromart-prod
```
**Status:** [ ] Complete

---

### 5.2 Create Secrets
```bash
# Database secret
kubectl create secret generic db-credentials \
  --from-literal=username=agromart_user \
  --from-literal=password=YOUR_DB_PASSWORD \
  --from-literal=database=vmv_agromart

# JWT secret
kubectl create secret generic jwt-secret \
  --from-literal=secret=$(openssl rand -base64 32)

# Twilio (SMS)
kubectl create secret generic twilio-credentials \
  --from-literal=account-sid=YOUR_TWILIO_SID \
  --from-literal=auth-token=YOUR_TWILIO_TOKEN \
  --from-literal=phone-number=YOUR_TWILIO_PHONE

# Razorpay (Payments)
kubectl create secret generic razorpay-credentials \
  --from-literal=key-id=YOUR_RAZORPAY_KEY \
  --from-literal=key-secret=YOUR_RAZORPAY_SECRET

# Google Maps
kubectl create secret generic google-maps-key \
  --from-literal=api-key=YOUR_GOOGLE_MAPS_KEY
```
**Status:** [ ] Complete

---

### 5.3 Deploy Services
```bash
# Deploy backend
kubectl apply -f backend-deployment.yaml

# Deploy frontend
kubectl apply -f web-frontend-deployment.yaml

# Deploy ingress
kubectl apply -f ingress.yaml

# Check status
kubectl get pods
kubectl get svc
kubectl get ingress
```
**Time:** 10 minutes
**Status:** [ ] Complete

---

### 5.4 Initialize Database
```bash
# Copy SQL files to pod
kubectl cp vmv-agromart-schema.sql mysql-0:/tmp/schema.sql
kubectl cp vmv-agromart-dummy-data.sql mysql-0:/tmp/data.sql

# Execute
kubectl exec -it mysql-0 -- mysql -u root -p < /tmp/schema.sql
kubectl exec -it mysql-0 -- mysql -u root -p < /tmp/data.sql
```
**Status:** [ ] Complete

---

## 📋 PHASE 6: Configure DNS (Day 2)

### 6.1 Get Load Balancer URL
```bash
kubectl get ingress vmv-agromart-ingress -o jsonpath='{.status.loadBalancer.ingress[0].hostname}'

# Example output: k8s-vmvagrom-xxxx.us-east-1.elb.amazonaws.com
```
**Load Balancer URL:** _________________________________

---

### 6.2 Create DNS Records
```bash
# In Route 53 or your DNS provider:
# Create A record (Alias):
# agromart.com → Load Balancer URL
# api.agromart.com → Load Balancer URL
# www.agromart.com → Load Balancer URL

# Wait for DNS propagation (5-10 minutes)
nslookup agromart.com
```
**Status:** [ ] Complete

---

## 📋 PHASE 7: Test Web Application (Day 2)

### 7.1 Test Website
```bash
# Open browser
https://agromart.com

# Test:
[ ] Homepage loads
[ ] Login with: +919823458001
[ ] OTP: 123456
[ ] Browse products
[ ] Add to cart
[ ] Checkout
```
**Status:** [ ] Complete

---

### 7.2 Test API
```bash
# Test health endpoint
curl https://api.agromart.com/api/actuator/health

# Expected: {"status":"UP"}
```
**Status:** [ ] Complete

---

## 📋 PHASE 8: Build Mobile Apps (Day 3)

### 8.1 Android APK

**Prerequisites:**
- [ ] Android Studio installed
- [ ] JDK 11+ installed
- [ ] Keystore created

**Steps:**
```bash
cd mobile-app

# Update API URL
cat > src/environments/environment.prod.ts << 'EOF'
export const environment = {
  production: true,
  apiUrl: 'https://api.agromart.com/api'
};
EOF

# Build
ionic build --prod
npx cap add android
npx cap sync android
npx cap open android

# In Android Studio:
# 1. Build → Generate Signed Bundle/APK
# 2. Choose APK
# 3. Use release keystore
# 4. Build
# 5. Output: app/release/app-release.apk
```
**APK Location:** _________________________________
**Status:** [ ] Complete

---

### 8.2 iOS IPA (macOS only)

**Prerequisites:**
- [ ] Xcode installed
- [ ] Apple Developer Account ($99/year)
- [ ] Certificates configured

**Steps:**
```bash
cd mobile-app

# Build
ionic build --prod
npx cap add ios
npx cap sync ios
npx cap open ios

# In Xcode:
# 1. Select Team
# 2. Configure signing
# 3. Product → Archive
# 4. Distribute → App Store Connect
# 5. Upload
```
**Status:** [ ] Complete

---

## 📋 PHASE 9: Deploy Mobile Apps (Day 4-5)

### 9.1 Google Play Store

**Steps:**
1. [ ] Create Play Console account ($25 one-time)
2. [ ] Create new app
3. [ ] Upload APK
4. [ ] Fill store listing:
   - [ ] Title: VMV AgroMart
   - [ ] Description
   - [ ] Screenshots (5 required)
   - [ ] Icon (512x512)
   - [ ] Feature graphic (1024x500)
5. [ ] Set pricing: Free
6. [ ] Submit for review (1-3 days)

**Play Store URL:** _________________________________
**Status:** [ ] Complete

---

### 9.2 Apple App Store

**Steps:**
1. [ ] Create App Store Connect account ($99/year)
2. [ ] Create new app
3. [ ] Upload IPA via Xcode
4. [ ] Fill app information:
   - [ ] Screenshots (iPhone, iPad)
   - [ ] Description
   - [ ] Keywords
   - [ ] Privacy policy URL
5. [ ] Submit for review (1-5 days)

**App Store URL:** _________________________________
**Status:** [ ] Complete

---

## 📋 PHASE 10: Monitoring Setup (Day 5)

### 10.1 Install Monitoring Tools
```bash
# Install Prometheus & Grafana
helm repo add prometheus-community https://prometheus-community.github.io/helm-charts
helm repo update

helm install prometheus prometheus-community/kube-prometheus-stack \
  --namespace monitoring \
  --create-namespace

# Get Grafana password
kubectl get secret --namespace monitoring prometheus-grafana \
  -o jsonpath="{.data.admin-password}" | base64 --decode

# Port forward to access
kubectl port-forward -n monitoring svc/prometheus-grafana 3000:80
```
**Grafana URL:** http://localhost:3000
**Username:** admin
**Password:** _________________________________
**Status:** [ ] Complete

---

### 10.2 Setup Alerts
```bash
# Configure alerts for:
[ ] CPU > 80%
[ ] Memory > 85%
[ ] Pods not ready
[ ] Database connection failures
[ ] API response time > 2s
```
**Status:** [ ] Complete

---

## 📋 PHASE 11: Final Checks (Day 5)

### 11.1 Security Checklist
- [ ] All secrets created (no hardcoded credentials)
- [ ] SSL certificate installed and working
- [ ] HTTPS enforced (HTTP redirects to HTTPS)
- [ ] Database in private subnet
- [ ] Security groups configured
- [ ] IAM roles properly configured
- [ ] No root privileges in containers

---

### 11.2 Performance Checklist
- [ ] Auto-scaling enabled (HPA)
- [ ] Resource limits set
- [ ] Database connection pooling
- [ ] Redis caching enabled
- [ ] CDN configured (optional)
- [ ] Image optimization
- [ ] Gzip compression enabled

---

### 11.3 Reliability Checklist
- [ ] Health checks configured
- [ ] Liveness probes working
- [ ] Readiness probes working
- [ ] Multi-AZ deployment
- [ ] Database backups automated
- [ ] Disaster recovery plan
- [ ] Rolling updates configured

---

### 11.4 Monitoring Checklist
- [ ] Grafana dashboards created
- [ ] CloudWatch logs enabled
- [ ] Alerts configured
- [ ] Uptime monitoring (Pingdom/UptimeRobot)
- [ ] Error tracking (Sentry)
- [ ] Application Performance Monitoring

---

## 📋 PHASE 12: Go Live! (Day 5)

### 12.1 Pre-Launch
```bash
# Final smoke tests:
[ ] Web app works
[ ] API responds
[ ] Mobile apps connect
[ ] Payments work (test mode)
[ ] OTP delivered
[ ] Orders created
[ ] Tracking works
```

---

### 12.2 Launch
```bash
# Switch to production mode:
1. [ ] Update payment gateway to live mode
2. [ ] Update OTP to real SMS
3. [ ] Update domain to production
4. [ ] Enable real-time monitoring
5. [ ] Announce launch! 🎉
```

---

### 12.3 Post-Launch
```bash
# Monitor for 24 hours:
[ ] Check error rates
[ ] Monitor response times
[ ] Watch resource usage
[ ] Review user feedback
[ ] Fix critical issues
```

---

## 💰 Budget Tracker

| Item | Cost | Status |
|------|------|--------|
| AWS Account | Free | [ ] |
| Domain (.com) | $12/year | [ ] |
| SSL Certificate | Free (ACM) | [ ] |
| EKS Cluster | $72/month | [ ] |
| EC2 Nodes | $180/month | [ ] |
| RDS Database | $140/month | [ ] |
| Load Balancer | $25/month | [ ] |
| Google Play | $25 one-time | [ ] |
| Apple Developer | $99/year | [ ] |
| **Total Setup** | **$136** | |
| **Monthly** | **~$417** | |

---

## 📞 Support Contacts

### AWS Support
- [ ] Support plan subscribed
- [ ] Support case number: ___________

### Third-Party Services
- [ ] Twilio account: ___________
- [ ] Razorpay account: ___________
- [ ] Google Maps API: ___________

### Team Contacts
- [ ] DevOps engineer: ___________
- [ ] Backend developer: ___________
- [ ] Frontend developer: ___________
- [ ] Mobile developer: ___________

---

## 🎯 Success Criteria

### Day 1
- [x] AWS configured
- [x] Docker images built
- [x] ECR repositories created

### Day 2
- [ ] EKS cluster running
- [ ] Database deployed
- [ ] Application deployed
- [ ] Website accessible

### Day 3
- [ ] Mobile apps built
- [ ] API tested
- [ ] End-to-end testing complete

### Day 4-5
- [ ] Apps submitted to stores
- [ ] Monitoring configured
- [ ] Documentation complete

### Week 2
- [ ] Apps approved and published
- [ ] Users onboarded
- [ ] System stable

---

## 📈 KPIs to Track

### Week 1
- [ ] Website uptime: _____%
- [ ] API response time: ___ms
- [ ] Error rate: _____%
- [ ] Registered users: _____

### Month 1
- [ ] Total users: _____
- [ ] Daily active users: _____
- [ ] Orders placed: _____
- [ ] Revenue: ₹_____

---

## 🎉 Deployment Complete!

**Congratulations!** Your VMV AgroMart application is now live in production!

**URLs:**
- Website: https://agromart.com
- API: https://api.agromart.com
- Android: [Play Store Link]
- iOS: [App Store Link]

**Next Steps:**
1. Monitor for issues
2. Gather user feedback
3. Plan feature updates
4. Scale as needed
5. Celebrate success! 🎊

---

© 2026 VMV AgroMart - Production Deployment Checklist
