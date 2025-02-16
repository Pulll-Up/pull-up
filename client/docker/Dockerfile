FROM node:lts AS builder

WORKDIR /app

COPY ../package*.json ./

RUN npm install

COPY ../ ./

RUN npm run build

CMD ["cp", "-r", "/app/dist", "/var/www/html/"]