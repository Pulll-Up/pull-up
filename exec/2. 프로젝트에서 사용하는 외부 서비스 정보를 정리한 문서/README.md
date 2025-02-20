# OAuth2

## Google
```
1. https://console.cloud.google.com/ 접속 및 로그인
2. 프로젝트 만든 후, API 및 서비스에서 사용자 인증 정보 클릭
3. OAuth2 2.0 클라이언트 ID 클릭
4. 승인된 리디렉션 URI에 2가지 등록
    http://localhost:8080/login/oauth2/code/google
    https://api.pull-up.store/login/oauth2/code/google
```

## Kakao
```
1. https://developers.kakao.com/ 접속 및 로그인
2. 내 애플리케이션으로 이동
3. 애플리케이션 추가하기 → pull-up
4. 앱 사진 등록 후, 카카오 로그인 클릭
5. Redirect URI 2가지 등록
    http://localhost:8080/login/oauth2/code/kakao
    https://api.pull-up.store/login/oauth2/code/kakao
6. 동의 항목에서 닉네임, 프로필 사진, 카카오 계정 설정
```
## Naver
```
1. https://developers.naver.com/ 접속 및 로그인
2. 로그인 API 명세에서 오픈 API 이용 신청하기 클릭
3. 애플리케이션 등록
4. 서비스 URL에 [https://www.pull-up.store](https://www.pull-up.store/) 입력
5. Callback URL에 2가지 등록
    http://localhost:8080/login/oauth2/code/naver
    https://api.pull-up.store/login/oauth2/code/naver
6. 사진 등록 후, 심사 받기 (배포된 상태로 운영중인 서비스여야 함)
7. 회원 이름, 연락처 이메일 주소, 프로필 사진 설정
```
