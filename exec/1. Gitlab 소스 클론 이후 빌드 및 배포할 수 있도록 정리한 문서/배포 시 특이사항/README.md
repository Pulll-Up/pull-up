### 현재 중요 파일(개인 정보)을 다 숨겨 두어서, Frontend나 Backend나 clone만 받아서 docker-compose 실행한다고 되는 것이 아니라, 2번에 기재된 파일을 생성해야 합니다.

### 프론트엔드는 Nginx를 이용한 정적 파일 서빙 방식을 이용하기 때문에 Nginx 컨테이너가 띄워져 있어야 합니다.

## 생성 후, docker 폴더로 디렉토리 경로를 이동하여 아래와 같이 입력합니다.
### 프론트엔드 명령어 : docker compose -f frontend up -d
### 백엔드 명령어 : docker compose -f spring-app-dev up -d


### nginx.conf
server {
    listen 80;
    server_name api.pull-up.store jenkins.pull-up.store www.pull-up.store;
    server_tokens off;

    location /.well-known/acme-challenge/ {
        root /var/www/certbot;
    }

    location / {
        return 301 https://$host$request_uri;
    }
}

server {
    listen 443 ssl;
    server_name api.pull-up.store;
    server_tokens off;

    ssl_certificate /etc/letsencrypt/live/api.pull-up.store/fullchain.pem;
    ssl_certificate_key /etc/letsencrypt/live/api.pull-up.store/privkey.pem;

    location / {
        proxy_pass  http://api.pull-up.store:8080;
        proxy_set_header    Host                $http_host;
        proxy_set_header    X-Real-IP           $remote_addr;
        proxy_set_header    X-Forwarded-For     $proxy_add_x_forwarded_for;
        proxy_http_version 1.1;
        proxy_set_header Upgrade $http_upgrade;
        proxy_set_header Connection "Upgrade";

    }
}

server {
    listen 443 ssl;
    server_name jenkins.pull-up.store;
    server_tokens off;

    ssl_certificate /etc/letsencrypt/live/jenkins.pull-up.store/fullchain.pem;
    ssl_certificate_key /etc/letsencrypt/live/jenkins.pull-up.store/privkey.pem;

    location / {
        proxy_pass http://jenkins:9090;
        proxy_set_header    Host                $http_host;
        proxy_set_header    X-Real-IP           $remote_addr;
        proxy_set_header    X-Forwarded-For     $proxy_add_x_forwarded_for;
    }
}

server {
    listen 443 ssl;
    server_name www.pull-up.store;
    server_tokens off;

    ssl_certificate /etc/letsencrypt/live/www.pull-up.store/fullchain.pem;
    ssl_certificate_key /etc/letsencrypt/live/www.pull-up.store/privkey.pem;

    location / {
        root /usr/share/nginx/html;
        index index.html;
        try_files $uri $uri/ /index.html;

        add_header Cache-Control "no-cache, no-store, must-revalidate";
        add_header Pragma "no-cache";
        add_header Expires "0";
    }

    location /api {
        proxy_pass http://api.pull-up.store:8080;
        proxy_set_header Host $http_host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_http_version 1.1;
        proxy_set_header Upgrade $http_upgrade;
        proxy_set_header Connection "Upgrade"; 
    }

    location /game-websocket/ {
        proxy_pass http://api.pull-up.store:8080;
        proxy_http_version 1.1;
        proxy_set_header Upgrade $http_upgrade;
        proxy_set_header Connection "Upgrade";
        proxy_set_header Host $host;
    }
}

## docker-compose.nginx.yml
version: '3.8'

services:
  nginx:
    image: nginx:latest
    container_name: nginx
    ports:
      - "80:80"
      - "443:443"
    volumes:
      - ../nginx-data/nginx:/etc/nginx/conf.d
      - ../nginx-data/certbot/conf:/etc/letsencrypt
      - ../nginx-data/certbot/www:/var/www/certbot
      - /var/www/html:/usr/share/nginx/html
    depends_on:
      - certbot
    networks:
      - proxy_network 
      - ci_network 
      - backend
      - frontend

  certbot:
    image: certbot/certbot
    volumes:
      - ../nginx-data/certbot/conf:/etc/letsencrypt
      - ../nginx-data/certbot/www:/var/www/certbot
    networks:
      - proxy_network

volumes:
  frontend_build:
    external: true

networks:
  proxy_network:
    driver: bridge
  ci_network:
    driver: bridge
  backend:
    driver: bridge
  frontend:
    driver: bridge
