import { createAnswer, useGetInterview } from '@/api/interview';
import InputForm from '@/components/interview/inputForm';
import InterviewCard from '@/components/interview/interviewCard';
import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { toast } from 'react-toastify';
import { TextAreaChangeEvent, TextAreaKeyboardEvent } from '@/types/event';
import { memberStore } from '@/stores/memberStore';
import { getMember } from '@/api/member';
import { queryClient } from '@/main';
import { Member } from '@/types/member';
import LoadingPage from '@/pages/loading';

const InterviewPage = () => {
  const navigate = useNavigate();
  const { setInterviewAnswerId, setIsSolvedToday, isSolvedToday } = memberStore();
  const [member, setMember] = useState<Member>();
  const { data } = useGetInterview();

  const [hint, setHint] = useState(false);
  const [answer, setAnswer] = useState('');
  const [isSubmitting, setIsSubmitting] = useState(false);

  useEffect(() => {
    if (isSolvedToday) {
      navigate('/');
      toast.info('오늘의 문제를 이미 풀었습니다. 결과를 확인하세요!', {
        position: 'bottom-center',
        toastId: 'interview-solved',
      });

      return;
    }

    const fetchMember = async () => {
      const data = await queryClient.fetchQuery({
        queryKey: ['member'],
        queryFn: getMember,
      });

      if (!data) return null;

      setMember(data);
    };

    fetchMember();
  }, []);

  if (!data || !member) return null;

  const onSubmit = async () => {
    if (!answer) {
      toast.error('답변을 입력해주세요.', { position: 'bottom-center', toastId: 'answer-required' });

      return;
    }

    setIsSubmitting(true);

    // 답안 제출
    const response = await createAnswer({
      interviewId: data.interviewId,
      answer,
    });

    setInterviewAnswerId(response.interviewAnswerId);
    setIsSolvedToday(true);
    navigate(`/interview/result/${response.interviewAnswerId}`);

    setIsSubmitting(false);
  };

  const onKeyDown = (e: TextAreaKeyboardEvent) => {
    if (e.key === 'Enter' && !e.shiftKey) {
      e.preventDefault();
      onSubmit();
    }
  };

  const onChange = (e: TextAreaChangeEvent) => {
    setAnswer(e.target.value);
  };

  // 힌트 보기
  const onHintClick = () => {
    setHint(!hint);
  };

  return (
    <>
      {isSubmitting ? (
        <LoadingPage />
      ) : (
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
                placeholder="질문에 대한 답을 남겨주세요."
                value={answer}
                limit={500}
                onChange={onChange}
                onSubmit={onSubmit}
                onKeyDown={onKeyDown}
              />
            </div>
          </div>
        </div>
      )}
    </>
  );
};

export default InterviewPage;
