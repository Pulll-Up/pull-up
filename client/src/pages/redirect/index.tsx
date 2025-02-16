import { login } from '@/api/auth';
import { getMember } from '@/api/member';
import { queryClient } from '@/main';
import { memberStore } from '@/stores/memberStore';
import { setupNotification } from '@/utils/notiService';
import { useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { toast } from 'react-toastify';

const RedirectPage = () => {
  const navigate = useNavigate();
  const { setMember, setIsLoggedIn, setIsSolvedToday, setInterviewAnswerId } = memberStore();
  console.log('redirect 페이지');

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

      // 사용자 정보 설정
      setIsSolvedToday(auth.isSolvedToday);
      setInterviewAnswerId(auth.interviewAnswerId);

      console.log('사용자 상태: ', auth.isSignedUp);

      // 비회원가입 시
      if (!auth.isSignedUp) {
        console.log('비회원 확인');
        navigate('/signup');
        return;
      }

      const member = await getMember();

      // 관심과목 미선택 시
      if (!member.interestSubjects) {
        console.log('관심과목 확인');
        navigate('/signup');
        return;
      }

      setMember(member);
      setIsLoggedIn(true);
      navigate('/');
    };

    handleRedirect();
  }, [navigate]);

  return null;
};

export default RedirectPage;
