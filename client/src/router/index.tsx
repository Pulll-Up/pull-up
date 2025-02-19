import { createBrowserRouter } from 'react-router-dom';
import dashBoardRoutes from './routes/dashboardRoutes';
import interviewRoutes from './routes/interviewRoutes';
import gameRoutes from './routes/gameRoutes';
import GameLayout from '@/layouts/gameLayout';
import MainLayout from '@/layouts/mainLayout';
import { AuthCheck } from '@/layouts/authCheck';
import etcRoutes from './routes/etcRoutes';
import examRoutes from './routes/examRoutes';

const routes = [
  {
    element: <AuthCheck />,
    children: [
      {
        path: '/',
        element: <MainLayout />,
        children: [...dashBoardRoutes, ...interviewRoutes, ...examRoutes],
      },
      {
        path: '/game',
        element: <GameLayout />,
        children: [...gameRoutes],
      },
    ],
  },
  // 인증이 필요없는 페이지들
  {
    path: '/',
    element: <MainLayout />,
    children: [...etcRoutes],
  },
];

const router = createBrowserRouter(routes);
export default router;
