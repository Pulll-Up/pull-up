import { logout, reissue } from '@/api/auth';
import api from '@/api/instance';
import { API_RETRY_COUNT } from '@/constants/auth';
import { BeforeRetryHook } from 'ky';

const AUTH_TOKEN_KEY = 'auth_access_token';

export const AuthStore = (() => {
  return {
    getAccessToken: (): string | null => {
      return localStorage.getItem(AUTH_TOKEN_KEY);
    },

    setAccessToken: (token: string) => {
      if (!token) {
        localStorage.removeItem(AUTH_TOKEN_KEY);
        return;
      }

      const accessToken = token.startsWith('Bearer ') ? token.slice(7) : token;
      localStorage.setItem(AUTH_TOKEN_KEY, accessToken);
    },

    clearAccessToken: () => {
      localStorage.removeItem(AUTH_TOKEN_KEY);
    },
  };
})();

// 헤더에 토큰 주입
export const setTokenHeader = (request: Request) => {
  const token = AuthStore.getAccessToken();
  const isLogin = request.url.includes('/auth/signin'); // 로그인은 헤더에 토큰 주입 안함

  if (token && !isLogin) {
    request.headers.set('Authorization', `Bearer ${token}`);
  }
};

// 토큰 재발급
let refreshPromise: Promise<void> | null = null;

export const reissueToken = async () => {
  // 이미 진행 중인 reissue 요청이 있다면 그것을 반환
  if (refreshPromise) {
    return refreshPromise;
  }

  // 새로운 reissue 요청 생성
  refreshPromise = reissue();

  // reissue 완료 후 Promise 초기화
  await refreshPromise;
  refreshPromise = null;
};

export const handleRefreshToken: BeforeRetryHook = async ({ error, retryCount }) => {
  const errorMessage = error.message;

  // 토큰 만료 아니면 멈춤
  if (errorMessage !== '[ACCESS_TOKEN] 만료된 Token 입니다.') {
    return api.stop;
  }

  // retry 횟수 넘기면 로그아웃
  if (retryCount === API_RETRY_COUNT - 1) {
    await logout();
    return api.stop;
  }

  try {
    await reissueToken(); // 단일 reissue 프로미스 사용
  } catch (error) {
    await logout();
    return api.stop;
  }
};
