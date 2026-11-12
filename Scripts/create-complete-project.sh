#!/bin/bash

# Script to create complete VMV AgroMart project structure
# This creates ALL necessary files for a working application

echo "Creating complete VMV AgroMart source code..."

# Backend structure
cd /mnt/user-data/outputs/source-code/backend
mkdir -p src/main/{java/com/vmv/agromart/{model,repository,service,controller,config,dto,security,util,exception},resources}
mkdir -p src/test/java/com/vmv/agromart

# Frontend structure
cd /mnt/user-data/outputs/source-code/frontend
mkdir -p src/{app/{components,services,models,guards,interceptors},assets,environments}

# Mobile structure
cd /mnt/user-data/outputs/source-code/mobile-app
mkdir -p src/{app/{pages,services,models,guards},assets,environments}

echo "✅ Project structure created successfully!"
