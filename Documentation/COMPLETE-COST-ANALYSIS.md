# VMV AgroMart - Complete 1 Year Cost Analysis

## 💰 Total Cost Breakdown for AWS EKS Production Deployment

---

## 📊 QUICK SUMMARY

### **Total First Year Cost:**

| Scenario | Setup Cost | Monthly Cost | Annual Cost | **Total Year 1** |
|----------|------------|--------------|-------------|------------------|
| **Basic** | $136 | $250-300 | $3,000-3,600 | **$3,136-3,736** |
| **Standard** | $136 | $417 | $5,004 | **$5,140** |
| **Enterprise** | $136 | $800-1,000 | $9,600-12,000 | **$9,736-12,136** |

### **Recommended: Standard Setup = ~$5,140/year (~₹4,28,000/year)**

---

## 💵 DETAILED COST BREAKDOWN

### **1. ONE-TIME COSTS (Year 1 Only)**

| Item | Cost | When | Notes |
|------|------|------|-------|
| **Domain Registration** | $12 | Month 1 | .com domain (GoDaddy/Namecheap) |
| **SSL Certificate** | $0 | Month 1 | Free with AWS ACM |
| **Google Play Developer** | $25 | Month 3 | One-time, lifetime |
| **Apple Developer Program** | $99 | Month 3 | Annual renewal required |
| **Initial Setup Labor** | $0-500 | Month 1 | If doing yourself = $0 |
| **Total One-Time** | **$136** | | **₹11,356** |

---

### **2. AWS MONTHLY COSTS (Standard Setup)**

#### **Compute Resources**

| Service | Configuration | Cost/Month | Cost/Year |
|---------|---------------|------------|-----------|
| **EKS Control Plane** | 1 cluster | $72 | $864 |
| **EC2 Worker Nodes** | 3x t3.medium (24/7) | $110 | $1,320 |
| **EKS Data Transfer** | 100GB/month | $9 | $108 |
| **Subtotal Compute** | | **$191** | **$2,292** |

#### **Database & Storage**

| Service | Configuration | Cost/Month | Cost/Year |
|---------|---------------|------------|-----------|
| **RDS MySQL** | db.t3.medium Multi-AZ | $140 | $1,680 |
| **RDS Storage** | 100GB GP3 | $12 | $144 |
| **RDS Backups** | 7 days retention | $5 | $60 |
| **ElastiCache Redis** | cache.t3.medium | $70 | $840 |
| **EBS Volumes** | 150GB for pods | $15 | $180 |
| **S3 Storage** | 100GB files | $2.30 | $27.60 |
| **Subtotal Storage** | | **$244.30** | **$2,931.60** |

#### **Networking**

| Service | Configuration | Cost/Month | Cost/Year |
|---------|---------------|------------|-----------|
| **Application Load Balancer** | 1 ALB | $22.50 | $270 |
| **ALB LCU Hours** | ~50 LCUs | $5 | $60 |
| **Data Transfer Out** | 500GB/month | $45 | $540 |
| **Route 53** | 1 hosted zone + queries | $0.50 | $6 |
| **Subtotal Networking** | | **$73** | **$876** |

#### **Container Registry & Logs**

| Service | Configuration | Cost/Month | Cost/Year |
|---------|---------------|------------|-----------|
| **ECR Storage** | 10GB images | $1 | $12 |
| **CloudWatch Logs** | 20GB/month | $10 | $120 |
| **CloudWatch Metrics** | Custom metrics | $5 | $60 |
| **Subtotal Monitoring** | | **$16** | **$192** |

#### **Backups & Security**

| Service | Configuration | Cost/Month | Cost/Year |
|---------|---------------|------------|-----------|
| **S3 Backups** | 50GB database backups | $1.15 | $13.80 |
| **AWS Backup** | Automated backups | $5 | $60 |
| **Secrets Manager** | 10 secrets | $4 | $48 |
| **Subtotal Backup** | | **$10.15** | **$121.80** |

---

### **3. TOTAL AWS MONTHLY COSTS**

| Category | Monthly | Annual |
|----------|---------|--------|
| Compute | $191 | $2,292 |
| Storage | $244.30 | $2,931.60 |
| Networking | $73 | $876 |
| Monitoring | $16 | $192 |
| Backup | $10.15 | $121.80 |
| **Total AWS** | **$534.45** | **$6,413.40** |

**With 20% buffer for overages:** **~$417-534/month** or **~$5,004-6,408/year**

---

### **4. THIRD-PARTY SERVICES (Annual)**

