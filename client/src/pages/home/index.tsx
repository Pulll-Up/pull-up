import { useGetAuthInfo } from '@/api/auth';
import { getInterviewResult } from '@/api/interview';
import SmallChip from '@/components/common/smallchip';
import SubmitButton from '@/components/common/submitButton';
import { useChipAnimation } from '@/hooks/useChipAnimation';
import { queryClient } from '@/main';
import { useEffect } from 'react';
import { useNavigate } from 'react-router-dom';

const HomePage = () => {
  const navigate = useNavigate();
  const { authInfo } = useGetAuthInfo();

  useEffect(() => {
    // 메인 페이지 이동 시 결과 페이지 prefetch
    if (authInfo?.isSolvedToday && authInfo?.interviewAnswerId) {
      queryClient.prefetchQuery({
        queryKey: ['result', authInfo.interviewAnswerId],
        queryFn: () => getInterviewResult(authInfo.interviewAnswerId),
      });
    }
  }, [authInfo?.isSolvedToday, authInfo?.interviewAnswerId]);

  const onClick = () => {
    if (authInfo) {
      // 관심과목 미설정 시
      if (!authInfo.isSignedUp) {
        navigate('/signup');
        return;
      }

      if (authInfo.isSolvedToday) {
        // 문제를 풀었을 경우
        navigate(`/interview/result/${authInfo.interviewAnswerId}`);
        return;
      } else {
        navigate('/interview');
        return;
      }
    } else {
      navigate('/signin');
    }
  };

  // 버튼 호버 시 결과 페이지 prefetch
  const onButtonHover = () => {
    if (authInfo?.isSolvedToday && authInfo?.interviewAnswerId) {
      queryClient.prefetchQuery({
        queryKey: ['result', authInfo.interviewAnswerId],
        queryFn: () => getInterviewResult(authInfo.interviewAnswerId),
      });
    }
  };

  // 애니메이션 적용
  const { styles, currentStyles } = useChipAnimation();
  const activeIndex = currentStyles.findIndex((style) => style === 1); // 활성화 인덱스
  const titles = ['오늘의 문제', '모의고사', '학습 게임'];
  const contents = [
    ['매일 새로운 CS 문제', 'AI 분석과 다양한 답안 비교'],
    ['랜덤 모의고사로 실력 점검', '틀린 문제 아카이브로 복습'],
    ['1대1 CS 카드게임 대결', '친구와 함께 재미있게 학습'],
  ];

  return (
    <div
      className="relative flex min-h-full min-w-full items-center pt-[94px] sm:pt-16"
      style={{
        background: `
        radial-gradient(circle at 50% 50%, rgb(255, 255, 255) 0%, transparent 100%),
        radial-gradient(circle at 50% 10%, rgb(186, 230, 253) 0%, transparent 30%),
        radial-gradient(circle at 80% 80%, rgb(227, 227, 255) 0%, transparent 50%),
        linear-gradient(180deg, rgb(219, 234, 254) 0%, rgb(255, 255, 255) 100%)
      `,
      }}
    >
      <div className="relative flex h-full w-full items-center justify-around p-6 md:p-10 lg:p-20">
        {/* 좌측 컨테이너 */}
        <div className="flex flex-col gap-10">
          <div className="flex flex-col gap-6">
            <div className="flex gap-4">
              {titles.map((title, index) => (
                <SmallChip
                  key={title}
                  title={title}
                  color={`${styles[currentStyles[index]]} transition-all `}
                  style={{ transitionDuration: '2000ms' }}
                />
              ))}
            </div>
            <div className="flex flex-col items-center text-center text-2xl font-bold md:w-[320px] md:items-start md:text-left md:text-3xl lg:w-[380px] lg:text-4xl xl:w-[510px] xl:text-5xl">
              {contents.map((group, index) => (
                <div
                  key={index}
                  className={`flex flex-col transition-all ${
                    index === activeIndex ? 'opacity-100' : 'absolute opacity-0'
                  }`}
                  style={{ transitionDuration: '1500ms', transitionDelay: index === activeIndex ? '500ms' : '0ms' }}
                >
                  <div className="flex flex-col space-y-2">
                    {group.map((text, i) => (
                      <span key={i}>{text}</span>
                    ))}
                  </div>
                </div>
              ))}
            </div>
            <div className="flex flex-col gap-2">
              <div className="flex flex-col items-center text-xl font-semibold md:items-start md:text-2xl">
                <span>CS 모의고사 풀이부터</span>
                <span>매일 알림으로 받는 기술 면접까지</span>
              </div>
              <span className="text-lg font-medium text-primary-500 md:text-xl">
                간편하게 오늘의 문제부터 받아보세요.
              </span>
            </div>
          </div>
          <SubmitButton
            text={
              !authInfo
                ? '알림 받으러 가기'
                : !authInfo.isSignedUp
                  ? '관심 과목 설정하러 가기'
                  : !authInfo.isSolvedToday
                    ? '오늘의 문제 풀러 가기'
                    : '오늘의 문제 결과 보기'
            }
            color="secondary"
            onClick={onClick}
            onMouseEnter={onButtonHover}
          />
        </div>
        {/* 우측 컨테이너 */}
        <img
          src="/assets/images/exam1.png"
          alt="대문 이미지"
          className="hidden h-auto w-[300px] md:block lg:w-[400px] xl:w-[600px]"
        />
      </div>
    </div>
  );
};

export default HomePage;
