import ExamAnswer from './examAnswer';
import Icon from '@/components/common/icon';
import { useToggleProblemBookmark } from '@/hooks/useToggleBookmark';
import { useExamStore } from '@/stores/examStore';
import { convertSubject } from '@/utils/convertSubject';
import { PageType } from '@/utils/pageType';
import { useParams } from 'react-router-dom';

interface ExamProblemProps {
  index?: number;
  problem: {
    problemId: string;
    question: string;
    subject: string;
    questionType: 'SHORT_ANSWER' | 'MULTIPLE_CHOICE';
    chosenAnswer?: string;
    options?: string[];
    answer?: string;
    bookmarkStatus?: boolean;
  };
}

const ExamProblem = ({ index, problem }: ExamProblemProps) => {
  const { isSolutionPage } = useExamStore();
  const { isExamResultPage } = PageType();
  const { examId } = useParams();
  const validExamId = isExamResultPage ? examId : '';

  const toggleBookmarkMutation = useToggleProblemBookmark(problem.problemId, validExamId);
  const handleBookmark = () => {
    toggleBookmarkMutation.mutate();
  };

  return (
    <div className="flex flex-col gap-7 rounded-xl border border-primary-200 bg-white p-7">
      {/* 질문 섹션 */}
      <div className="flex flex-col gap-2">
        <div className="flex items-center justify-between">
          <div className="flex cursor-pointer items-center gap-2">
            <span className="text-lg font-bold text-stone-900 md:text-xl lg:text-2xl">문제 {index}</span>
            {isSolutionPage && (
              <button onClick={handleBookmark} aria-label={problem.bookmarkStatus ? '북마크 해제' : '북마크 추가'}>
                <Icon id={problem.bookmarkStatus ? 'bookmark' : 'bookmark-empty'} size={24} />
              </button>
            )}
          </div>
          <div className="rounded-lg border border-secondary-600 bg-secondary-50 px-3 py-1 text-secondary-600">
            {convertSubject(problem.subject)}
          </div>
        </div>
        <span className="text-lg font-semibold md:text-xl lg:text-2xl">{problem.question}</span>
      </div>
      {/* 답안 선택 섹션 */}
      <ExamAnswer questionType={problem.questionType} problemId={problem.problemId} />
    </div>
  );
};

export default ExamProblem;
