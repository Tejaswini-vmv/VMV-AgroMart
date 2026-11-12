#!/bin/bash

# ============================================================================
# VMV AgroMart - Startup Script
# Quick deployment script for Docker setup
# ============================================================================

set -e  # Exit on error

# Colors
BLUE='\033[0;34m'
GREEN='\033[0;32m'
YELLOW='\033[0;33m'
RED='\033[0;31m'
NC='\033[0m' # No Color

# Functions
print_header() {
    echo -e "${BLUE}================================${NC}"
    echo -e "${BLUE}  VMV AgroMart - Startup Script${NC}"
    echo -e "${BLUE}================================${NC}"
    echo ""
}

print_success() {
    echo -e "${GREEN}✓ $1${NC}"
}

print_error() {
    echo -e "${RED}✗ $1${NC}"
}

print_info() {
    echo -e "${YELLOW}ℹ $1${NC}"
}

check_prerequisites() {
    echo -e "${BLUE}Checking prerequisites...${NC}"

    # Check Docker
    if ! command -v docker &> /dev/null; then
        print_error "Docker is not installed"
        echo "Install from: https://docs.docker.com/get-docker/"
        exit 1
    fi
    print_success "Docker installed"

    # Check Docker Compose
    if ! command -v docker-compose &> /dev/null; then
        print_error "Docker Compose is not installed"
        echo "Install from: https://docs.docker.com/compose/install/"
        exit 1
    fi
    print_success "Docker Compose installed"

    # Check if Docker daemon is running
    if ! docker info &> /dev/null; then
        print_error "Docker daemon is not running"
        echo "Please start Docker and try again"
        exit 1
    fi
    print_success "Docker daemon is running"

    echo ""
}

setup_directories() {
    echo -e "${BLUE}Creating directory structure...${NC}"

    mkdir -p backend/src
    mkdir -p web-frontend/src
    mkdir -p mobile-app/src
    mkdir -p database/init
    mkdir -p nginx/ssl
    mkdir -p backups

    print_success "Directories created"
    echo ""
}

setup_environment() {
    echo -e "${BLUE}Setting up environment...${NC}"

    if [ ! -f .env ]; then
        if [ -f .env.example ]; then
            cp .env.example .env
            print_success ".env file created from .env.example"
            print_info "Please edit .env file with your actual credentials"
            echo ""
            echo "Required credentials:"
            echo "  - TWILIO_ACCOUNT_SID, TWILIO_AUTH_TOKEN"
            echo "  - RAZORPAY_KEY_ID, RAZORPAY_KEY_SECRET"
            echo "  - GOOGLE_MAPS_API_KEY"
            echo ""
            read -p "Press Enter after updating .env file..."
        else
            print_error ".env.example not found"
            exit 1
        fi
    else
        print_success ".env file already exists"
    fi
    echo ""
}

copy_database_schema() {
    echo -e "${BLUE}Checking database schema...${NC}"

    if [ -f vmv-agromart-schema.sql ]; then
        cp vmv-agromart-schema.sql database/init/
        print_success "Database schema copied"
    else
        print_error "vmv-agromart-schema.sql not found"
        print_info "Database will start without initial schema"
    fi
    echo ""
}

start_services() {
    echo -e "${BLUE}Starting Docker services...${NC}"
    echo "This may take a few minutes on first run..."
    echo ""

    docker-compose up -d

    echo ""
    print_success "Services started!"
    echo ""
}

wait_for_services() {
    echo -e "${BLUE}Waiting for services to be ready...${NC}"

    # Wait for MySQL
    echo -n "MySQL: "
    for i in {1..30}; do
        if docker exec vmv-mysql mysqladmin ping -h localhost &> /dev/null; then
            print_success "Ready"
            break
        fi
        echo -n "."
        sleep 2
    done

    # Wait for Backend
    echo -n "Backend API: "
    for i in {1..30}; do
        if curl -sf http://localhost:8080/api/actuator/health &> /dev/null; then
            print_success "Ready"
            break
        fi
        echo -n "."
        sleep 2
    done

    echo ""
}

