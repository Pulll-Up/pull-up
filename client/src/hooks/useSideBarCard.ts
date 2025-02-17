import { useGetExamAll } from '@/api/exam';
import { useGetArchivedProblemAll, useGetRecentWrongProblem } from '@/api/problem';
import { convertSubject } from '@/utils/convertSubject';

function useSideBarCard(limit: number = 10) {
  const { data: recentWrongProblems } = useGetRecentWrongProblem();
  const { data: archivedProblems } = useGetArchivedProblemAll();
  const { data: examAll } = useGetExamAll();

  // 문제 데이터 가공(recent, archived)
  function getProcessedProblemList(
    problemDtos?: { problemId: string; question: string; subject: string }[],
    defaultMessage: string = '문제 데이터가 없습니다.',
  ) {
    if (!problemDtos || problemDtos.length === 0) {
      return [{ id: 0, content: defaultMessage, date: '', subjects: [] }];
    }
    return problemDtos.slice(0, Math.min(problemDtos.length, limit)).map((item) => ({
      id: item.problemId,
      content: item.question,
      subjects: Array.isArray(item.subject) ? item.subject : [convertSubject(item.subject)],
    }));
  }

  // examData 가공
  const recentExamList =
    Array.isArray(examAll?.getExamResponses) && examAll.getExamResponses.length > 0
      ? examAll.getExamResponses.slice(0, 1).map((item) => ({
          id: item.examId,
          content: item.examName,
          subjects: convertSubject(item.subjects),
        }))
      : [{ id: 0, content: '최근 푼 모의고사 기록이 없습니다.', subjects: [] }];

  const wrongProblemList = getProcessedProblemList(recentWrongProblems?.recentWrongQuestionDtos);
  const archiveProblemList = getProcessedProblemList(archivedProblems?.bookmarkedProblemDtos);

  return { recentExamList, wrongProblemList, archiveProblemList };
}

export default useSideBarCard;
