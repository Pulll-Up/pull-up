import { useGetAuthInfo } from '@/api/auth';
import { useEffect } from 'react';
import { Outlet, useLocation, useNavigate } from 'react-router-dom';
import { toast } from 'react-toastify';

export const AuthCheck = () => {
  const location = useLocation();
  const navigate = useNavigate();
  const { authInfo, isAuthorized, isSuccess, isError } = useGetAuthInfo();

  useEffect(() => {
    if (isError || !isAuthorized) {
      // 로그아웃
      if (location.pathname !== '/signin') {
        toast.error('로그인이 필요합니다.', {
          position: 'bottom-center',
          toastId: 'login-required',
        });
      }
      navigate('/signin');
      return;
    }

    if (isSuccess && !authInfo?.isSignedUp) {
      toast.info('서비스 이용을 위해 관심 과목을 선택해주세요.', {
        position: 'bottom-center',
        toastId: 'subjects-required',
      });
      navigate('/signup');
      return;
    }
  }, [authInfo, isAuthorized, isError, isSuccess, location.pathname]);

  return <Outlet />;
};
