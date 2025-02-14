import { createAnswer, useGetInterview } from '@/api/interview';
import InputForm from '@/components/interview/inputForm';
import InterviewCard from '@/components/interview/interviewCard';
import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { toast } from 'react-toastify';
import Page404 from '../404';
import { useMember } from '@/hooks/useMember';
import { TextAreaChangeEvent, TextAreaKeyboardEvent } from '@/types/event';

const InterviewPage = () => {
  const navigate = useNavigate();
  const memberInfo = useMember().memberInfo;
  const { data } = useGetInterview();

  const [hint, setHint] = useState(false);
  const [answer, setAnswer] = useState('');

  if (!data) return <Page404 />;

  const onSubmit = async () => {
    if (!answer) {
      toast.error('답변을 입력해주세요.', { position: 'bottom-center', toastId: 'answer-required' });
      return;
    }

    // 답안 제출
    const response = await createAnswer({
      interviewId: data.interviewId,
      answer,
    });

    navigate(`/interview/result/${response.interviewAnswerId}`);

    setAnswer('');
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
    <div className="flex min-h-full w-full items-center justify-center bg-gradient-to-b from-primary-50 to-white p-6 md:p-10">
      <div className="flex w-[873px] flex-col items-center justify-center gap-12 pt-[94px] sm:pt-16">
        <div className="text-xl font-extrabold md:text-2xl lg:text-3xl">
          <span className="text-primary-600">{`${memberInfo.name}`}</span>
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
  );
};

export default InterviewPage;
