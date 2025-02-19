import { createBrowserRouter } from 'react-router-dom';
import dashBoardRoutes from './routes/dashboardRoutes';
import interviewRoutes from './routes/interviewRoutes';
import gameRoutes from './routes/gameRoutes';
import GameLayout from '@/layouts/gameLayout';
import MainLayout from '@/layouts/mainLayout';
import { AuthCheck } from '@/layouts/authCheck';
import examRoutes from './routes/examRoutes';
import etcRoutes from './routes/etcRoutes';

const routes = [
  // 인증이 필요없는 페이지
  {
    path: '/',
    element: <MainLayout />,
    children: [...etcRoutes],
  },
  // 인증이 필요한 페이지
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
];

const router = createBrowserRouter(routes);
export default router;
