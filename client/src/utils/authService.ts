import { logout, reissue } from '@/api/auth';
import api from '@/api/instance';
import { API_RETRY_COUNT } from '@/constants/auth';
import { ErrorResponseType } from '@/types/response/error';
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
  console.log('토큰 주입');
  const token = AuthStore.getAccessToken();
  const isLogin = request.url.includes('/auth/signin'); // 로그인은 헤더에 토큰 주입 안함

  if (token && !isLogin) {
    request.headers.set('Authorization', `Bearer ${token}`);
  }
};

// 토큰 재발급
export const handleRefreshToken: BeforeRetryHook = async ({ error, retryCount }) => {
  console.log('토큰 에러: ', error);
  const customError = error as ErrorResponseType;

  console.log('커스텀 에러: ', customError);
  if (customError.errorMessage === '[ACCESS_TOKEN] 만료된 Token 입니다.') {
    console.log('토큰 만료');

    if (retryCount === API_RETRY_COUNT - 1) {
      await logout();
      return api.stop;
    }

    await reissue();
  }
};
