import { lazy } from 'react';
import ExamPage from '@/pages/exam';

const ExamDetailPage = lazy(() => import('@/pages/exam/detail'));
const ExamResultPage = lazy(() => import('@/pages/exam/result'));
const ProblemDetail = lazy(() => import('@/pages/exam/problemDetail'));

const examRoutes = [
  {
    path: 'exam',
    children: [
      {
        index: true,
        element: <ExamPage />,
      },
      {
        path: ':examId',
        element: <ExamDetailPage />,
      },
      {
        path: ':examId/result',
        element: <ExamResultPage />,
      },
      {
        path: 'problem/:problemId',
        element: <ProblemDetail />,
      },
    ],
  },
];

export default examRoutes;
