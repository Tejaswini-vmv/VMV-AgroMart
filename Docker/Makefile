# ============================================================================
# VMV AgroMart - Makefile
# Convenient commands for Docker management
# ============================================================================

.PHONY: help setup start stop restart logs clean build rebuild status backup restore

# Default target
.DEFAULT_GOAL := help

# Colors for output
BLUE := \033[0;34m
GREEN := \033[0;32m
YELLOW := \033[0;33m
RED := \033[0;31m
NC := \033[0m # No Color

##@ General Commands

help: ## Show this help message
	@echo '${BLUE}VMV AgroMart - Available Commands${NC}'
	@echo ''
	@awk 'BEGIN {FS = ":.*##"; printf "\nUsage:\n  make ${YELLOW}<target>${NC}\n"} /^[a-zA-Z_-]+:.*?##/ { printf "  ${GREEN}%-15s${NC} %s\n", $$1, $$2 } /^##@/ { printf "\n${BLUE}%s${NC}\n", substr($$0, 5) } ' $(MAKEFILE_LIST)

##@ Setup & Configuration

setup: ## Initial setup - copy environment files
	@echo "${BLUE}Setting up VMV AgroMart...${NC}"
	@mkdir -p backend web-frontend mobile-app database/init nginx/ssl
	@if [ ! -f .env ]; then \
		cp .env.example .env; \
		echo "${GREEN}Created .env file. Please edit with your credentials.${NC}"; \
	else \
		echo "${YELLOW}.env file already exists${NC}"; \
	fi
	@echo "${GREEN}Setup complete!${NC}"

check-env: ## Check if .env file exists
	@if [ ! -f .env ]; then \
		echo "${RED}Error: .env file not found!${NC}"; \
		echo "Run: make setup"; \
		exit 1; \
	fi

##@ Docker Operations

start: check-env ## Start all services
	@echo "${BLUE}Starting VMV AgroMart...${NC}"
	@docker-compose up -d
	@echo "${GREEN}Services started!${NC}"
	@echo "Web: http://localhost"
	@echo "API: http://localhost:8080/api"
	@echo "Mobile: http://localhost:8100"

start-mobile: check-env ## Start mobile development environment
	@echo "${BLUE}Starting mobile development environment...${NC}"
	@docker-compose -f docker-compose.mobile.yml up -d
	@echo "${GREEN}Mobile environment started!${NC}"

stop: ## Stop all services
	@echo "${BLUE}Stopping services...${NC}"
	@docker-compose down
	@echo "${GREEN}Services stopped${NC}"

restart: ## Restart all services
	@echo "${BLUE}Restarting services...${NC}"
	@docker-compose restart
	@echo "${GREEN}Services restarted${NC}"

status: ## Show status of all services
	@echo "${BLUE}Service Status:${NC}"
	@docker-compose ps

##@ Build & Development

build: ## Build all Docker images
	@echo "${BLUE}Building Docker images...${NC}"
	@docker-compose build
	@echo "${GREEN}Build complete!${NC}"

rebuild: ## Rebuild and restart all services
	@echo "${BLUE}Rebuilding services...${NC}"
	@docker-compose build --no-cache
	@docker-compose down
	@docker-compose up -d
	@echo "${GREEN}Rebuild complete!${NC}"

build-backend: ## Build only backend
	@echo "${BLUE}Building backend...${NC}"
	@docker-compose build backend
	@docker-compose up -d backend
	@echo "${GREEN}Backend rebuilt!${NC}"

build-frontend: ## Build only web frontend
	@echo "${BLUE}Building frontend...${NC}"
	@docker-compose build web-frontend
	@docker-compose up -d web-frontend
	@echo "${GREEN}Frontend rebuilt!${NC}"

build-mobile: ## Build mobile APK
	@echo "${BLUE}Building mobile APK...${NC}"
	@docker-compose --profile build run android-builder
	@echo "${GREEN}APK built: mobile-app/output/vmv-agromart.apk${NC}"

##@ Logs & Monitoring

logs: ## Show logs from all services
	@docker-compose logs -f

logs-backend: ## Show backend logs
	@docker-compose logs -f backend

logs-frontend: ## Show frontend logs
	@docker-compose logs -f web-frontend

logs-db: ## Show database logs
	@docker-compose logs -f mysql

logs-redis: ## Show Redis logs
	@docker-compose logs -f redis

##@ Database Operations

db-shell: ## Access MySQL shell
	@echo "${BLUE}Accessing MySQL shell...${NC}"
	@docker exec -it vmv-mysql mysql -u root -p

db-backup: ## Backup database
	@echo "${BLUE}Backing up database...${NC}"
	@mkdir -p backups
	@docker exec vmv-mysql mysqldump -u root -p$${MYSQL_ROOT_PASSWORD} vmv_agromart > backups/backup-$(shell date +%Y%m%d-%H%M%S).sql
	@echo "${GREEN}Backup created in backups/ directory${NC}"

db-restore: ## Restore database from backup (Usage: make db-restore FILE=backup.sql)
	@if [ -z "$(FILE)" ]; then \
		echo "${RED}Error: Please specify backup file${NC}"; \
		echo "Usage: make db-restore FILE=backup.sql"; \
		exit 1; \
	fi
	@echo "${BLUE}Restoring database from $(FILE)...${NC}"
	@docker exec -i vmv-mysql mysql -u root -p$${MYSQL_ROOT_PASSWORD} vmv_agromart < $(FILE)
	@echo "${GREEN}Database restored!${NC}"

redis-cli: ## Access Redis CLI
	@echo "${BLUE}Accessing Redis CLI...${NC}"
	@docker exec -it vmv-redis redis-cli -a $${REDIS_PASSWORD}

##@ Testing & Debug

test: ## Run tests (if configured)
	@echo "${BLUE}Running tests...${NC}"
	@docker-compose exec backend mvn test
	@echo "${GREEN}Tests complete!${NC}"

debug: ## Start services with debug output
	@echo "${BLUE}Starting in debug mode...${NC}"
	@docker-compose --profile debug up

shell-backend: ## Shell access to backend container
	@docker exec -it vmv-backend sh

shell-frontend: ## Shell access to frontend container
	@docker exec -it vmv-web-frontend sh

##@ Cleanup

clean: ## Stop and remove containers, networks
	@echo "${BLUE}Cleaning up...${NC}"
	@docker-compose down
	@echo "${GREEN}Cleanup complete${NC}"

clean-all: ## Remove containers, networks, volumes, and images
	@echo "${RED}WARNING: This will delete all data!${NC}"
	@read -p "Are you sure? [y/N] " -n 1 -r; \
	echo; \
	if [[ $$REPLY =~ ^[Yy]$$ ]]; then \
		docker-compose down -v; \
		docker system prune -af; \
		echo "${GREEN}Full cleanup complete${NC}"; \
	fi

reset: ## Reset everything and start fresh
	@echo "${RED}Resetting VMV AgroMart...${NC}"
	@docker-compose down -v
	@docker-compose build --no-cache
	@docker-compose up -d
	@echo "${GREEN}Reset complete!${NC}"

##@ Production

deploy: ## Deploy to production
	@echo "${BLUE}Deploying to production...${NC}"
	@docker-compose -f docker-compose.yml -f docker-compose.prod.yml up -d
	@echo "${GREEN}Deployment complete!${NC}"

scale-backend: ## Scale backend instances (Usage: make scale-backend N=3)
	@if [ -z "$(N)" ]; then \
		echo "${RED}Error: Please specify number of instances${NC}"; \
		echo "Usage: make scale-backend N=3"; \
		exit 1; \
	fi
	@echo "${BLUE}Scaling backend to $(N) instances...${NC}"
	@docker-compose up -d --scale backend=$(N)
	@echo "${GREEN}Backend scaled to $(N) instances${NC}"

##@ Utilities

update: ## Pull latest changes and rebuild
	@echo "${BLUE}Updating VMV AgroMart...${NC}"
	@git pull
	@docker-compose build
	@docker-compose down
	@docker-compose up -d
	@echo "${GREEN}Update complete!${NC}"

stats: ## Show resource usage statistics
	@echo "${BLUE}Resource Usage:${NC}"
	@docker stats --no-stream

network: ## Show network information
	@echo "${BLUE}Network Configuration:${NC}"
	@docker network inspect vmv-agromart_agromart-network

volumes: ## Show volume information
	@echo "${BLUE}Volumes:${NC}"
	@docker volume ls | grep vmv

##@ Quick Actions

quick-start: setup build start ## Complete setup and start (first time)
	@echo "${GREEN}VMV AgroMart is now running!${NC}"
	@echo ""
	@echo "Access points:"
	@echo "  Web: http://localhost"
	@echo "  API: http://localhost:8080/api"
	@echo "  Mobile: http://localhost:8100"
	@echo "  DB Admin: http://localhost:8081"

dev: ## Start development environment with logs
	@docker-compose up

prod: ## Start production environment
	@docker-compose -f docker-compose.yml up -d