show_access_info() {
    echo -e "${GREEN}================================${NC}"
    echo -e "${GREEN}  VMV AgroMart is now running!${NC}"
    echo -e "${GREEN}================================${NC}"
    echo ""
    echo "Access Points:"
    echo ""
    echo -e "  ${BLUE}Web Application:${NC}    http://localhost"
    echo -e "  ${BLUE}Backend API:${NC}        http://localhost:8080/api"
    echo -e "  ${BLUE}Mobile Dev Server:${NC}  http://localhost:8100"
    echo -e "  ${BLUE}Database Admin:${NC}     http://localhost:8081"
    echo ""
    echo "Container Status:"
    echo ""
    docker-compose ps
    echo ""
    echo "Useful Commands:"
    echo ""
    echo "  View logs:           docker-compose logs -f"
    echo "  Stop services:       docker-compose down"
    echo "  Restart services:    docker-compose restart"
    echo "  Shell access:        docker exec -it vmv-backend sh"
    echo ""
    echo "For more commands, see README.md or run: make help"
    echo ""
}

show_menu() {
    echo ""
    echo "What would you like to do?"
    echo ""
    echo "1) Full Setup & Start (First Time)"
    echo "2) Start Services Only"
    echo "3) Start Mobile Development"
    echo "4) Stop All Services"
    echo "5) View Logs"
    echo "6) Service Status"
    echo "7) Database Backup"
    echo "8) Clean Up & Reset"
    echo "9) Exit"
    echo ""
    read -p "Enter choice [1-9]: " choice

    case $choice in
        1)
            full_setup
            ;;
        2)
            start_only
            ;;
        3)
            start_mobile
            ;;
        4)
            stop_services
            ;;
        5)
            view_logs
            ;;
        6)
            service_status
            ;;
        7)
            database_backup
            ;;
        8)
            clean_reset
            ;;
        9)
            exit 0
            ;;
        *)
            print_error "Invalid choice"
            show_menu
            ;;
    esac
}

full_setup() {
    print_header
    check_prerequisites
    setup_directories
    setup_environment
    copy_database_schema
    start_services
    wait_for_services
    show_access_info
}

start_only() {
    print_header
    echo -e "${BLUE}Starting services...${NC}"
    docker-compose up -d
    show_access_info
}

start_mobile() {
    print_header
    echo -e "${BLUE}Starting mobile development environment...${NC}"
    docker-compose -f docker-compose.mobile.yml up -d
    echo ""
    print_success "Mobile environment started!"
    echo ""
    echo "Access mobile app at: http://localhost:8100"
    echo ""
}

stop_services() {
    print_header
    echo -e "${BLUE}Stopping services...${NC}"
    docker-compose down
    print_success "Services stopped"
}

view_logs() {
    print_header
    echo "Press Ctrl+C to exit logs"
    sleep 2
    docker-compose logs -f
}

service_status() {
    print_header
    echo -e "${BLUE}Service Status:${NC}"
    echo ""
    docker-compose ps
    echo ""
}

database_backup() {
    print_header
    echo -e "${BLUE}Creating database backup...${NC}"

    mkdir -p backups
    BACKUP_FILE="backups/backup-$(date +%Y%m%d-%H%M%S).sql"

    docker exec vmv-mysql mysqldump -u root -p${MYSQL_ROOT_PASSWORD} vmv_agromart > $BACKUP_FILE

    print_success "Backup created: $BACKUP_FILE"
}

clean_reset() {
    print_header
    echo -e "${RED}WARNING: This will delete all data and reset everything!${NC}"
    read -p "Are you sure? (yes/no): " confirm

    if [ "$confirm" = "yes" ]; then
        echo -e "${BLUE}Cleaning up...${NC}"
        docker-compose down -v
        docker system prune -af
        print_success "Reset complete"
    else
        print_info "Reset cancelled"
    fi
}

# Main execution
if [ "$1" = "--help" ] || [ "$1" = "-h" ]; then
    echo "VMV AgroMart Startup Script"
    echo ""
    echo "Usage:"
    echo "  ./start.sh          - Show interactive menu"
    echo "  ./start.sh setup    - Full setup and start"
    echo "  ./start.sh start    - Start services only"
    echo "  ./start.sh stop     - Stop services"
    echo "  ./start.sh logs     - View logs"
    echo "  ./start.sh status   - Show service status"
    echo ""
    exit 0
fi

case "$1" in
    setup)
        full_setup
        ;;
    start)
        start_only
        ;;
    stop)
        stop_services
        ;;
    logs)
        view_logs
        ;;
    status)
        service_status
        ;;
    mobile)
        start_mobile
        ;;
    backup)
        database_backup
        ;;
    clean)
        clean_reset
        ;;
    *)
        print_header
        show_menu
        ;;
esac
