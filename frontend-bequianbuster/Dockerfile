# =====================================================================
# Etapa 1: compilación de Angular
# =====================================================================
FROM node:24-bookworm-slim AS compilacion
WORKDIR /app

# Primero las dependencias, para aprovechar la caché de Docker
# npm ci instalará las dependencias exactas del package-lock.json
COPY package.json package-lock.json ./
RUN npm ci --no-audit --no-fund

# Luego el código, y la compilación de producción
# (usa environment.ts con las rutas relativas /api/...)
COPY . .
RUN npm run build

# =====================================================================
# Etapa 2: Nginx sirve los archivos estáticos
# =====================================================================
FROM nginx:stable-alpine

COPY nginx.conf /etc/nginx/conf.d/default.conf
COPY --from=compilacion /app/dist/frontend-bequianbuster/browser /usr/share/nginx/html

EXPOSE 80