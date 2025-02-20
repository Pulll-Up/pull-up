import { lazy } from 'react';
import GamePage from '@/pages/game';
import { ProtectedRoute } from '../ProtectRoute';

const GameResultPage = lazy(() => import('@/pages/game/gameResult'));
const GameStage = lazy(() => import('@/pages/game/gameStage'));

const gameRoutes = [
  {
    index: true,
    element: <GamePage />,
  },
  {
    path: ':gameId',
    element: (
      <ProtectedRoute blockedPath="/game/" redirectPath="/game" blockedText="새로고침이나 직접 접근이 감지되었습니다.">
        <GameStage />
      </ProtectedRoute>
    ),
  },
  {
    path: 'result',
    element: (
      <ProtectedRoute blockedPath="/game/" redirectPath="/game" blockedText="새로고침이나 직접 접근이 감지되었습니다.">
        <GameResultPage />
      </ProtectedRoute>
    ),
  },
];

export default gameRoutes;
