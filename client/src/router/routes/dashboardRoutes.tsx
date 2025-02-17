import { lazy } from 'react';
import DashBoardLayout from '@/layouts/dashboardLayout';
import DashBoardPage from '@/pages/dashboard';

const Recent = lazy(() => import('@/pages/dashboard/recent'));
const Wrong = lazy(() => import('@/pages/dashboard/wrong'));
const Archive = lazy(() => import('@/pages/dashboard/archive'));

const dashBoardRoutes = [
  {
    path: 'dashboard',
    element: <DashBoardLayout />,
    children: [
      {
        index: true,
        element: <DashBoardPage />,
      },
      {
        path: 'recent',
        element: <Recent />,
      },
      {
        path: 'wrong',
        element: <Wrong />,
      },
      {
        path: 'archive',
        element: <Archive />,
      },
    ],
  },
];

export default dashBoardRoutes;
