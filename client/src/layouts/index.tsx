import { useAuth } from '@/api/auth';
import { useGetMember } from '@/api/member';
import Header from '@/components/common/header/Header';
import MobileHeader from '@/components/common/header/MobileHeader';
import PageSpinner from '@/components/fallbacks/spinners/PageSpinner';
import { cn } from '@/lib/utils';
import { Suspense, useEffect } from 'react';
import { Outlet, useLocation, useNavigate } from 'react-router';
import { toast } from 'react-toastify';

const SM_STYLE = 'mx-auto max-w-[430px]';
const BASIC_STYLE = 'sm:mx-0 sm:max-w-full w-full h-screen';

const MainLayout = () => {
  const location = useLocation();
  const navigate = useNavigate();
  const { data, isLoading } = useGetMember();

  const { isLoggedIn } = useAuth();

  useEffect(() => {
    if (!isLoading && !data?.interestSubjects.length) {
      navigate('/signup');
      return;
    }

    if (
      location.pathname !== '/' &&
      location.pathname !== '/signin' &&
      location.pathname !== '/signup' &&
      location.pathname !== '/redirect' &&
      !isLoggedIn
    ) {
      toast.error('로그인이 필요합니다.', { position: 'bottom-center', toastId: 'login-required' });
      navigate('/signin');
      return;
    }
  }, [isLoggedIn, location.pathname, isLoading]);

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