| Service | Purpose | Cost/Month | Cost/Year | Required? |
|---------|---------|------------|-----------|-----------|
| **Twilio** | SMS/OTP (5,000 SMS) | $50 | $600 | ✅ Yes |
| **Razorpay** | Payment Gateway (2% fee) | Variable | $0-500 | ✅ Yes |
| **Google Maps API** | Location services | $20 | $240 | ✅ Yes |
| **Email Service** | SendGrid/SES | $10 | $120 | ⚠️ Optional |
| **Monitoring** | UptimeRobot/Pingdom | $0-10 | $0-120 | ⚠️ Optional |
| **Error Tracking** | Sentry | $0-26 | $0-312 | ⚠️ Optional |
| **Total 3rd Party** | | **$80-116** | **$960-1,392** |

---

### **5. RECURRING ANNUAL COSTS (Year 2+)**

| Item | Cost | Frequency |
|------|------|-----------|
| **Domain Renewal** | $12 | Annual |
| **Apple Developer** | $99 | Annual |
| **Google Play** | $0 | One-time only |
| **Total Annual** | **$111** | |

---

## 📊 COMPLETE 1-YEAR COST BREAKDOWN

### **Standard Setup (Recommended)**

```
ONE-TIME COSTS (Month 1):
├─ Domain registration           $12
├─ Google Play fee               $25
├─ Apple Developer               $99
└─ Total One-Time               $136

AWS COSTS (Monthly × 12):
├─ EKS + EC2                    $191/mo × 12 = $2,292
├─ Database & Storage           $244/mo × 12 = $2,931
├─ Networking                   $73/mo × 12 = $876
├─ Monitoring & Logs            $16/mo × 12 = $192
├─ Backups                      $10/mo × 12 = $122
└─ Total AWS (12 months)                    $6,413

THIRD-PARTY SERVICES (Monthly × 12):
├─ Twilio (SMS)                 $50/mo × 12 = $600
├─ Google Maps                  $20/mo × 12 = $240
├─ Razorpay fees                Variable    = $300
└─ Total 3rd Party (12 months)              $1,140

═══════════════════════════════════════════════
GRAND TOTAL YEAR 1:                         $7,689
═══════════════════════════════════════════════

In Indian Rupees (@ ₹83.50/USD):          ₹6,42,124
```

---

## 💡 COST OPTIMIZATION OPTIONS

### **Option 1: Basic Setup (Budget-Friendly)**

**Changes:**
- 2x t3.small nodes instead of 3x t3.medium
- Single-AZ RDS (no Multi-AZ)
- cache.t3.small Redis
- Reduced data transfer

**Savings:**

| Item | Standard | Optimized | Savings |
|------|----------|-----------|---------|
| EC2 Nodes | $110/mo | $42/mo | $68/mo |
| RDS MySQL | $140/mo | $75/mo | $65/mo |
| Redis | $70/mo | $35/mo | $35/mo |
| **Total/Month** | **$534/mo** | **$300/mo** | **$234/mo** |
| **Total/Year** | **$6,413** | **$3,600** | **$2,813** |

**Year 1 Total:** $136 + $3,600 + $1,140 = **$4,876** (₹4,07,000)

⚠️ **Trade-offs:**
- Lower reliability (Single-AZ)
- Less performance
- Limited scaling
- Not recommended for production

---

### **Option 2: Spot Instances (60-70% Savings)**

**Use Spot Instances for worker nodes:**

| Item | On-Demand | Spot | Savings |
|------|-----------|------|---------|
| 3x t3.medium | $110/mo | $33/mo | $77/mo |
| **Annual Savings** | | | **$924/year** |

**Year 1 Total:** $136 + ($5,489) + $1,140 = **$6,765** (₹5,64,000)

⚠️ **Trade-off:** Nodes can be terminated with 2-min notice

---

### **Option 3: Reserved Instances (40% Savings)**

**Buy 1-year Reserved Instances:**

| Item | On-Demand | Reserved | Savings |
|------|-----------|----------|---------|
| 3x t3.medium | $110/mo | $66/mo | $44/mo |
| db.t3.medium | $140/mo | $84/mo | $56/mo |
| **Annual Savings** | | | **$1,200/year** |

**Year 1 Total:** $136 + $5,213 + $1,140 = **$6,489** (₹5,41,000)

✅ **Best option:** Save 20% with no downtime risk!

---

### **Option 4: Aggressive Optimization**

**Combine all optimizations:**
- Spot Instances (70% off compute)
- Reserved RDS (40% off database)
- Reduced tier (t3.small)
- Single-AZ RDS
- Lower data transfer

**Result:**

