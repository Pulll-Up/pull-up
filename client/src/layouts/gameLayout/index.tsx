import Header from '@/components/common/header/Header';
import MobileHeader from '@/components/common/header/MobileHeader';
import PageSpinner from '@/components/fallbacks/spinners/PageSpinner';
import { cn } from '@/lib/utils';
import { useWebSocketStore } from '@/stores/useWebSocketStore';
import { Suspense, useEffect } from 'react';
import { Outlet, useLocation } from 'react-router';

const SM_STYLE = 'mx-auto max-w-[430px]';
const BASIC_STYLE = 'sm:mx-0 sm:max-w-full w-full h-screen';

const GameLayout = () => {
  const location = useLocation();

  const { connectWebSocket, disconnectWebSocket } = useWebSocketStore();

  useEffect(() => {
    connectWebSocket();

    return () => {
      disconnectWebSocket();
    };
  }, []);

  const hideHeader = /^\/game\/[^/]+$/.test(location.pathname);

  return (
    <div className={cn(SM_STYLE, BASIC_STYLE)}>
      <Header />
      {hideHeader ? <></> : <MobileHeader />}
      <main className="h-full w-full">
        <Suspense fallback={<PageSpinner />}>
          <Outlet />
        </Suspense>
      </main>
    </div>
  );
};

export default GameLayout;
