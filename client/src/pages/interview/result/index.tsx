import { useGetInterviewList, useGetInterviewResult } from '@/api/interview';
import SearchModal from '@/components/interview/searchModal';
import SideMenu from '@/components/interview/sideMenu';
import InterviewFeedback from '@/components/interview/interviewFeedback';
import { useState } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { Tabs, TabsContent, TabsList, TabsTrigger } from '@radix-ui/react-tabs';
import InterviewMyAnswer from '@/components/interview/myAnswer';
import convertDate from '@/utils/convertDate';
import Icon from '@/components/common/icon';
import { toast } from 'react-toastify';
import LoadingLayout from '@/layouts/loadingLayout';

const InterviewResultPage = () => {
  const navigate = useNavigate();
  const { interviewAnswerId } = useParams();
  const { data: result, isLoading, isError } = useGetInterviewResult(Number(interviewAnswerId));
  const { data: interviewList } = useGetInterviewList();

  const [isSideMenuOpen, setIsSideMenuOpen] = useState(false);
  const [isModalOpen, setIsModalOpen] = useState(false);

  // 결과 분석 오류
  if (isError) {
    navigate('/');
    toast.error('존재하지 않는 페이지입니다.', {
      position: 'bottom-center',
      toastId: 'not-found',
    });

    return;
  }

  // 결과 분석 로딩
  if (isLoading) {
    return <LoadingLayout />;
  } else {
    if (!result || !interviewList) return null;
  }

  const handleMenuClick = () => {
    setIsSideMenuOpen(!isSideMenuOpen);
  };

  // 다른 사람 답변 보기
  const onButtonClick = () => {
    navigate(`/interview/${result.interviewId}/answers`);
  };

  // 지난 문제 보기
  const onInterviewClick = (interviewAnswerId: number) => {
    setIsModalOpen(false);
    navigate(`/interview/result/${interviewAnswerId}`);
  };

  const formatDate = convertDate(result.createdAt).split('-');

  return (
    <div className="flex min-h-full bg-Main p-6 md:p-10">
      {/* 사이드바 넓이 만큼 왼쪽 마진 조절 */}
      <div
        className={`relative flex flex-1 pt-[94px] transition-all duration-500 sm:pt-16 ${
          isSideMenuOpen ? 'md:ml-[280px] lg:ml-[300px]' : 'ml-0'
        }`}
      >
        <SideMenu
          isOpen={isSideMenuOpen}
          interviewList={interviewList}
          handleMenuClick={handleMenuClick}
          handleSearchClick={() => setIsModalOpen(true)}
          onInterviewClick={onInterviewClick}
        />

        {/* 데스크탑 뷰 (lg 이상) */}
        <div className="hidden w-full flex-col gap-8 lg:flex lg:flex-row">
          <div className="flex flex-[4.5] flex-col gap-6">
            <button className="flex items-center gap-4">
              {!isSideMenuOpen ? (
                <Icon id="menu" size={20} onClick={() => setIsSideMenuOpen(true)} className="h-auto md:w-6" />
              ) : null}
              <span className="text-xl font-semibold md:text-2xl">
                {`${formatDate[0]}년 ${formatDate[1]}월 ${formatDate[2]}일`}
              </span>
            </button>
            <InterviewMyAnswer question={result.question} answer={result.memberAnswer} onButtonClick={onButtonClick} />
          </div>
          <div className="flex flex-[5.5]">
            <InterviewFeedback
              keywords={result.keywords}
              strength={result.strength}
              weakness={result.weakness}
              answer={result.answer}
            />
          </div>
        </div>

        {/* 모바일/태블릿 뷰 (md 미만) */}
        <div className="flex w-full flex-col gap-6 lg:hidden">
          <button className="flex items-center gap-4">
            {!isSideMenuOpen ? (
              <Icon id="menu" size={20} onClick={() => setIsSideMenuOpen(true)} className="h-auto md:w-6" />
            ) : null}
            <span className="text-xl font-semibold md:text-2xl">
              {`${formatDate[0]}년 ${formatDate[1]}월 ${formatDate[2]}일`}
            </span>
          </button>
          <Tabs defaultValue="myAnswer" className="w-full">
            <TabsList className="mb-6 grid w-full grid-cols-2 rounded-lg bg-stone-50 p-1 shadow-sm">
              <TabsTrigger
                value="myAnswer"
                className="rounded-lg p-2 transition-all duration-200 data-[state=active]:bg-white data-[state=active]:font-semibold data-[state=inactive]:text-stone-700"
              >
                나의 답변
              </TabsTrigger>
              <TabsTrigger
                value="analyze"
                className="rounded-lg p-2 transition-all duration-200 data-[state=active]:bg-white data-[state=active]:font-semibold data-[state=inactive]:text-stone-700"
              >
                답변 분석
              </TabsTrigger>
            </TabsList>
            <TabsContent value="myAnswer">
              <InterviewMyAnswer
                question={result.question}
                answer={result.memberAnswer}
                onButtonClick={onButtonClick}
              />
            </TabsContent>
            <TabsContent value="analyze">
              <InterviewFeedback
                keywords={result.keywords}
                strength={result.strength}
                weakness={result.weakness}
                answer={result.answer}
              />
            </TabsContent>
          </Tabs>
        </div>

        {isModalOpen && <SearchModal onClose={() => setIsModalOpen(false)} onInterviewClick={onInterviewClick} />}
      </div>
    </div>
  );
};

export default InterviewResultPage;
