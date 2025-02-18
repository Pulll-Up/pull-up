import { useAuth } from '@/api/auth';
import { useGetInterviewResult } from '@/api/interview';
import Icon from '@/components/common/icon';
import { InterviewResultResponse } from '@/types/response/interview';
import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';

const DashboardFeedback = () => {
  const navigate = useNavigate();
  const { authInfo } = useAuth();
  const { data: result, isLoading } = useGetInterviewResult(authInfo?.interviewAnswerId ?? '0');

  const [resultData, setResultData] = useState<InterviewResultResponse>({
    interviewId: '1',
    question: '프로세스와 스레드의 차이에 대해서 설명해주세요.',
    memberAnswer:
      '프로세스는 운영체제에서 독립적으로 실행되는 프로그램의 단위이며, 스레드는 프로세스 내에서 실행되는 작은 작업 단위입니다. 각 프로세스는 독립적인 메모리 공간을 가지고 있지만, 스레드는 프로세스 내에서 메모리를 공유합니다.',
    keywords: ['프로세스', '스레드', '메모리 공유'],
    strength:
      '프로세스와 스레드의 정의를 정확하게 설명하였고, 스레드가 프로세스 내에서 메모리를 공유한다는 점을 명확히 언급하였습니다. 또한, 각 프로세스가 독립적인 메모리 공간을 갖는다는 점도 잘 설명하였습니다.',
    weakness:
      '프로세스 간의 통신 방법(IPC)에 대한 설명이 부족했습니다. 또한, 스레드 간의 동기화 문제와 관련된 예시를 보완하면 더 완벽한 답변이 될 것입니다.',
    answer:
      '프로세스는 운영체제에서 독립적으로 실행되는 프로그램 단위로, 각 프로세스는 자신만의 메모리 공간(코드, 데이터, 힙, 스택)을 갖습니다. 반면, 스레드는 프로세스 내에서 실행되는 작업 단위로, 같은 프로세스 내의 다른 스레드들과 메모리 공간을 공유합니다. 프로세스 간에는 메모리를 공유하지 않으므로 통신을 위해 IPC(Inter-Process Communication)가 필요하지만, 스레드는 같은 메모리 공간을 공유하여 빠른 통신이 가능합니다. 그러나 이로 인해 동기화 문제가 발생할 수 있으므로 적절한 동기화 기법이 필요합니다.',
    createdAt: '2025-02-05T14:28:35.123456789',
  });

  useEffect(() => {
    if (authInfo?.isSolvedToday && result) {
      setResultData(result);
    }
  }, [authInfo?.isSolvedToday, result]);

  if (isLoading) return <>불러오는 중...</>;

  return (
    <div className="relative flex h-full w-full flex-col gap-3 lg:flex-row">
      <div className="flex w-full flex-1 flex-col gap-3">
        {/* 문제 */}
        <div className="flex flex-1 items-center justify-center rounded-xl border border-stone-200 p-6">
          <span className="break-keep text-center text-lg font-bold">{resultData.question}</span>
        </div>
        {/* 키워드 */}
        <div className="flex flex-1 flex-col items-center justify-center gap-2 rounded-xl border border-stone-200 p-6">
          <div className="w-fit rounded-md bg-primary-500 px-3 py-1 text-sm text-white">키워드</div>
          <div className="flex flex-wrap justify-center gap-3">
            {resultData.keywords.map((keyword, id) => (
              <div key={id}>{keyword}</div>
            ))}
          </div>
        </div>
      </div>
      {/* 강점. 약점 */}
      <div className="flex flex-1 flex-col gap-3 rounded-xl border border-stone-200 p-5">
        <div className="flex items-center justify-between">
          <div className="flex gap-1">
            <Icon id={'ai'} size={24} />
            <span className="text-xl font-semibold text-stone-950">AI 피드백</span>
          </div>

          <button
            onClick={() => navigate(`/interview/result/${authInfo?.interviewAnswerId}`)}
            className="flex w-fit items-center justify-center gap-2 rounded-lg border border-primary-500 bg-primary-50 px-2 py-1 font-semibold text-primary-500"
          >
            <div>자세히 보기</div>
            <Icon id={'list'} size={16}></Icon>
          </button>
        </div>
        <section>
          <span className="text-lg font-semibold text-primary-500">강점</span>
          <p>{resultData.strength}</p>
        </section>
        <section>
          <span className="text-lg font-semibold text-primary-500">개선점</span>
          <p>{resultData.weakness}</p>
        </section>
      </div>

      {!authInfo?.isSolvedToday && (
        <div className="absolute inset-0 flex flex-col items-center justify-center bg-white bg-opacity-80">
          <p className="text-lg font-bold text-primary-500">아직 문제를 풀지 않았어요!</p>
          <button
            onClick={() => navigate('/interview')}
            className="mt-4 w-[80%] rounded-xl bg-primary-500 p-3 text-lg font-semibold text-white lg:text-xl lg:font-bold"
          >
            오늘의 문제 풀러 가기
          </button>
        </div>
      )}
    </div>
  );
};

export default DashboardFeedback;