| Item | Monthly | Annual |
|------|---------|--------|
| AWS | $200 | $2,400 |
| 3rd Party | $95 | $1,140 |
| **Total** | **$295/mo** | **$3,540/year** |

**Year 1 Total:** $136 + $3,540 = **$3,676** (₹3,07,000)

⚠️ **Trade-offs:**
- Reduced reliability
- Limited performance
- Risk of spot termination
- Not ideal for production

---

## 📈 COST SCALING BY USERS

### **Estimated costs as user base grows:**

| Users | Orders/Month | AWS Cost | 3rd Party | Total/Month |
|-------|--------------|----------|-----------|-------------|
| **100** | 500 | $300 | $80 | **$380** |
| **500** | 2,500 | $417 | $120 | **$537** |
| **1,000** | 5,000 | $550 | $160 | **$710** |
| **5,000** | 25,000 | $900 | $300 | **$1,200** |
| **10,000** | 50,000 | $1,500 | $500 | **$2,000** |

### **Revenue vs Cost (Projection)**

Assuming ₹50 average order value, 10% platform fee:

| Users | Orders/Mo | Revenue/Mo | Cost/Mo | **Profit/Mo** |
|-------|-----------|------------|---------|---------------|
| 100 | 500 | ₹2,500 | ₹31,700 | **-₹29,200** |
| 500 | 2,500 | ₹12,500 | ₹44,800 | **-₹32,300** |
| 1,000 | 5,000 | ₹25,000 | ₹59,300 | **-₹34,300** |
| 5,000 | 25,000 | ₹1,25,000 | ₹1,00,000 | **+₹25,000** ✅ |
| 10,000 | 50,000 | ₹2,50,000 | ₹1,67,000 | **+₹83,000** ✅ |

**Break-even:** ~4,000-5,000 users

---

## 💸 ALTERNATIVE DEPLOYMENT OPTIONS

### **Option A: AWS Lightsail (Cheaper, Simpler)**

| Service | Config | Cost/Month | Cost/Year |
|---------|--------|------------|-----------|
| Lightsail Instance | $40 plan | $40 | $480 |
| Database | $30 plan | $30 | $360 |
| Load Balancer | $18 | $18 | $216 |
| Static IP | Free | $0 | $0 |
| **Total AWS** | | **$88/mo** | **$1,056** |
| 3rd Party | | $95/mo | $1,140 |
| **Grand Total** | | **$183/mo** | **$2,196** |

**Year 1 Total:** $136 + $2,196 = **$2,332** (₹1,94,700)

✅ **Pros:**
- Much cheaper
- Simpler management
- Predictable costs
- Good for <1,000 users

❌ **Cons:**
- Limited scaling
- Less features
- Manual scaling needed
- Not enterprise-grade

---

### **Option B: DigitalOcean Kubernetes**

| Service | Config | Cost/Month | Cost/Year |
|---------|--------|------------|-----------|
| Kubernetes Cluster | 3 nodes | $120 | $1,440 |
| Managed Database | 4GB RAM | $60 | $720 |
| Managed Redis | 1GB RAM | $15 | $180 |
| Load Balancer | 1 LB | $10 | $120 |
| **Total DO** | | **$205/mo** | **$2,460** |
| 3rd Party | | $95/mo | $1,140 |
| **Grand Total** | | **$300/mo** | **$3,600** |

**Year 1 Total:** $136 + $3,600 = **$3,736** (₹3,12,000)

✅ **Pros:**
- Cheaper than AWS
- Simpler than EKS
- Good for <5,000 users

❌ **Cons:**
- Less scalable than AWS
- Fewer features
- Smaller ecosystem

---

### **Option C: Shared Hosting (Cheapest)**

| Service | Config | Cost/Month | Cost/Year |
|---------|--------|------------|-----------|
| VPS | 8GB RAM, 4 CPU | $40 | $480 |
| Domain | .com | $1 | $12 |
| SSL | Let's Encrypt | $0 | $0 |
| **Total Hosting** | | **$41/mo** | **$492** |
| 3rd Party | | $95/mo | $1,140 |
| **Grand Total** | | **$136/mo** | **$1,632** |

**Year 1 Total:** $136 + $1,632 = **$1,768** (₹1,47,600)

✅ **Pros:**
- Very cheap
- Simple setup
- Good for MVP/testing

❌ **Cons:**
- No auto-scaling
- Limited resources
- Manual management
- Not production-ready for 1000+ users

---

## 🎯 RECOMMENDATIONS BY STAGE

