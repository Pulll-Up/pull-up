import { toggleProblemBookmark } from '@/api/problem';
import { queryClient } from '@/main';
import { ExamResultResponse } from '@/types/exam';
import { ProblemDetail } from '@/types/problem';
import { useMutation } from '@tanstack/react-query';

const QUERY_KEYS = {
  PROBLEM_DETAIL: (problemId: string) => ['problemDetail', problemId],
  EXAM_RESULT: (examId: string) => ['examResult', examId],
};

export const useToggleProblemBookmark = (problemId: string, examId?: string) => {
  return useMutation({
    mutationFn: () => toggleProblemBookmark(problemId),
    onMutate: async () => {
      const previousProblemDetail = queryClient.getQueryData<ProblemDetail>(QUERY_KEYS.PROBLEM_DETAIL(problemId));
      const previousExamResult = examId
        ? queryClient.getQueryData<ExamResultResponse>(QUERY_KEYS.EXAM_RESULT(examId))
        : undefined;

      queryClient.setQueryData<ProblemDetail>(QUERY_KEYS.PROBLEM_DETAIL(problemId), (data) =>
        data ? { ...data, bookmarkStatus: !data.bookmarkStatus } : data,
      );

      if (examId) {
        queryClient.setQueryData<ExamResultResponse>(QUERY_KEYS.EXAM_RESULT(examId), (data) => {
          if (!data) return data;
          return {
            ...data,
            examResultDetailDtos: data.examResultDetailDtos.map((detail) =>
              detail.problemId === problemId ? { ...detail, bookmarkStatus: !detail.bookmarkStatus } : detail,
            ),
          };
        });
      }

      const cancelPromises = [queryClient.cancelQueries({ queryKey: QUERY_KEYS.PROBLEM_DETAIL(problemId) })];
      if (examId) {
        cancelPromises.push(queryClient.cancelQueries({ queryKey: QUERY_KEYS.EXAM_RESULT(examId) }));
      }
      await Promise.all(cancelPromises);

      return { previousProblemDetail, previousExamResult };
    },
    onError: (error, _, context) => {
      console.error('북마크 토글 실패:', error);
      if (context?.previousProblemDetail) {
        queryClient.setQueryData(QUERY_KEYS.PROBLEM_DETAIL(problemId), context.previousProblemDetail);
      }
      if (examId && context?.previousExamResult) {
        queryClient.setQueryData(QUERY_KEYS.EXAM_RESULT(examId), context.previousExamResult);
      }
    },
    onSettled: () => {
      const invalidatePromises = [queryClient.invalidateQueries({ queryKey: QUERY_KEYS.PROBLEM_DETAIL(problemId) })];
      if (examId) {
        invalidatePromises.push(queryClient.invalidateQueries({ queryKey: QUERY_KEYS.EXAM_RESULT(examId) }));
      }
      Promise.all(invalidatePromises);
    },
  });
};
