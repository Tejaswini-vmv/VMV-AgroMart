# VMV AgroMart - AWS EKS Production Deployment Guide

## Complete Guide for Web + Mobile (Android + iOS) Production Deployment

---

## 📋 Table of Contents

1. [Prerequisites](#prerequisites)
2. [Architecture Overview](#architecture-overview)
3. [AWS Setup](#aws-setup)
4. [EKS Cluster Setup](#eks-cluster-setup)
5. [Docker Images](#docker-images)
6. [Kubernetes Deployment](#kubernetes-deployment)
7. [Mobile App Deployment](#mobile-app-deployment)
8. [Domain & SSL](#domain--ssl)
9. [Monitoring & Logging](#monitoring--logging)
10. [CI/CD Pipeline](#cicd-pipeline)
11. [Scaling & Performance](#scaling--performance)
12. [Backup & Disaster Recovery](#backup--disaster-recovery)

---

## 1. Prerequisites

### Required Tools

```bash
# Install AWS CLI
curl "https://awscli.amazonaws.com/awscli-exe-linux-x86_64.zip" -o "awscliv2.zip"
unzip awscliv2.zip
sudo ./aws/install

# Install kubectl
curl -LO "https://dl.k8s.io/release/$(curl -L -s https://dl.k8s.io/release/stable.txt)/bin/linux/amd64/kubectl"
sudo install -o root -g root -m 0755 kubectl /usr/local/bin/kubectl

# Install eksctl
curl --silent --location "https://github.com/weaveworks/eksctl/releases/latest/download/eksctl_$(uname -s)_amd64.tar.gz" | tar xz -C /tmp
sudo mv /tmp/eksctl /usr/local/bin

# Install Helm
curl https://raw.githubusercontent.com/helm/helm/main/scripts/get-helm-3 | bash

# Install Docker
curl -fsSL https://get.docker.com | sh

# Verify installations
aws --version
kubectl version --client
eksctl version
helm version
docker --version
```

### AWS Account Requirements

- AWS Account with appropriate permissions
- IAM user with AdministratorAccess or EKS permissions
- Budget: ~$200-500/month for production setup
- Domain name (e.g., agromart.com)

---

## 2. Architecture Overview

### Production Architecture

```
┌─────────────────────────────────────────────────────────────┐
│                     INTERNET                                │
└────────────────┬────────────────────────────────────────────┘
                 │
┌────────────────▼────────────────────────────────────────────┐
│              Route 53 (DNS)                                 │
│  • agromart.com → Web App                                   │
│  • api.agromart.com → Backend API                           │
│  • admin.agromart.com → Admin Panel                         │
└────────────────┬────────────────────────────────────────────┘
                 │
┌────────────────▼────────────────────────────────────────────┐
│         Application Load Balancer (ALB)                     │
│         SSL/TLS Termination                                 │
└────────────────┬────────────────────────────────────────────┘
                 │
┌────────────────▼────────────────────────────────────────────┐
│              AWS EKS Cluster                                │
│                                                             │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐    │
│  │   Backend    │  │ Web Frontend │  │  Mobile API  │    │
│  │  (3 Pods)    │  │  (2 Pods)    │  │  (2 Pods)    │    │
│  └──────┬───────┘  └──────────────┘  └──────────────┘    │
│         │                                                   │
│  ┌──────▼───────┐  ┌──────────────┐                       │
│  │   Redis      │  │   RabbitMQ   │                       │
│  │ (StatefulSet)│  │(StatefulSet) │                       │
│  └──────────────┘  └──────────────┘                       │
└────────────────┬────────────────────────────────────────────┘
                 │
┌────────────────▼────────────────────────────────────────────┐
│             AWS Managed Services                            │
│  • RDS MySQL (Multi-AZ)                                     │
│  • ElastiCache Redis (Cluster mode)                         │
│  • S3 (File storage)                                        │
│  • CloudWatch (Monitoring)                                  │
│  • ECR (Container registry)                                 │
└─────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────┐
│              Mobile Apps                                    │
│  • Android App → Google Play Store                          │
│  • iOS App → Apple App Store                                │
│  • API: api.agromart.com                                    │
└─────────────────────────────────────────────────────────────┘
```

---

## 3. AWS Setup

### 3.1 Configure AWS CLI

```bash
# Configure AWS credentials
aws configure

# Enter:
# AWS Access Key ID: YOUR_ACCESS_KEY
# AWS Secret Access Key: YOUR_SECRET_KEY
# Default region: us-east-1 (or your preferred region)
# Default output format: json

# Verify configuration
aws sts get-caller-identity
```

### 3.2 Create S3 Bucket for Terraform State

```bash
# Create S3 bucket for state storage
aws s3 mb s3://vmv-agromart-terraform-state --region us-east-1

# Enable versioning
aws s3api put-bucket-versioning \
    --bucket vmv-agromart-terraform-state \
    --versioning-configuration Status=Enabled
```

### 3.3 Create ECR Repositories

```bash
# Create repositories for each service
aws ecr create-repository --repository-name vmv-agromart/backend --region us-east-1
aws ecr create-repository --repository-name vmv-agromart/web-frontend --region us-east-1
aws ecr create-repository --repository-name vmv-agromart/mobile-api --region us-east-1

# Get ECR login
aws ecr get-login-password --region us-east-1 | docker login --username AWS --password-stdin ACCOUNT_ID.dkr.ecr.us-east-1.amazonaws.com
```

---

## 4. EKS Cluster Setup

### 4.1 Create EKS Cluster with eksctl

Create file: `eks-cluster-config.yaml`

```yaml
apiVersion: eksctl.io/v1alpha5
kind: ClusterConfig

metadata:
  name: vmv-agromart-prod
  region: us-east-1
  version: "1.28"

# Networking
vpc:
  cidr: 10.0.0.0/16
  nat:
    gateway: HighlyAvailable

# IAM
iam:
  withOIDC: true
  serviceAccounts:
  - metadata:
      name: aws-load-balancer-controller
      namespace: kube-system
    wellKnownPolicies:
      awsLoadBalancerController: true
  - metadata:
      name: external-dns
      namespace: kube-system
    wellKnownPolicies:
      externalDNS: true
  - metadata:
      name: cert-manager
      namespace: cert-manager
    wellKnownPolicies:
      certManager: true

# Node Groups
managedNodeGroups:
  # General purpose nodes
  - name: ng-general
    instanceType: t3.medium
    desiredCapacity: 3
    minSize: 2
    maxSize: 6
    volumeSize: 50
    volumeType: gp3
    privateNetworking: true
    labels:
      role: general
    tags:
      k8s.io/cluster-autoscaler/enabled: "true"
      k8s.io/cluster-autoscaler/vmv-agromart-prod: "owned"
    iam:
      withAddonPolicies:
        imageBuilder: true
        autoScaler: true
        externalDNS: true
        certManager: true
        albIngress: true
        cloudWatch: true

  # Application nodes
  - name: ng-apps
    instanceType: t3.large
    desiredCapacity: 2
    minSize: 2
    maxSize: 10
    volumeSize: 100
    volumeType: gp3
    privateNetworking: true
    labels:
      role: application
    taints:
      - key: application
        value: "true"
        effect: NoSchedule
    tags:
      k8s.io/cluster-autoscaler/enabled: "true"

# Addons
addons:
  - name: vpc-cni
    version: latest
  - name: coredns
    version: latest
  - name: kube-proxy
    version: latest
  - name: aws-ebs-csi-driver
    version: latest

# CloudWatch Logging
cloudWatch:
  clusterLogging:
    enableTypes: ["*"]
```

### 4.2 Create the Cluster

```bash
# Create cluster (takes 15-20 minutes)
eksctl create cluster -f eks-cluster-config.yaml

# Verify cluster
kubectl get nodes
kubectl get pods -A

# Update kubeconfig
aws eks update-kubeconfig --name vmv-agromart-prod --region us-east-1
```

### 4.3 Install Essential Add-ons

```bash
# Install AWS Load Balancer Controller
kubectl apply -k "github.com/aws/eks-charts/stable/aws-load-balancer-controller//crds?ref=master"

helm repo add eks https://aws.github.io/eks-charts
helm install aws-load-balancer-controller eks/aws-load-balancer-controller \
  -n kube-system \
  --set clusterName=vmv-agromart-prod \
  --set serviceAccount.create=false \
  --set serviceAccount.name=aws-load-balancer-controller

# Install Metrics Server
kubectl apply -f https://github.com/kubernetes-sigs/metrics-server/releases/latest/download/components.yaml

# Install Cluster Autoscaler
kubectl apply -f https://raw.githubusercontent.com/kubernetes/autoscaler/master/cluster-autoscaler/cloudprovider/aws/examples/cluster-autoscaler-autodiscover.yaml

kubectl -n kube-system annotate deployment.apps/cluster-autoscaler \
  cluster-autoscaler.kubernetes.io/safe-to-evict="false"

kubectl -n kube-system edit deployment.apps/cluster-autoscaler

# Add these flags in the command section:
# - --balance-similar-node-groups
# - --skip-nodes-with-system-pods=false
# Replace <CLUSTER NAME> with: vmv-agromart-prod

# Install cert-manager
kubectl apply -f https://github.com/cert-manager/cert-manager/releases/download/v1.13.0/cert-manager.yaml
```

---

## 5. Docker Images

### 5.1 Build and Push Backend Image

```bash
# Navigate to backend directory
cd backend

# Create Dockerfile (if not exists)
cat > Dockerfile << 'EOF'
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
EOF

# Build image
docker build -t vmv-agromart/backend:latest .

# Tag for ECR
docker tag vmv-agromart/backend:latest \
  ACCOUNT_ID.dkr.ecr.us-east-1.amazonaws.com/vmv-agromart/backend:latest

docker tag vmv-agromart/backend:latest \
  ACCOUNT_ID.dkr.ecr.us-east-1.amazonaws.com/vmv-agromart/backend:v1.0.0

# Push to ECR
docker push ACCOUNT_ID.dkr.ecr.us-east-1.amazonaws.com/vmv-agromart/backend:latest
docker push ACCOUNT_ID.dkr.ecr.us-east-1.amazonaws.com/vmv-agromart/backend:v1.0.0
```

### 5.2 Build and Push Web Frontend Image

```bash
cd web-frontend

# Dockerfile
cat > Dockerfile << 'EOF'
FROM node:18-alpine AS build
WORKDIR /app
COPY package*.json ./
RUN npm ci
COPY . .
RUN npm run build -- --configuration production

FROM nginx:alpine
COPY --from=build /app/dist /usr/share/nginx/html
COPY nginx.conf /etc/nginx/conf.d/default.conf
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
EOF

# nginx.conf
cat > nginx.conf << 'EOF'
server {
    listen 80;
    server_name _;
    root /usr/share/nginx/html;
    index index.html;

    location / {
        try_files $uri $uri/ /index.html;
    }

    location /api {
        proxy_pass http://backend-service:8080;
        proxy_http_version 1.1;
        proxy_set_header Upgrade $http_upgrade;
        proxy_set_header Connection 'upgrade';
        proxy_set_header Host $host;
        proxy_cache_bypass $http_upgrade;
    }
}
EOF

# Build and push
docker build -t vmv-agromart/web-frontend:latest .
docker tag vmv-agromart/web-frontend:latest \
  ACCOUNT_ID.dkr.ecr.us-east-1.amazonaws.com/vmv-agromart/web-frontend:latest
docker push ACCOUNT_ID.dkr.ecr.us-east-1.amazonaws.com/vmv-agromart/web-frontend:latest
```

### 5.3 Environment Variables Script

Create `build-and-push.sh`:

```bash
#!/bin/bash

# Configuration
AWS_ACCOUNT_ID="YOUR_ACCOUNT_ID"
AWS_REGION="us-east-1"
ECR_REGISTRY="${AWS_ACCOUNT_ID}.dkr.ecr.${AWS_REGION}.amazonaws.com"
VERSION="${1:-latest}"

# Login to ECR
aws ecr get-login-password --region ${AWS_REGION} | \
  docker login --username AWS --password-stdin ${ECR_REGISTRY}

# Build and push backend
echo "Building backend..."
cd backend
docker build -t vmv-agromart/backend:${VERSION} .
docker tag vmv-agromart/backend:${VERSION} ${ECR_REGISTRY}/vmv-agromart/backend:${VERSION}
docker push ${ECR_REGISTRY}/vmv-agromart/backend:${VERSION}

# Build and push web frontend
echo "Building web frontend..."
cd ../web-frontend
docker build -t vmv-agromart/web-frontend:${VERSION} .
docker tag vmv-agromart/web-frontend:${VERSION} ${ECR_REGISTRY}/vmv-agromart/web-frontend:${VERSION}
docker push ${ECR_REGISTRY}/vmv-agromart/web-frontend:${VERSION}

echo "All images pushed successfully!"
echo "Backend: ${ECR_REGISTRY}/vmv-agromart/backend:${VERSION}"
echo "Web: ${ECR_REGISTRY}/vmv-agromart/web-frontend:${VERSION}"
```

```bash
chmod +x build-and-push.sh
./build-and-push.sh v1.0.0
```

---

## 6. Kubernetes Deployment

### 6.1 Create Namespace

```bash
kubectl create namespace vmv-agromart-prod
kubectl config set-context --current --namespace=vmv-agromart-prod
```

### 6.2 Create Secrets

```bash
# Database credentials
kubectl create secret generic db-credentials \
  --from-literal=username=agromart_user \
  --from-literal=password=YOUR_STRONG_PASSWORD \
  --from-literal=database=vmv_agromart

# JWT Secret
kubectl create secret generic jwt-secret \
  --from-literal=secret=$(openssl rand -base64 32)

# Twilio credentials
kubectl create secret generic twilio-credentials \
  --from-literal=account-sid=YOUR_TWILIO_SID \
  --from-literal=auth-token=YOUR_TWILIO_TOKEN \
  --from-literal=phone-number=YOUR_TWILIO_PHONE

# Razorpay credentials
kubectl create secret generic razorpay-credentials \
  --from-literal=key-id=YOUR_RAZORPAY_KEY \
  --from-literal=key-secret=YOUR_RAZORPAY_SECRET

# Google Maps API
kubectl create secret generic google-maps-key \
  --from-literal=api-key=YOUR_GOOGLE_MAPS_KEY
```

### 6.3 Create ConfigMap

Create `configmap.yaml`:

```yaml
apiVersion: v1
kind: ConfigMap
metadata:
  name: app-config
  namespace: vmv-agromart-prod
data:
  # Application settings
  SPRING_PROFILES_ACTIVE: "production"
  SERVER_PORT: "8080"

  # Database
  DB_HOST: "vmv-agromart-db.xxxxx.us-east-1.rds.amazonaws.com"
  DB_PORT: "3306"
  DB_NAME: "vmv_agromart"

  # Redis
  REDIS_HOST: "vmv-agromart-redis.xxxxx.cache.amazonaws.com"
  REDIS_PORT: "6379"

  # App URLs
  WEB_URL: "https://agromart.com"
  API_URL: "https://api.agromart.com"

  # CORS
  CORS_ALLOWED_ORIGINS: "https://agromart.com,https://www.agromart.com"
```

Apply:
```bash
kubectl apply -f configmap.yaml
```

---

## 6.4 Database Deployment

### Option A: AWS RDS (Recommended for Production)

```bash
# Create RDS MySQL instance via AWS Console or CLI
aws rds create-db-instance \
    --db-instance-identifier vmv-agromart-db \
    --db-instance-class db.t3.medium \
    --engine mysql \
    --engine-version 8.0.35 \
    --master-username admin \
    --master-user-password YOUR_STRONG_PASSWORD \
    --allocated-storage 100 \
    --storage-type gp3 \
    --storage-encrypted \
    --backup-retention-period 7 \
    --preferred-backup-window "03:00-04:00" \
    --preferred-maintenance-window "mon:04:00-mon:05:00" \
    --multi-az \
    --publicly-accessible false \
    --vpc-security-group-ids sg-xxxxx \
    --db-subnet-group-name vmv-agromart-db-subnet
```

### Option B: In-Cluster MySQL (Development/Testing)

Create `mysql-statefulset.yaml`:

```yaml
apiVersion: v1
kind: Service
metadata:
  name: mysql
  namespace: vmv-agromart-prod
spec:
  ports:
  - port: 3306
  selector:
    app: mysql
  clusterIP: None
---
apiVersion: apps/v1
kind: StatefulSet
metadata:
  name: mysql
  namespace: vmv-agromart-prod
spec:
  serviceName: mysql
  replicas: 1
  selector:
    matchLabels:
      app: mysql
  template:
    metadata:
      labels:
        app: mysql
    spec:
      containers:
      - name: mysql
        image: mysql:8.0
        env:
        - name: MYSQL_ROOT_PASSWORD
          valueFrom:
            secretKeyRef:
              name: db-credentials
              key: password
        - name: MYSQL_DATABASE
          valueFrom:
            secretKeyRef:
              name: db-credentials
              key: database
        - name: MYSQL_USER
          valueFrom:
            secretKeyRef:
              name: db-credentials
              key: username
        - name: MYSQL_PASSWORD
          valueFrom:
            secretKeyRef:
              name: db-credentials
              key: password
        ports:
        - containerPort: 3306
          name: mysql
        volumeMounts:
        - name: mysql-storage
          mountPath: /var/lib/mysql
  volumeClaimTemplates:
  - metadata:
      name: mysql-storage
    spec:
      accessModes: [ "ReadWriteOnce" ]
      storageClassName: gp3
      resources:
        requests:
          storage: 100Gi
```

```bash
kubectl apply -f mysql-statefulset.yaml
```

---

## 6.5 Redis Deployment

### Option A: AWS ElastiCache (Recommended)

```bash
aws elasticache create-replication-group \
    --replication-group-id vmv-agromart-redis \
    --replication-group-description "VMV AgroMart Redis Cluster" \
    --engine redis \
    --cache-node-type cache.t3.medium \
    --num-cache-clusters 2 \
    --automatic-failover-enabled \
    --at-rest-encryption-enabled \
    --transit-encryption-enabled \
    --security-group-ids sg-xxxxx \
    --cache-subnet-group-name vmv-agromart-redis-subnet
```

### Option B: In-Cluster Redis

Create `redis-statefulset.yaml`:

```yaml
apiVersion: v1
kind: Service
metadata:
  name: redis
  namespace: vmv-agromart-prod
spec:
  ports:
  - port: 6379
  selector:
    app: redis
  clusterIP: None
---
apiVersion: apps/v1
kind: StatefulSet
metadata:
  name: redis
  namespace: vmv-agromart-prod
spec:
  serviceName: redis
  replicas: 1
  selector:
    matchLabels:
      app: redis
  template:
    metadata:
      labels:
        app: redis
    spec:
      containers:
      - name: redis
        image: redis:7-alpine
        ports:
        - containerPort: 6379
          name: redis
        volumeMounts:
        - name: redis-storage
          mountPath: /data
        command:
        - redis-server
        - "--appendonly"
        - "yes"
  volumeClaimTemplates:
  - metadata:
      name: redis-storage
    spec:
      accessModes: [ "ReadWriteOnce" ]
      storageClassName: gp3
      resources:
        requests:
          storage: 20Gi
```

```bash
kubectl apply -f redis-statefulset.yaml
```

---

## 6.6 Backend Deployment

Create `backend-deployment.yaml`:

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: backend
  namespace: vmv-agromart-prod
spec:
  replicas: 3
  selector:
    matchLabels:
      app: backend
  template:
    metadata:
      labels:
        app: backend
    spec:
      containers:
      - name: backend
        image: ACCOUNT_ID.dkr.ecr.us-east-1.amazonaws.com/vmv-agromart/backend:latest
        ports:
        - containerPort: 8080
        env:
        - name: SPRING_PROFILES_ACTIVE
          valueFrom:
            configMapKeyRef:
              name: app-config
              key: SPRING_PROFILES_ACTIVE
        - name: DB_HOST
          valueFrom:
            configMapKeyRef:
              name: app-config
              key: DB_HOST
        - name: DB_USERNAME
          valueFrom:
            secretKeyRef:
              name: db-credentials
              key: username
        - name: DB_PASSWORD
          valueFrom:
            secretKeyRef:
              name: db-credentials
              key: password
        - name: JWT_SECRET
          valueFrom:
            secretKeyRef:
              name: jwt-secret
              key: secret
        - name: TWILIO_ACCOUNT_SID
          valueFrom:
            secretKeyRef:
              name: twilio-credentials
              key: account-sid
        - name: RAZORPAY_KEY_ID
          valueFrom:
            secretKeyRef:
              name: razorpay-credentials
              key: key-id
        resources:
          requests:
            memory: "512Mi"
            cpu: "500m"
          limits:
            memory: "1Gi"
            cpu: "1000m"
        livenessProbe:
          httpGet:
            path: /api/actuator/health
            port: 8080
          initialDelaySeconds: 60
          periodSeconds: 10
        readinessProbe:
          httpGet:
            path: /api/actuator/health
            port: 8080
          initialDelaySeconds: 30
          periodSeconds: 5
---
apiVersion: v1
kind: Service
metadata:
  name: backend-service
  namespace: vmv-agromart-prod
spec:
  selector:
    app: backend
  ports:
  - protocol: TCP
    port: 8080
    targetPort: 8080
  type: ClusterIP
```

```bash
kubectl apply -f backend-deployment.yaml
```

---

## 6.7 Web Frontend Deployment

Create `web-frontend-deployment.yaml`:

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: web-frontend
  namespace: vmv-agromart-prod
spec:
  replicas: 2
  selector:
    matchLabels:
      app: web-frontend
  template:
    metadata:
      labels:
        app: web-frontend
    spec:
      containers:
      - name: web-frontend
        image: ACCOUNT_ID.dkr.ecr.us-east-1.amazonaws.com/vmv-agromart/web-frontend:latest
        ports:
        - containerPort: 80
        resources:
          requests:
            memory: "128Mi"
            cpu: "100m"
          limits:
            memory: "256Mi"
            cpu: "200m"
---
apiVersion: v1
kind: Service
metadata:
  name: web-frontend-service
  namespace: vmv-agromart-prod
spec:
  selector:
    app: web-frontend
  ports:
  - protocol: TCP
    port: 80
    targetPort: 80
  type: ClusterIP
```

```bash
kubectl apply -f web-frontend-deployment.yaml
```

---

## 6.8 Ingress Configuration

Create `ingress.yaml`:

```yaml
apiVersion: networking.k8s.io/v1
kind: Ingress
metadata:
  name: vmv-agromart-ingress
  namespace: vmv-agromart-prod
  annotations:
    kubernetes.io/ingress.class: alb
    alb.ingress.kubernetes.io/scheme: internet-facing
    alb.ingress.kubernetes.io/target-type: ip
    alb.ingress.kubernetes.io/listen-ports: '[{"HTTP": 80}, {"HTTPS": 443}]'
    alb.ingress.kubernetes.io/ssl-redirect: '443'
    alb.ingress.kubernetes.io/certificate-arn: arn:aws:acm:us-east-1:ACCOUNT_ID:certificate/CERT_ID
    alb.ingress.kubernetes.io/healthcheck-path: /health
    alb.ingress.kubernetes.io/success-codes: '200,301,302'
spec:
  rules:
  - host: agromart.com
    http:
      paths:
      - path: /
        pathType: Prefix
        backend:
          service:
            name: web-frontend-service
            port:
              number: 80
  - host: api.agromart.com
    http:
      paths:
      - path: /
        pathType: Prefix
        backend:
          service:
            name: backend-service
            port:
              number: 8080
```

```bash
kubectl apply -f ingress.yaml

# Get load balancer URL
kubectl get ingress vmv-agromart-ingress -n vmv-agromart-prod
```

---

## 7. Mobile App Deployment

### 7.1 Android App Deployment

#### Build APK

```bash
cd mobile-app

# Update environment for production
cat > src/environments/environment.prod.ts << 'EOF'
export const environment = {
  production: true,
  apiUrl: 'https://api.agromart.com/api'
};
EOF

# Build for production
ionic build --prod

# Add Android platform
npx cap add android

# Sync
npx cap sync android

# Open in Android Studio
npx cap open android
```

#### In Android Studio:

1. **Update build.gradle (app level)**:
```gradle
android {
    defaultConfig {
        applicationId "com.vmv.agromart"
        versionCode 1
        versionName "1.0.0"
    }

    signingConfigs {
        release {
            storeFile file("keystore/release.keystore")
            storePassword System.getenv("KEYSTORE_PASSWORD")
            keyAlias "vmv-agromart"
            keyPassword System.getenv("KEY_PASSWORD")
        }
    }

    buildTypes {
        release {
            minifyEnabled true
            shrinkResources true
            proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'), 'proguard-rules.pro'
            signingConfig signingConfigs.release
        }
    }
}
```

2. **Generate Signed APK**:
   - Build → Generate Signed Bundle / APK
   - Choose APK
   - Create new keystore (save it securely!)
   - Fill in details
   - Build release APK

3. **Output**: `app/release/app-release.apk`

#### Deploy to Google Play Store

```bash
# 1. Create Google Play Console Account
# https://play.google.com/console

# 2. Create New App
# - Fill app details
# - Upload screenshots
# - Add description
# - Set pricing (Free/Paid)

# 3. Upload APK
# - Production → Create new release
# - Upload app-release.apk
# - Fill release notes
# - Review and roll out

# 4. Submit for Review
# Usually takes 1-3 days
```

---

### 7.2 iOS App Deployment

#### Build IPA

```bash
cd mobile-app

# Build for production
ionic build --prod

# Add iOS platform
npx cap add ios

# Sync
npx cap sync ios

# Open in Xcode
npx cap open ios
```

#### In Xcode:

1. **Configure Project**:
   - Select project in navigator
   - General → Identity
   - Bundle Identifier: com.vmv.agromart
   - Version: 1.0.0
   - Build: 1

2. **Signing & Capabilities**:
   - Team: Select your Apple Developer Team
   - Signing Certificate: Distribution
   - Provisioning Profile: App Store

3. **Build Settings**:
   - Product → Scheme → Edit Scheme
   - Run → Build Configuration → Release
   - Archive

4. **Create Archive**:
   - Product → Archive
   - Wait for build to complete
   - Organizer window opens

5. **Upload to App Store Connect**:
   - Select archive
   - Distribute App → App Store Connect
   - Upload
   - Fill metadata

#### Deploy to Apple App Store

```bash
# 1. Create App Store Connect Account
# https://appstoreconnect.apple.com

# 2. Create New App
# - My Apps → + → New App
# - Platform: iOS
# - Name: VMV AgroMart
# - Primary Language: English
# - Bundle ID: com.vmv.agromart
# - SKU: vmv-agromart-ios

# 3. Fill App Information
# - Screenshots (iPhone, iPad)
# - Description
# - Keywords
# - Support URL
# - Marketing URL
# - Privacy Policy URL

# 4. Pricing and Availability
# - Price: Free
# - Availability: All countries

# 5. Submit for Review
# - App Review Information
# - Version Release: Manual
# - Submit
# Usually takes 1-5 days
```

---

## 8. Domain & SSL

### 8.1 Request SSL Certificate

```bash
# Request certificate in ACM
aws acm request-certificate \
    --domain-name agromart.com \
    --subject-alternative-names www.agromart.com api.agromart.com admin.agromart.com \
    --validation-method DNS \
    --region us-east-1

# Note the Certificate ARN
# arn:aws:acm:us-east-1:ACCOUNT_ID:certificate/CERT_ID
```

### 8.2 Configure Route 53

```bash
# Create hosted zone
aws route53 create-hosted-zone \
    --name agromart.com \
    --caller-reference $(date +%s)

# Get load balancer URL from ingress
kubectl get ingress vmv-agromart-ingress -n vmv-agromart-prod -o jsonpath='{.status.loadBalancer.ingress[0].hostname}'

# Create DNS records
cat > dns-records.json << 'EOF'
{
  "Changes": [
    {
      "Action": "CREATE",
      "ResourceRecordSet": {
        "Name": "agromart.com",
        "Type": "A",
        "AliasTarget": {
          "HostedZoneId": "Z35SXDOTRQ7X7K",
          "DNSName": "k8s-vmvagrom-xxxxx.us-east-1.elb.amazonaws.com",
          "EvaluateTargetHealth": false
        }
      }
    },
    {
      "Action": "CREATE",
      "ResourceRecordSet": {
        "Name": "api.agromart.com",
        "Type": "A",
        "AliasTarget": {
          "HostedZoneId": "Z35SXDOTRQ7X7K",
          "DNSName": "k8s-vmvagrom-xxxxx.us-east-1.elb.amazonaws.com",
          "EvaluateTargetHealth": false
        }
      }
    }
  ]
}
EOF

aws route53 change-resource-record-sets \
    --hosted-zone-id HOSTED_ZONE_ID \
    --change-batch file://dns-records.json
```

---

## 9. Monitoring & Logging

### 9.1 Install Prometheus & Grafana

```bash
# Add Helm repos
helm repo add prometheus-community https://prometheus-community.github.io/helm-charts
helm repo add grafana https://grafana.github.io/helm-charts
helm repo update

# Create monitoring namespace
kubectl create namespace monitoring

# Install Prometheus
helm install prometheus prometheus-community/kube-prometheus-stack \
  --namespace monitoring \
  --set prometheus.prometheusSpec.serviceMonitorSelectorNilUsesHelmValues=false

# Install Grafana
helm install grafana grafana/grafana \
  --namespace monitoring \
  --set persistence.enabled=true \
  --set persistence.size=10Gi \
  --set adminPassword='YOUR_STRONG_PASSWORD'

# Get Grafana URL
kubectl get svc -n monitoring grafana -o jsonpath='{.status.loadBalancer.ingress[0].hostname}'

# Get Grafana admin password
kubectl get secret --namespace monitoring grafana -o jsonpath="{.data.admin-password}" | base64 --decode
```

### 9.2 CloudWatch Logs

```bash
# Install FluentBit
kubectl apply -f https://raw.githubusercontent.com/aws-samples/amazon-cloudwatch-container-insights/latest/k8s-deployment-manifest-templates/deployment-mode/daemonset/container-insights-monitoring/quickstart/cwagent-fluentd-quickstart.yaml

# Verify
kubectl get pods -n amazon-cloudwatch
```

---

## 10. CI/CD Pipeline

Create `.github/workflows/deploy.yml`:

```yaml
name: Deploy to EKS

on:
  push:
    branches: [ main ]

env:
  AWS_REGION: us-east-1
  EKS_CLUSTER: vmv-agromart-prod
  ECR_REGISTRY: ${{ secrets.AWS_ACCOUNT_ID }}.dkr.ecr.us-east-1.amazonaws.com

jobs:
  build-and-deploy:
    runs-on: ubuntu-latest

    steps:
    - name: Checkout code
      uses: actions/checkout@v3

    - name: Configure AWS credentials
      uses: aws-actions/configure-aws-credentials@v2
      with:
        aws-access-key-id: ${{ secrets.AWS_ACCESS_KEY_ID }}
        aws-secret-access-key: ${{ secrets.AWS_SECRET_ACCESS_KEY }}
        aws-region: ${{ env.AWS_REGION }}

    - name: Login to Amazon ECR
      id: login-ecr
      uses: aws-actions/amazon-ecr-login@v1

    - name: Build, tag, and push backend image
      working-directory: ./backend
      run: |
        docker build -t $ECR_REGISTRY/vmv-agromart/backend:$GITHUB_SHA .
        docker push $ECR_REGISTRY/vmv-agromart/backend:$GITHUB_SHA

    - name: Build, tag, and push frontend image
      working-directory: ./web-frontend
      run: |
        docker build -t $ECR_REGISTRY/vmv-agromart/web-frontend:$GITHUB_SHA .
        docker push $ECR_REGISTRY/vmv-agromart/web-frontend:$GITHUB_SHA

    - name: Update kube config
      run: aws eks update-kubeconfig --name $EKS_CLUSTER --region $AWS_REGION

    - name: Deploy to EKS
      run: |
        kubectl set image deployment/backend backend=$ECR_REGISTRY/vmv-agromart/backend:$GITHUB_SHA -n vmv-agromart-prod
        kubectl set image deployment/web-frontend web-frontend=$ECR_REGISTRY/vmv-agromart/web-frontend:$GITHUB_SHA -n vmv-agromart-prod
        kubectl rollout status deployment/backend -n vmv-agromart-prod
        kubectl rollout status deployment/web-frontend -n vmv-agromart-prod
```

---

## 11. Scaling & Performance

### 11.1 Horizontal Pod Autoscaler

Create `hpa.yaml`:

```yaml
apiVersion: autoscaling/v2
kind: HorizontalPodAutoscaler
metadata:
  name: backend-hpa
  namespace: vmv-agromart-prod
spec:
  scaleTargetRef:
    apiVersion: apps/v1
    kind: Deployment
    name: backend
  minReplicas: 3
  maxReplicas: 10
  metrics:
  - type: Resource
    resource:
      name: cpu
      target:
        type: Utilization
        averageUtilization: 70
  - type: Resource
    resource:
      name: memory
      target:
        type: Utilization
        averageUtilization: 80
---
apiVersion: autoscaling/v2
kind: HorizontalPodAutoscaler
metadata:
  name: web-frontend-hpa
  namespace: vmv-agromart-prod
spec:
  scaleTargetRef:
    apiVersion: apps/v1
    kind: Deployment
    name: web-frontend
  minReplicas: 2
  maxReplicas: 6
  metrics:
  - type: Resource
    resource:
      name: cpu
      target:
        type: Utilization
        averageUtilization: 70
```

```bash
kubectl apply -f hpa.yaml
```

---

## 12. Backup & Disaster Recovery

### 12.1 Database Backups

```bash
# Automated RDS backups (if using RDS)
aws rds modify-db-instance \
    --db-instance-identifier vmv-agromart-db \
    --backup-retention-period 7 \
    --preferred-backup-window "03:00-04:00"

# Manual backup script
cat > backup-db.sh << 'EOF'
#!/bin/bash
DATE=$(date +%Y%m%d_%H%M%S)
kubectl exec -n vmv-agromart-prod mysql-0 -- \
  mysqldump -u root -p$MYSQL_ROOT_PASSWORD vmv_agromart | \
  gzip > backup_${DATE}.sql.gz
aws s3 cp backup_${DATE}.sql.gz s3://vmv-agromart-backups/db/
EOF

chmod +x backup-db.sh
```

### 12.2 Velero for Kubernetes Backups

```bash
# Install Velero
helm repo add vmware-tanzu https://vmware-tanzu.github.io/helm-charts
helm install velero vmware-tanzu/velero \
  --namespace velero \
  --create-namespace \
  --set configuration.provider=aws \
  --set configuration.backupStorageLocation.bucket=vmv-agromart-velero-backups \
  --set configuration.backupStorageLocation.config.region=us-east-1 \
  --set snapshotsEnabled=true

# Create backup schedule
velero schedule create daily-backup --schedule="0 2 * * *"
```

---

## 🎯 Production Deployment Checklist

### Pre-Deployment
- [ ] AWS account configured
- [ ] Domain purchased and configured
- [ ] SSL certificate requested and validated
- [ ] All secrets created
- [ ] Docker images built and pushed
- [ ] Database initialized with schema

### Deployment
- [ ] EKS cluster created
- [ ] All services deployed
- [ ] Ingress configured
- [ ] DNS records created
- [ ] SSL working (https://)
- [ ] Health checks passing

### Testing
- [ ] Web app accessible
- [ ] API responding
- [ ] Mobile apps connecting to API
- [ ] OTP working
- [ ] Payments working
- [ ] Order flow working

### Post-Deployment
- [ ] Monitoring setup (Grafana)
- [ ] Logging configured (CloudWatch)
- [ ] Alerts configured
- [ ] Backups scheduled
- [ ] CI/CD pipeline working
- [ ] Documentation updated

### Mobile Apps
- [ ] Android APK built
- [ ] iOS IPA built
- [ ] Google Play Store submission
- [ ] Apple App Store submission
- [ ] Apps approved and published

---

## 💰 Estimated AWS Costs

### Monthly Production Costs:

| Service | Configuration | Est. Cost |
|---------|---------------|-----------|
| **EKS Cluster** | Control plane | $72 |
| **EC2 (Nodes)** | 5x t3.medium | $180 |
| **RDS MySQL** | db.t3.medium Multi-AZ | $140 |
| **ElastiCache** | cache.t3.medium | $70 |
| **ALB** | 1 load balancer | $25 |
| **S3** | 100GB storage | $5 |
| **CloudWatch** | Logs + Metrics | $30 |
| **Data Transfer** | 500GB/month | $45 |
| **Route 53** | Hosted zone + queries | $5 |
| **ECR** | Container registry | $5 |
| **Backups** | S3 + Snapshots | $20 |
| **Total** | | **~$600/month** |

### Cost Optimization:
- Use Spot Instances for worker nodes (-70%)
- Reserved Instances for predictable workloads (-40%)
- Optimize container images (smaller = cheaper)
- Implement caching (reduce compute)

---

## 🚀 Quick Deploy Commands

### Complete deployment script:

```bash
#!/bin/bash
# complete-deploy.sh

set -e

echo "🚀 Starting VMV AgroMart Production Deployment..."

# 1. Create EKS cluster
echo "📦 Creating EKS cluster..."
eksctl create cluster -f eks-cluster-config.yaml

# 2. Build and push images
echo "🐳 Building Docker images..."
./build-and-push.sh v1.0.0

# 3. Create namespace and secrets
echo "🔐 Creating secrets..."
kubectl create namespace vmv-agromart-prod
# ... create all secrets ...

# 4. Deploy services
echo "🎯 Deploying services..."
kubectl apply -f configmap.yaml
kubectl apply -f mysql-statefulset.yaml
kubectl apply -f redis-statefulset.yaml
kubectl apply -f backend-deployment.yaml
kubectl apply -f web-frontend-deployment.yaml
kubectl apply -f ingress.yaml
kubectl apply -f hpa.yaml

# 5. Wait for deployments
echo "⏳ Waiting for deployments..."
kubectl wait --for=condition=available --timeout=600s \
  deployment/backend deployment/web-frontend -n vmv-agromart-prod

# 6. Get load balancer URL
echo "✅ Deployment complete!"
echo "🌐 Load Balancer URL:"
kubectl get ingress vmv-agromart-ingress -n vmv-agromart-prod

echo "📱 Update your DNS records to point to this URL"
echo "🎉 Done!"
```

```bash
chmod +x complete-deploy.sh
./complete-deploy.sh
```

---

## 📞 Support & Next Steps

### After Deployment:

1. **Update DNS** - Point domain to load balancer
2. **Test everything** - Web, Mobile, API
3. **Monitor** - Check Grafana dashboards
4. **Scale** - Adjust replicas as needed
5. **Optimize** - Monitor costs and performance

### Useful Commands:

```bash
# Check pod status
kubectl get pods -n vmv-agromart-prod

# View logs
kubectl logs -f deployment/backend -n vmv-agromart-prod

# Scale deployment
kubectl scale deployment backend --replicas=5 -n vmv-agromart-prod

# Update image
kubectl set image deployment/backend backend=NEW_IMAGE -n vmv-agromart-prod

# Rollback
kubectl rollout undo deployment/backend -n vmv-agromart-prod
```

---

**🎉 Your VMV AgroMart application is now production-ready on AWS EKS!**

**Web App:** https://agromart.com
**API:** https://api.agromart.com
**Android:** Google Play Store
**iOS:** Apple App Store

---

© 2026 VMV AgroMart - EKS Production Deployment Guide
