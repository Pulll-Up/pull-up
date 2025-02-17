FROM node:lts AS builder

WORKDIR /app

COPY ../package*.json ./

RUN npm install

COPY ../ ./

RUN npm run build

CMD rm -rf /var/www/html/* && \
    cp -r /app/dist/* /var/www/html/ && \
    chown -R www-data:www-data /var/www/html