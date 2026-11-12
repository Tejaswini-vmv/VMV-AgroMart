#!/bin/bash

# ============================================================================
# VMV AgroMart - One-Command Setup Script
# ============================================================================

set -e

echo "================================"
echo "VMV AgroMart - Quick Setup"
echo "================================"
echo ""

# Step 1: Copy database initialization files
echo "[1/5] Setting up database..."
mkdir -p database/init
cp vmv-agromart-schema.sql database/init/01-schema.sql
cp vmv-agromart-dummy-data.sql database/init/02-dummy-data.sql

# Step 2: Copy configuration files
echo "[2/5] Copying configuration files..."
mkdir -p backend/src/main/resources
cp application-testing.yaml backend/src/main/resources/
cp application-prod.yaml backend/src/main/resources/

# Step 3: Copy Dockerfiles
echo "[3/5] Setting up Docker configurations..."
cp backend-Dockerfile backend/Dockerfile
cp web-frontend-Dockerfile web-frontend/Dockerfile
cp mobile-app-Dockerfile mobile-app/Dockerfile
cp web-frontend-nginx.conf web-frontend/nginx.conf

# Step 4: Setup environment
echo "[4/5] Creating environment file..."
if [ ! -f .env ]; then
    cp .env.example .env
    echo "✓ .env file created"
    echo "⚠ IMPORTANT: Edit .env file with your credentials!"
else
    echo "✓ .env file already exists"
fi

# Step 5: Start services
echo "[5/5] Starting Docker services..."
chmod +x start.sh
docker-compose up -d

echo ""
echo "================================"
echo "✓ Setup Complete!"
echo "================================"
echo ""
echo "Access Points:"
echo "  Web:     http://localhost"
echo "  API:     http://localhost:8080/api"
echo "  Mobile:  http://localhost:8100"
echo "  DB Admin: http://localhost:8081"
echo ""
echo "Test Login (any role):"
echo "  Super Admin:  +919876543210"
echo "  City Admin:   +919823456701"
echo "  Farmer:       +919823456901"
echo "  Retailer:     +919823457001"
echo "  Customer:     +919823458001"
echo ""
echo "Note: OTP is 123456 for testing"
echo ""
echo "View logs: docker-compose logs -f"
echo "Stop services: docker-compose down"
echo ""
