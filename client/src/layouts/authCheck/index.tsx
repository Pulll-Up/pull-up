import { useGetAuthInfo } from '@/api/auth';
import { useEffect } from 'react';
import { Outlet, useLocation, useNavigate } from 'react-router-dom';
import { toast } from 'react-toastify';

const AuthRequired = () => {
  const location = useLocation();
  const navigate = useNavigate();
  const { authInfo, isAuthorized, isSuccess, isError } = useGetAuthInfo();

  useEffect(() => {
    if (isError || !isAuthorized) {
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

// 사용자 정보 확인
export const AuthCheck = () => {
  const location = useLocation();

  // 리다이렉트 페이지는 확인없이 바로 렌더링
  if (location.pathname === '/redirect') {
    return <Outlet />;
  }

  return <AuthRequired />;
};
