export const PAGE_PATTERNS = {
  EXAM_RESULT: /^\/exam\/\d+\/result$/, // examId가 포함된 examResultPage
  PROBLEM_DETAIL: /^\/exam\/problem\/\d+$/, // problemDetailPage
} as const;
