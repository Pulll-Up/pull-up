import { useEffect } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { postExamAnswer, useGetExamDetails } from '@/api/exam';
import { useExamStore } from '@/stores/examStore';
import { Accordion, AccordionContent, AccordionItem, AccordionTrigger } from '@/components/ui/accordion';
import Timer from '@/components/exam/timer';
import ExamProblem from '@/components/exam/problem';
import InfoSection from '@/components/exam/infoSection';
import ProblemStatusButton from '@/components/exam/infoSection/problemStatusButton';
import Icon from '@/components/common/icon';
import NavigationDialog from '@/components/common/navigationDialog';
import SubmitDialog from '@/components/exam/submitDialog';
import usePrompt from '@/hooks/useNavigationBlocker';

const ExamDetailPage = () => {
  const navigate = useNavigate();
  const { examId } = useParams();
  const validExamId = examId || '';
  const { data: examProblems } = useGetExamDetails(validExamId);
  const answers = useExamStore((state) => state.answers);
  const { resetExamState, setAnswer, setSolutionPage, initializeAndSetOptions } = useExamStore();
  const { isBlocked, handleProceed, handleCancel, setException } = usePrompt();
  const isAllSolved = (examProblems || []).every(
    (problem) => answers[problem.problemId] && answers[problem.problemId].trim() !== '',
  );

  useEffect(() => {
    if (!examProblems) return;
    resetExamState();
    setSolutionPage(false);
    examProblems.forEach((problem) => {
      initializeAndSetOptions(problem.problemId, problem.options);
      setAnswer(problem.problemId, '');
    });
  }, [examProblems, resetExamState, initializeAndSetOptions, setSolutionPage, setAnswer]);

  // 새로고침 감지 이벤트 등록
  useEffect(() => {
    sessionStorage.setItem(`exam_${validExamId}_active`, 'true');
    // 새로고침 감지
    const handleBeforeUnload = (event: BeforeUnloadEvent) => {
      if (sessionStorage.getItem(`exam_${validExamId}_active`) === 'true') {
        sessionStorage.setItem(`exam_${validExamId}_redirect`, 'true');
        event.preventDefault();
      }
    };
    // 페이지 진입 시 리다이렉트 확인
    if (sessionStorage.getItem(`exam_${validExamId}_redirect`) === 'true') {
      sessionStorage.removeItem(`exam_${validExamId}_redirect`);
      navigate('/exam', { replace: true }); // 새로고침 시 /exam 페이지로 이동
    }
    window.addEventListener('beforeunload', handleBeforeUnload);

    return () => {
      // 페이지 떠날 때 상태 초기화
      sessionStorage.removeItem(`exam_${validExamId}_active`);
      window.removeEventListener('beforeunload', handleBeforeUnload);
    };
  }, [validExamId, navigate]);

  const onSubmit = async () => {
    const requestBody = {
      problemAndChosenAnswers: Object.keys(answers).map((problemId) => ({
        problemId,
        chosenAnswer: answers[problemId] ?? '',
      })),
    };
    await postExamAnswer(validExamId, requestBody);
    sessionStorage.removeItem(`exam_${validExamId}_active`); // 시험 종료 시 상태 초기화
    setException();
    navigate(`/exam/${examId}/result`, { state: { fromExamPage: true } });
  };

  const infoSections = [
    {
      id: 'timeLeft',
      title: '남은 시간',
      icon: 'time',
      content: <Timer initialTime={900} onTimeOver={onSubmit} />,
    },
    {
      id: 'problemStatus',
      title: '문제 풀이 현황',
      icon: 'problem',
      content: (
        <div className="grid grid-cols-5 gap-2">
          {examProblems?.map((problem, index) => (
            <ProblemStatusButton
              key={problem.problemId}
              index={index + 1}
              status={answers[problem.problemId] ? 'solved' : 'default'}
              onClick={() => {
                document
                  .getElementById(`problem-${problem.problemId}`)
                  ?.scrollIntoView({ behavior: 'smooth', block: window.innerWidth >= 768 ? 'center' : 'end' });
              }}
            />
          ))}
        </div>
      ),
    },
  ];

  return (
    <div className="flex gap-12 bg-Main md:px-16 md:py-10">
      <div className="relative flex w-full flex-col gap-4 sm:mt-16 md:flex-row">
        {/* Info Section - Mobile View */}
        <section className="sticky top-2 border border-b-2 bg-white px-10 pb-2 pt-[86px] sm:top-16 sm:pt-[8px] md:hidden">
          <Accordion type="single" defaultValue="timeLeft" collapsible>
            {infoSections.map(({ id, title, icon, content }) => (
              <AccordionItem key={id} value={id}>
                <AccordionTrigger>
                  <div className="flex items-center gap-2">
                    <Icon id={icon} size={20} className="h-auto md:w-6 lg:w-7" />
                    <span className="text-lg font-semibold text-stone-900 md:text-xl lg:text-2xl">{title}</span>
                  </div>
                </AccordionTrigger>
                <AccordionContent>
                  <InfoSection>{content}</InfoSection>
                </AccordionContent>
              </AccordionItem>
            ))}
          </Accordion>
        </section>

        {/* 문제 리스트 */}
        <section className="flex-2 flex w-full flex-col gap-6 px-10 md:w-[920px] md:gap-10">
          {examProblems?.map((problem, index) => (
            <div key={problem.problemId} id={`problem-${problem.problemId}`}>
              <ExamProblem
                index={index + 1}
                problem={{
                  problemId: problem.problemId,
                  question: problem.problem,
                  subject: problem.subject,
                  questionType: problem.problemType,
                  options: problem.options,
                }}
              />
            </div>
          ))}
        </section>

        <aside className="relative min-w-[280px] flex-1 flex-shrink-0 px-10 py-4 md:p-0 lg:min-w-[340px] xl:max-w-[380px]">
          {/* Info Section - Web View */}
          <div className="sticky top-24 flex flex-col gap-10">
            <div className="hidden flex-col gap-10 md:flex">
              {infoSections.map(({ id, title, icon, content }) => (
                <InfoSection key={id} title={title} icon={icon}>
                  {content}
                </InfoSection>
              ))}
            </div>
            {/* 답안 제출 */}
            <SubmitDialog
              onSubmit={onSubmit}
              isDisabled={!isAllSolved}
              title="정말 시험을 제출하시겠습니까?"
              description="제출 후에는 더 이상 답안을 수정할 수 없습니다."
            />
          </div>
        </aside>
      </div>
      {/* 페이지 이동 및 새로고침 경고 모달 */}
      <NavigationDialog
        isOpen={isBlocked}
        onProceed={handleProceed}
        onCancel={handleCancel}
        title="시험을 중단하시겠습니까?"
        description="페이지를 이동할 경우 시험은 0점으로 처리되며 선택한 답안이 모두 사라집니다."
      />
    </div>
  );
};

export default ExamDetailPage;
