import { createAnswer, useGetInterview } from '@/api/interview';
import InputForm from '@/components/interview/inputForm';
import InterviewCard from '@/components/interview/interviewCard';
import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { toast } from 'react-toastify';
import { TextAreaChangeEvent, TextAreaKeyboardEvent } from '@/types/event';
import { getMember } from '@/api/member';
import { queryClient } from '@/main';
import { Member } from '@/types/member';
import { useGetAuthInfo } from '@/api/auth';

const InterviewPage = () => {
  const navigate = useNavigate();
  const [member, setMember] = useState<Member>();
  const { authInfo } = useGetAuthInfo();
  const { data } = useGetInterview();

  const [hint, setHint] = useState(false);
  const [answer, setAnswer] = useState('');

  useEffect(() => {
    const fetchMember = async () => {
      const data = await queryClient.fetchQuery({
        queryKey: ['member'],
        queryFn: getMember,
      });

      if (!data) return null;

      if (authInfo && authInfo.isSolvedToday) {
        navigate('/');
        toast.info('오늘의 문제를 이미 풀었습니다. 결과를 확인하세요!', {
          position: 'bottom-center',
          toastId: 'interview-solved',
        });

        return;
      }

      setMember(data);
    };

    fetchMember();
  }, []);

  if (!authInfo || !data || !member) return null;

  const onSubmit = async () => {
    if (answer.trim().length < 20) {
      toast.error('최소 20자 이상 입력해주세요.', { position: 'bottom-center', toastId: 'answer-required' });

      return;
    }

    // 답안 제출
    const response = await createAnswer({
      interviewId: data.interviewId,
      answer,
    });

    navigate(`/interview/result/${response.interviewAnswerId}`);
  };

  const onKeyDown = (e: TextAreaKeyboardEvent) => {
    if (e.key === 'Enter' && !e.shiftKey) {
      e.preventDefault();
      onSubmit();
    }
  };

  const onChange = (e: TextAreaChangeEvent) => {
    if (e.target.value.length > 500) {
      return;
    }

    setAnswer(e.target.value);
  };

  // 힌트 보기
  const onHintClick = () => {
    setHint(!hint);
  };

  return (
    <>
      (
      <div className="flex min-h-full w-full items-center justify-center bg-gradient-to-b from-primary-50 to-white p-6 md:p-10">
        <div className="flex w-[873px] flex-col items-center justify-center gap-12 pt-[94px] sm:pt-16">
          <div className="text-xl font-extrabold md:text-2xl lg:text-3xl">
            <span className="text-primary-600">{`${member.name}`}</span>
            <span>님 만을 위한 오늘의 맞춤 문제🎯</span>
          </div>
          <InterviewCard title={data.question} keywords={data.keywords} hint={hint} onHintClick={onHintClick} />
          <div className="flex w-full flex-col justify-start gap-2">
            <span className="text-lg font-semibold lg:text-xl">나의 답변</span>
            <InputForm
              id="todayQuestion"
              placeholder="최소 20자 이상 작성해주세요."
              value={answer}
              limit={500}
              onChange={onChange}
              onSubmit={onSubmit}
              onKeyDown={onKeyDown}
            />
          </div>
        </div>
      </div>
      )
    </>
  );
};

export default InterviewPage;
