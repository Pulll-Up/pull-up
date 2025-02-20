import { getAuthInfo, login } from '@/api/auth';
import { queryClient } from '@/main';
import { useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { toast } from 'react-toastify';

const RedirectPage = () => {
  const navigate = useNavigate();

  useEffect(() => {
    const handleRedirect = async () => {
      const auth = await queryClient.fetchQuery({
        queryKey: ['auth'],
        queryFn: login,
      });

      if (!auth) {
        toast.error('로그인 정보가 없습니다. 다시 로그인 해주세요.', {
          position: 'bottom-center',
          toastId: 'auth-required',
        });
        navigate('/');
        return;
      }

      // 관심과목 미설정 시
      if (!auth.isSignedUp) {
        navigate('/signup');
        return;
      }

      const authInfo = await getAuthInfo();
      queryClient.setQueryData(['authInfo'], { authInfo: authInfo, isAuthorized: true });

      navigate('/');
    };

    handleRedirect();
  }, [navigate]);

  return null;
};

export default RedirectPage;
