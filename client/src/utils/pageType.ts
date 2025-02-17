import { PAGE_PATTERNS } from '@/constants/exam';
import { useLocation } from 'react-router-dom';

export const PageType = () => {
  const { pathname } = useLocation();

  return {
    isExamResultPage: PAGE_PATTERNS.EXAM_RESULT.test(pathname),
    isProblemDetailPage: PAGE_PATTERNS.PROBLEM_DETAIL.test(pathname),
  };
};
