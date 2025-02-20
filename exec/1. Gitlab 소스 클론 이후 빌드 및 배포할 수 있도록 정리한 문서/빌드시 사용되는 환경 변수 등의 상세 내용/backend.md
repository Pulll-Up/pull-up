## 실행시키고 싶은 환경에 맞게 설정합니다.
1. Edit Configuration 클릭
2. Active profiles: local or dev
3. Environment variables: ~/server/env/local.env

## local.env
### env 폴더를 생성 후, 아래에 local.env 파일을 생성해야 합니다.
```
DB_DRIVER_CLASS_NAME=com.mysql.cj.jdbc.Driver
DB_URL=jdbc:mysql://127.0.0.1:3306/pullup?serverTimezone=UTC&useUniCode=yes&characterEncoding=UTF-8
DB_USERNAME=root
DB_PASSWORD=4572

SECRET_KEY=b5d80a8ed4b0deb3f2f1205409bcf1657b41f3defa182a657a2f5c47918f3bb0
ACCESS_TOKEN_EXPIRATION=7200000
REFRESH_TOKEN_EXPIRATION=172800000

REDIS_HOST=127.0.0.1
REDIS_PORT=6379

GOOGLE_CLIENT_ID=발급받은 Google Client Id
GOOGLE_CLIENT_SECRET=발급받은 Google Client Secret
GOOGLE_REDIRECT_URI=http://localhost:8080/login/oauth2/code/google
GOOGLE_AUTHORIZATION_GRANT_TYPE=authorization_code

KAKAO_CLIENT_ID=발급받은 Kakao Client Id
KAKAO_CLIENT_SECRET=발급받은 Kakao Client Secret
KAKAO_REDIRECT_URI=http://localhost:8080/login/oauth2/code/kakao
KAKAO_AUTHORIZATION_GRANT_TYPE=authorization_code
KAKAO_CLIENT_AUTHENTICATION_METHOD=client_secret_post

NAVER_CLIENT_ID=발급받은 Naver Client Id
NAVER_CLIENT_SECRET=발급받은 Naver Client Secret
NAVER_REDIRECT_URI=http://localhost:8080/login/oauth2/code/naver
NAVER_AUTHORIZATION_GRANT_TYPE=authorization_code

AUTH_REDIRECT_URI=http://localhost:5173/redirect

OPEN_API_KEY=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

ENCRYPT_SECRET_KEY=3eee1c782a80fa791af57e535541398a
```

## dev.env
### env 폴더를 생성 후, 아래에 dev.env 파일을 생성해야 합니다.
```
DB_DRIVER_CLASS_NAME=com.mysql.cj.jdbc.Driver
DB_URL=배포한 MySQL Url
DB_USERNAME=설정한 MySQL ID
DB_PASSWORD=설정한 MySQL PW

SECRET_KEY=JWT 토큰 암호화 Key
ACCESS_TOKEN_EXPIRATION=7200000
REFRESH_TOKEN_EXPIRATION=172800000

REDIS_HOST=redis
REDIS_PORT=6379

GOOGLE_CLIENT_ID=발급받은 Google Client Id
GOOGLE_CLIENT_SECRET=발급받은 Google Client Secret
GOOGLE_REDIRECT_URI=https://api.pull-up.store/login/oauth2/code/google
GOOGLE_AUTHORIZATION_GRANT_TYPE=authorization_code

KAKAO_CLIENT_ID=발급받은 Kakao Client Id
KAKAO_CLIENT_SECRET=발급받은 Kakao Client Secret
KAKAO_REDIRECT_URI=https://api.pull-up.store/login/oauth2/code/kakao
KAKAO_AUTHORIZATION_GRANT_TYPE=authorization_code
KAKAO_CLIENT_AUTHENTICATION_METHOD=client_secret_post

NAVER_CLIENT_ID=발급받은 Naver Client Id
NAVER_CLIENT_SECRET=발급받은 Naver Client Secret
NAVER_REDIRECT_URI=https://api.pull-up.store/login/oauth2/code/naver
NAVER_AUTHORIZATION_GRANT_TYPE=authorization_code

AUTH_REDIRECT_URI=https://www.pull-up.store/redirect

OPEN_API_KEY=발급받은 Open API Key

ENCRYPT_SECRET_KEY=암호화에 사용되는 Key
```


## pullup-firebase-admin-sdk.json
### resources 밑에 생성해야 합니다.
```
{
  "type": "service_account",
  "project_id": "pullup-50907",
  "private_key_id": "발급받은 KEY Id",
  "private_key": "발급받은 KEY",
  "client_email": "firebase-adminsdk-fbsvc@pullup-50907.iam.gserviceaccount.com",
  "client_id": "발급받은 Client Id",
  "auth_uri": "https://accounts.google.com/o/oauth2/auth",
  "token_uri": "https://oauth2.googleapis.com/token",
  "auth_provider_x509_cert_url": "https://www.googleapis.com/oauth2/v1/certs",
  "client_x509_cert_url": "발급받은 Client Cert Url",
  "universe_domain": "googleapis.com"
}
```
