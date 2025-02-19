import Header from '@/components/common/header/Header';
import MobileHeader from '@/components/common/header/MobileHeader';
import PageSpinner from '@/components/fallbacks/spinners/PageSpinner';
import { cn } from '@/lib/utils';
import { Suspense } from 'react';
import { Outlet } from 'react-router';

const SM_STYLE = 'mx-auto max-w-[430px]';
const BASIC_STYLE = 'sm:mx-0 sm:max-w-full w-full h-screen';

const MainLayout = () => {
  return (
    <div className={cn(SM_STYLE, BASIC_STYLE)}>
      <Header />
      <MobileHeader />
      <main className="h-full w-full">
        <Suspense fallback={<PageSpinner />}>
          <Outlet />
        </Suspense>
      </main>
    </div>
  );
};

export default MainLayout;
