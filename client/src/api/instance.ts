import ky from 'ky';
import { setTokenHeader, handleRefreshToken } from '@/utils/authService';
import { API_RETRY_COUNT } from '@/constants/auth';
import { ApiError } from './error';

interface ErrorResponse {
  status: number;
  errorMessage: string;
}

const instance = ky.create({
  prefixUrl: import.meta.env.VITE_BASE_URL,
  credentials: 'include',
  headers: {
    'content-type': 'application/json',
  },
});

const api = instance.extend({
  timeout: 10 * 1000,
  retry: {
    limit: API_RETRY_COUNT,
    statusCodes: [401],
    methods: ['get', 'post', 'put', 'delete', 'patch'],
    backoffLimit: 3 * 1000,
  },

  hooks: {
    beforeRequest: [setTokenHeader],
    beforeRetry: [handleRefreshToken],
    afterResponse: [
      async (request, options, response) => {
        if (!response.ok) {
          const errorData = (await response.json().catch(() => null)) as ErrorResponse | null;

          if (errorData) {
            const message = errorData.errorMessage || 'Unknown error';

            throw new ApiError(request, options, response, message);
          }
        }

        return response;
      },
    ],
  },
});

export default api;
