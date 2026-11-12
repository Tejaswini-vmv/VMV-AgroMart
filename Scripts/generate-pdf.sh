#!/bin/bash

# ============================================================================
# VMV AgroMart - PDF Generation Script
# Converts markdown documentation to professional PDFs
# ============================================================================

echo "=================================="
echo "VMV AgroMart PDF Generator"
echo "=================================="
echo ""

# Check if pandoc is installed
if ! command -v pandoc &> /dev/null; then
    echo "❌ Pandoc is not installed!"
    echo ""
    echo "Install Pandoc:"
    echo "  Ubuntu/Debian: sudo apt install pandoc texlive-xetex"
    echo "  macOS: brew install pandoc basictex"
    echo "  Windows: Download from https://pandoc.org/installing.html"
    echo ""
    exit 1
fi

echo "✅ Pandoc found: $(pandoc --version | head -n1)"
echo ""

# Create output directory
mkdir -p pdfs

# Generate PDFs
echo "📄 Generating PDFs..."
echo ""

# 1. Complete User Manual
echo "  [1/3] Creating Complete User Manual..."
pandoc COMPLETE-USER-MANUAL.md \
    -o pdfs/VMV-AgroMart-User-Manual.pdf \
    --pdf-engine=xelatex \
    --toc \
    --toc-depth=3 \
    --number-sections \
    --highlight-style=tango \
    --variable=geometry:margin=1in \
    --variable=fontsize=11pt \
    --variable=documentclass=report \
    --metadata title="VMV AgroMart - Complete User Manual" \
    --metadata author="VMV AgroMart Team" \
    --metadata date="$(date +%Y-%m-%d)" \
    2>/dev/null && echo "     ✅ Generated: pdfs/VMV-AgroMart-User-Manual.pdf" || echo "     ❌ Failed"

# 2. Mobile Testing Guide
echo "  [2/3] Creating Mobile App Testing Guide..."
pandoc MOBILE-APP-TESTING-GUIDE.md \
    -o pdfs/Mobile-App-Testing-Guide.pdf \
    --pdf-engine=xelatex \
    --toc \
    --toc-depth=2 \
    --number-sections \
    --highlight-style=tango \
    --variable=geometry:margin=1in \
    --variable=fontsize=11pt \
    --metadata title="VMV AgroMart - Mobile App Testing Guide" \
    --metadata author="VMV AgroMart Team" \
    --metadata date="$(date +%Y-%m-%d)" \
    2>/dev/null && echo "     ✅ Generated: pdfs/Mobile-App-Testing-Guide.pdf" || echo "     ❌ Failed"

# 3. Deployment Guide
echo "  [3/3] Creating Deployment Guide..."
pandoc DOCKER-DEPLOYMENT-GUIDE.md \
    -o pdfs/Deployment-Guide.pdf \
    --pdf-engine=xelatex \
    --toc \
    --toc-depth=2 \
    --number-sections \
    --highlight-style=tango \
    --variable=geometry:margin=1in \
    --variable=fontsize=11pt \
    --metadata title="VMV AgroMart - Deployment Guide" \
    --metadata author="VMV AgroMart Team" \
    --metadata date="$(date +%Y-%m-%d)" \
    2>/dev/null && echo "     ✅ Generated: pdfs/Deployment-Guide.pdf" || echo "     ❌ Failed"

echo ""
echo "=================================="
echo "✅ PDF Generation Complete!"
echo "=================================="
echo ""
echo "Generated PDFs in: ./pdfs/"
ls -lh pdfs/*.pdf 2>/dev/null || echo "No PDFs found"
echo ""
echo "View PDFs:"
echo "  Open pdfs/VMV-AgroMart-User-Manual.pdf"
echo "  Open pdfs/Mobile-App-Testing-Guide.pdf"
echo "  Open pdfs/Deployment-Guide.pdf"
echo ""
