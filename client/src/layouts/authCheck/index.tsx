import { useGetAuthInfo } from '@/api/auth';
import { useEffect } from 'react';
import { Outlet, useLocation, useNavigate } from 'react-router-dom';
import { toast } from 'react-toastify';

const PUBLIC_PATHS = ['/', '/signin', '/signup', '/redirect'];

export const AuthCheck = () => {
  const { authInfo, isAuthorized, isLoading, error } = useGetAuthInfo();
  const location = useLocation();
  const navigate = useNavigate();

  useEffect(() => {
    const isPublicPath = PUBLIC_PATHS.some((path) => path === location.pathname);

    if (!isLoading && !isPublicPath) {
      if (!isAuthorized || error) {
        toast.error('로그인이 필요합니다.', { position: 'bottom-center', toastId: 'login-required' });
        navigate('/signin');
        return;
      }

      if (authInfo && !authInfo.isSignedUp) {
        toast.info('서비스 이용을 위해 관심 과목을 선택해주세요.', {
          position: 'bottom-center',
          toastId: 'subjects-required',
        });
        navigate('/signup');
        return;
      }
    }
  }, [authInfo, isAuthorized, isLoading, error, location.pathname]);

  return <Outlet />;
};
