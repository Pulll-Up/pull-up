import { createMemberAnswer, useGetInterview } from '@/api/interview';
import { useGetMemberInfo } from '@/api/member';
import InputForm from '@/components/interview/inputForm';
import InterviewCard from '@/components/interview/interviewCard';
import { memberStore } from '@/stores/memberStore';
import { InterviewResponse } from '@/types/interview';
import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';

const InterviewPage = () => {
  const { data: member } = useGetMemberInfo();
  const { setIsSolvedToday, setInterviewId, setInteverviewAnswerId } = memberStore();

  const navigate = useNavigate();
  const [hint, setHint] = useState(false);
  const [interviewAnswer, setInterviewAnswer] = useState(''); // 답변
  const { data: interview } = useGetInterview();
  const [interviewData, setInterviewData] = useState<InterviewResponse>({
    interviewId: 1,
    question: 'Checked Exception과 Unchecked Exception의 차이는 ?',
    keywords: ['Java', 'Exception'],
  });

  useEffect(() => {
    if (interview) {
      setInterviewData(interview);
    }
  }, [interview, member]);

  if (!interviewData) return null;

  const onHintClick = () => {
    setHint(!hint);
  };

  const onChange = (e: React.ChangeEvent<HTMLTextAreaElement>) => {
    setInterviewAnswer(e.target.value);
  };

  const onSubmit = async () => {
    if (!interviewAnswer) {
      alert('답안을 입력하세요.');
      return;
    }

    const data = await createMemberAnswer(interviewData.interviewId, interviewAnswer);
    setIsSolvedToday(true);
    setInterviewId(data.interviewAnswerId);
    setInteverviewAnswerId(data.interviewAnswerId);
    navigate(`/interview/result/${data.interviewAnswerId}`);

    console.log('제출 답안: ', interviewAnswer);
    setInterviewAnswer('');
  };

  const onKeyDown = (e: React.KeyboardEvent<HTMLTextAreaElement>) => {
    if (e.key === 'Enter' && !e.shiftKey) {
      e.preventDefault();
      onSubmit();
    }
  };

  return (
    <div className="flex min-h-full w-full items-center justify-center bg-gradient-to-b from-primary-50 to-white p-6 md:p-10">
      <div className="flex w-[873px] flex-col items-center justify-center gap-12 pt-[94px] sm:pt-16">
        <div className="text-xl font-extrabold md:text-2xl lg:text-3xl">
          <span className="text-primary-600">{`${member?.name}`}</span>
          <span>님 만을 위한 오늘의 맞춤 문제🎯</span>
        </div>
        <InterviewCard
          title={interviewData.question}
          keywords={interviewData.keywords}
          hint={hint}
          onHintClick={onHintClick}
        />
        <div className="flex w-full flex-col justify-start gap-2">
          <span className="text-lg font-semibold lg:text-xl">나의 답변</span>
          <InputForm
            id="todayQuestion"
            placeholder="질문에 대한 답을 남겨주세요."
            value={interviewAnswer}
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
