import { signup } from '@/api/auth';
import { getMember } from '@/api/member';
import CsConditionSelector from '@/components/common/csConditionSelector';
import ProgressSteps from '@/components/common/progressSteps';
import { queryClient } from '@/main';
import { memberStore } from '@/stores/memberStore';
import { Subject } from '@/types/member';
import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { toast } from 'react-toastify';
import CompleteMessage from '@/components/common/completeMessage';

const SignUpPage = () => {
  const navigate = useNavigate();
  const { setMember, setIsLoggedIn } = memberStore();
  const [progress, setProgress] = useState(1);
  const [showLoginComplete, setShowLoginComplete] = useState(false);
  const [showSelector, setShowSelector] = useState(false);

  useEffect(() => {
    const fetchMember = async () => {
      const member = await queryClient.fetchQuery({
        queryKey: ['member'],
        queryFn: getMember,
      });

      if (member.interestSubjects.length > 0) {
        navigate('/');
        toast.info('이미 가입된 회원입니다.', {
          position: 'bottom-center',
          toastId: 'member-checked',
        });
        return;
      }

      setShowLoginComplete(true);

      setTimeout(() => {
        setProgress(2);

        // 프로그레스바 변경 후 0.5초 뒤에 로그인 완료 메시지 페이드아웃
        setTimeout(() => {
          setShowLoginComplete(false);
          // 메시지 페이드아웃 후 0.3초 뒤에 선택기 표시
          setTimeout(() => {
            setShowSelector(true);
          }, 300);
        }, 500);
      }, 2000);
    };

    fetchMember();
  }, []);

  const onConfirmSignUp = async (selectedSubjects: Subject[]) => {
    await signup(selectedSubjects);
    setProgress(3);

    const member = await getMember();

    if (!member) {
      toast.error('회원가입을 실패했습니다.', {
        position: 'bottom-center',
        toastId: 'member-required',
      });
      setProgress(2);
      return;
    }

    setMember(member);
    setIsLoggedIn(true);

    setTimeout(() => {
      navigate('/');
      toast.success('회원가입이 완료되었습니다.', {
        position: 'bottom-center',
        toastId: 'signed-up',
      });
    }, 300);
  };

  return (
    <>
      <style>
        {`
          @keyframes fadeInUp {
            from {
              opacity: 0;
              transform: translateY(20px);
            }
            to {
              opacity: 1;
              transform: translateY(0);
            }
          }

          @keyframes fadeOut {
            from {
              opacity: 1;
              transform: translateY(0);
            }
            to {
              opacity: 0;
              transform: translateY(-20px);
            }
          }

          .animate-fade-in-up {
            animation: fadeInUp 0.5s ease-out forwards;
          }

          .animate-fade-out {
            animation: fadeOut 0.5s ease-out forwards;
          }
        `}
      </style>

      <div
        className="relative flex h-full w-full flex-col items-center pt-[94px] sm:pt-16"
        style={{
          background: `
          radial-gradient(circle at 50% 50%, rgb(255, 255, 255) 0%, transparent 100%),
          radial-gradient(circle at 50% 10%, rgb(186, 230, 253) 0%, transparent 30%),
          radial-gradient(circle at 80% 80%, rgb(227, 227, 255) 0%, transparent 50%),
          linear-gradient(180deg, rgb(219, 234, 254) 0%, rgb(255, 255, 255) 100%)
        `,
        }}
      >
        {/* 프로그레스바 컨테이너 */}
        <div className="flex h-full w-full flex-col items-center justify-start gap-10 p-36">
          <div className="sticky">
            <ProgressSteps currentStep={progress} totalStep={2} />
          </div>

          {/* 메인 컨텐츠 영역 */}
          <div className="flex h-[381px] w-[300px] items-center justify-center rounded-2xl bg-white shadow-sm md:h-[464px] md:min-w-[400px] xl:min-w-[450px]">
            {showLoginComplete && (
              <div className={`absolute ${!showSelector ? '' : 'animate-fade-out'}`}>
                <CompleteMessage
                  title="소셜 로그인 완료!"
                  subTitle="곧 마지막 절차로 이동합니다. 잠시만 기다려주세요."
                />
              </div>
            )}

            {showSelector && (
              <div className="animate-fade-in-up">
                <CsConditionSelector
                  title="관심 과목 선택"
                  text="회원가입"
                  onClick={(level, subjects) => onConfirmSignUp(subjects)}
                />
              </div>
            )}
          </div>
        </div>
      </div>
    </>
  );
};

export default SignUpPage;
