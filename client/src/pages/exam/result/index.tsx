import { lazy, Suspense, useEffect } from 'react';
import InfoSection from '@/components/exam/infoSection';
import SubmitButton from '@/components/common/submitButton';
import ProblemStatusButton from '@/components/exam/infoSection/problemStatusButton';
import { useNavigate, useParams } from 'react-router-dom';
import { useGetExamResult } from '@/api/exam';
import { useExamStore } from '@/stores/examStore';
import { Accordion, AccordionContent, AccordionItem, AccordionTrigger } from '@/components/ui/accordion';
import Icon from '@/components/common/icon';

const ExamProblem = lazy(() => import('@/components/exam/problem'));
const ExamSolution = lazy(() => import('@/components/exam/solution'));

const ExamResultPage = () => {
  const navigate = useNavigate();
  const { examId } = useParams();
  const { setSolutionPage, initializeAndSetOptions, setAnswer } = useExamStore();
  const { data: examResult } = useGetExamResult(examId!);
  useEffect(() => {
    setSolutionPage(true); // 결과 페이지로 설정
    // 각 문제에 대해 상태 초기화
    examResult.examResultDetailDtos.forEach((problem) => {
      initializeAndSetOptions(problem.problemId, problem.options, {
        answer: problem.answer,
        chosenAnswer: problem.chosenAnswer,
      });
      // 선택한 답변 저장
      if (problem.chosenAnswer) {
        setAnswer(problem.problemId, problem.chosenAnswer);
      }
    });
  }, [examResult, initializeAndSetOptions, setAnswer, setSolutionPage]);

  if (!examResult) {
    return <div>시험 결과를 불러오는 데 실패했습니다.</div>;
  }
  const { round, score, examResultDetailDtos } = examResult;
  const infoSections = [
    {
      id: 'score',
      title: '점수',
      icon: 'score',
      content: (
        <div className="text-2xl lg:text-3xl">
          <span className="text-primary-500">{score}</span> / 100
        </div>
      ),
    },
    {
      id: 'problemStatus',
      title: '문제 풀이 현황',
      icon: 'problem',
      content: (
        <div className="grid grid-cols-5 gap-3">
          {examResultDetailDtos.map((problem, index) => (
            <ProblemStatusButton
              index={index + 1}
              key={problem.problemId}
              status={problem.answerStatus ? 'correct' : 'wrong'}
              onClick={() => {
                document
                  .getElementById(`problem-${problem.problemId}`)
                  ?.scrollIntoView({ behavior: 'smooth', block: 'center' });
              }}
            />
          ))}
        </div>
      ),
    },
  ];

  return (
    <div className="flex min-h-screen gap-12 bg-Main md:px-8 md:py-10">
      <div className="relative flex w-full flex-col gap-4 sm:mt-16 md:flex-row md:justify-center">
        {/* Info Section*/}
        <section className="sticky top-2 border border-b-2 bg-white px-10 pb-2 pt-[86px] sm:pt-[8px] md:hidden">
          <Accordion type="single" defaultValue="score" collapsible>
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
        {/* Problem & Solution Section */}
        <section className="flex-2 flex w-full flex-col gap-6 px-10 md:w-[920px] md:gap-10">
          <Suspense fallback={<h2>문제 로딩 중!!</h2>}>
            {examResultDetailDtos.map((problem, index) => (
              <div key={problem.problemId} id={`problem-${problem.problemId}`} className="flex flex-col gap-2">
                <ExamProblem
                  index={index + 1}
                  problem={{
                    problemId: problem.problemId,
                    question: problem.problem,
                    subject: problem.subject,
                    questionType: problem.problemType,
                    options: problem.options,
                    chosenAnswer: problem.chosenAnswer,
                    bookmarkStatus: problem.bookmarkStatus,
                  }}
                />
                <ExamSolution
                  answer={problem.answer}
                  correctRate={problem.correctRate}
                  explanation={problem.explanation}
                />
              </div>
            ))}
          </Suspense>
        </section>
        {/* Info Section - Web View */}
        <aside className="relative min-w-[280px] flex-1 flex-shrink-0 px-10 py-4 md:p-0 lg:min-w-[340px] xl:max-w-[380px]">
          <div className="sticky top-10 flex flex-col gap-10">
            <div className="hidden flex-col gap-10 md:flex">
              <InfoSection>
                <span className="text-xl md:text-2xl lg:text-3xl">{round}</span>
              </InfoSection>
              {infoSections.map(({ id, title, icon, content }) => (
                <InfoSection key={id} title={title} icon={icon}>
                  {content}
                </InfoSection>
              ))}
            </div>
            <SubmitButton
              text="확인 완료"
              onClick={() => {
                navigate(`/dashboard`);
              }}
            />
          </div>
        </aside>
      </div>
    </div>
  );
};

export default ExamResultPage;
