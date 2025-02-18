import { getAuthInfo, login } from '@/api/auth';
import { getMember } from '@/api/member';
import { queryClient } from '@/main';
import { memberStore } from '@/stores/memberStore';
import { setupNotification } from '@/utils/notiService';
import { useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { toast } from 'react-toastify';

const RedirectPage = () => {
  const navigate = useNavigate();
  const { setMember } = memberStore();

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

      // 알림 설정
      setupNotification();

      const member = await getMember();

      // 비회원가입 시
      if (!auth.isSignedUp) {
        navigate('/signup');
        return;
      }

      // 관심과목 미선택 시
      if (!member.interestSubjects?.length) {
        navigate('/signup');
        return;
      }

      await queryClient.fetchQuery({
        queryKey: ['authInfo'],
        queryFn: getAuthInfo,
      });

      setMember(member);
      navigate('/');
    };

    handleRedirect();
  }, [navigate]);

  return null;
};

export default RedirectPage;
