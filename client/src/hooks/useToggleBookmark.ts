import { toggleProblemBookmark } from '@/api/problem';
import { queryClient } from '@/main';
import { ExamResultResponse } from '@/types/exam';
import { ProblemDetail } from '@/types/problem';
import { useMutation } from '@tanstack/react-query';

const QUERY_KEYS = {
  PROBLEM_DETAIL: (problemId: number) => ['problemDetail', problemId],
  EXAM_RESULT: (examId: number) => ['examResult', examId],
};

export const useTogglProblemBookmark = (problemId: number, examId?: number) => {
  const updateCache = <T>(queryKey: (string | number)[], updateFn: (data: T | undefined) => T | undefined) => {
    queryClient.setQueryData(queryKey, updateFn);
  };

  const handleOptimisticUpdate = () => {
    updateCache<ProblemDetail>(QUERY_KEYS.PROBLEM_DETAIL(problemId), (data) => {
      if (data) {
        return { ...data, bookmarkStatus: !data.bookmarkStatus }; // ✅ store 없이 queryClient만 업데이트
      }
      return data;
    });

    if (examId) {
      updateCache<ExamResultResponse>(QUERY_KEYS.EXAM_RESULT(examId), (data) => {
        if (data) {
          return {
            ...data,
            examResultDetailDtos: data.examResultDetailDtos.map((detail) =>
              detail.problemId === problemId ? { ...detail, bookmarkStatus: !detail.bookmarkStatus } : detail,
            ),
          };
        }
        return data;
      });
    }
  };

  return useMutation({
    mutationFn: () => toggleProblemBookmark(problemId),
    onMutate: async () => {
      const previousProblemDetail = queryClient.getQueryData<ProblemDetail>(QUERY_KEYS.PROBLEM_DETAIL(problemId));
      const previousExamResult = examId
        ? queryClient.getQueryData<ExamResultResponse>(QUERY_KEYS.EXAM_RESULT(examId))
        : undefined;

      handleOptimisticUpdate();

      await Promise.all(
        [
          queryClient.cancelQueries({ queryKey: QUERY_KEYS.PROBLEM_DETAIL(problemId) }),
          examId && queryClient.cancelQueries({ queryKey: QUERY_KEYS.EXAM_RESULT(examId) }),
        ].filter(Boolean),
      );

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
      queryClient.invalidateQueries({ queryKey: QUERY_KEYS.PROBLEM_DETAIL(problemId) });
      if (examId) {
        queryClient.invalidateQueries({ queryKey: QUERY_KEYS.EXAM_RESULT(examId) });
      }
    },
  });
};