### **Stage 1: MVP/Testing (0-100 users)**
**Best Option:** Shared VPS or AWS Lightsail
- **Cost:** $136 + $1,632 = **$1,768/year** (₹1,48,000)
- **When:** Building MVP, need to validate idea
- **Pros:** Cheap, simple, fast to deploy

### **Stage 2: Launch (100-1,000 users)**
**Best Option:** DigitalOcean Kubernetes
- **Cost:** $136 + $3,600 = **$3,736/year** (₹3,12,000)
- **When:** Launching to public, growing user base
- **Pros:** Good balance of cost and features

### **Stage 3: Growth (1,000-10,000 users)**
**Best Option:** AWS EKS (Standard)
- **Cost:** $136 + $6,413 = **$6,549/year** (₹5,47,000)
- **When:** Proven product, scaling rapidly
- **Pros:** Enterprise-ready, auto-scaling, reliable

### **Stage 4: Scale (10,000+ users)**
**Best Option:** AWS EKS (Optimized with Reserved Instances)
- **Cost:** $136 + $5,213 = **$5,349/year** (₹4,46,600)
- **When:** Established business, predictable traffic
- **Pros:** Cost-optimized, highly scalable

---

## 📊 FINAL RECOMMENDATION

### **For Your Case (VMV AgroMart):**

**Recommended Path:**

**Phase 1 (Month 1-3): AWS Lightsail**
- Cost: $183/month = **$549 for 3 months**
- Launch quickly
- Validate product-market fit
- Get first 100-500 users

**Phase 2 (Month 4-12): AWS EKS**
- Cost: $417/month = **$3,753 for 9 months**
- Migrate when you hit 500+ users
- Scale confidently
- Enterprise-ready

**Total Year 1:**
- Setup: $136
- Phase 1 (3 mo): $549
- Phase 2 (9 mo): $3,753
- **Total: $4,438** (₹3,70,600)

**Benefits:**
- ✅ Start cheap, scale when needed
- ✅ Don't over-invest initially
- ✅ Smooth migration path
- ✅ Save ~$2,500 in first year

---

## 💰 SUMMARY TABLE

| Deployment Option | Year 1 Cost | Best For | Recommendation |
|-------------------|-------------|----------|----------------|
| **Shared VPS** | $1,768 (₹1.5L) | MVP, Testing | ⚠️ Not scalable |
| **AWS Lightsail** | $2,332 (₹1.9L) | 0-1K users | ✅ Great for launch |
| **DigitalOcean K8s** | $3,736 (₹3.1L) | 1K-5K users | ✅ Good middle ground |
| **AWS EKS (Basic)** | $4,876 (₹4.1L) | 1K-5K users | ⚠️ Budget option |
| **AWS EKS (Standard)** | $6,549 (₹5.5L) | 5K+ users | ✅ **Recommended** |
| **AWS EKS (Optimized)** | $5,349 (₹4.5L) | 5K+ users | ✅ Best value |
| **Hybrid Approach** | $4,438 (₹3.7L) | All stages | ✅ **Best strategy** |

---

## ✅ FINAL ANSWER

### **For 1 Year, Complete Production Deployment:**

**Standard AWS EKS Setup:**
```
One-time costs:     $136
AWS (12 months):    $6,413
3rd party services: $1,140
─────────────────────────
TOTAL YEAR 1:       $7,689

In Indian Rupees:   ₹6,42,000
Monthly average:    ₹53,500
```

**Optimized AWS EKS (Recommended):**
```
One-time costs:     $136
AWS optimized:      $5,213
3rd party services: $1,140
─────────────────────────
TOTAL YEAR 1:       $6,489

In Indian Rupees:   ₹5,41,000
Monthly average:    ₹45,000
```

**Hybrid Approach (Best for Startups):**
```
One-time costs:     $136
Lightsail (3 mo):   $549
AWS EKS (9 mo):     $3,753
─────────────────────────
TOTAL YEAR 1:       $4,438

In Indian Rupees:   ₹3,70,000
Monthly average:    ₹31,000
```

---

## 🎯 MY RECOMMENDATION

**Start with AWS Lightsail ($183/month) for first 3 months**
**Then migrate to AWS EKS when you hit 500+ users**

**Total Year 1 Cost: ₹3,70,000 (~$4,400)**

This gives you:
- ✅ Low initial investment
- ✅ Scalability when needed
- ✅ Professional production setup
- ✅ Mobile apps on both stores
- ✅ Room to grow

---

**Questions? Need clarification on any cost component?**

I can provide more detailed breakdowns or suggest further optimizations based on your specific needs!

© 2026 VMV AgroMart - Cost Analysis
