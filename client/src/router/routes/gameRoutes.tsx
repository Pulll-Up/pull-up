import { lazy } from 'react';
import GamePage from '@/pages/game';

const GameResultPage = lazy(() => import('@/pages/game/gameResult'));
const GameStage = lazy(() => import('@/pages/game/gameStage'));

const gameRoutes = [
  {
    index: true,
    element: <GamePage />,
  },
  {
    path: ':gameId',
    element: <GameStage />,
  },
  {
    path: 'result',
    element: <GameResultPage />,
  },
];

export default gameRoutes;
