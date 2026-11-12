# ============================================================================
# VMV AgroMart Web Frontend - Multi-stage Docker build (Angular -> Nginx)
# ============================================================================

# ---- Build stage ----
FROM node:18-alpine AS build
WORKDIR /build

# Accepted for docker-compose compatibility; the app currently reads the API URL
# from src/environments/environment.prod.ts rather than at build time.
ARG API_URL

COPY package.json ./
RUN npm install --legacy-peer-deps

COPY . .
RUN npm run build:prod -- --output-path=dist/vmv-agromart-frontend

# ---- Runtime stage ----
FROM nginx:alpine

COPY nginx.conf /etc/nginx/conf.d/default.conf
COPY --from=build /build/dist/vmv-agromart-frontend /usr/share/nginx/html

EXPOSE 80

HEALTHCHECK --interval=30s --timeout=5s --retries=3 \
  CMD wget --quiet --tries=1 --spider http://localhost:80 || exit 1

CMD ["nginx", "-g", "daemon off;"]
