import { logout, useGetAuthInfo } from '@/api/auth';
import { cn } from '@/lib/utils';
import { Link, useLocation } from 'react-router-dom';

interface HeaderItem {
  label: string;
  path: string;
}

const MobileHeader = () => {
  const location = useLocation();
  const isExamInProgress = /^\/exam\/\d+$/.test(location.pathname);
  const isGameInProgress = /^\/game\/(?!$).+$/.test(location.pathname);

  const { authInfo, isAuthorized } = useGetAuthInfo();

  const headerItems: HeaderItem[] = [
    {
      label: '오늘의 문제',
      path: !authInfo?.isSolvedToday ? '/interview' : `/interview/result/${authInfo.interviewAnswerId}`,
    },
    { label: '시험모드', path: '/exam' },
    { label: '게임모드', path: '/game' },
    { label: '대시보드', path: '/dashboard' },
  ];

  const loginItem: HeaderItem = { label: isAuthorized ? '로그아웃' : '로그인', path: isAuthorized ? '/' : '/signin' };

  const handleAuthClick = async () => {
    if (isAuthorized) {
      if (isExamInProgress || isGameInProgress) {
        return;
      }
      await logout();
    }
  };

  return (
    <header
      className={cn(
        'fixed top-0 z-10 flex w-full max-w-[430px] flex-col items-center justify-between gap-5 bg-white/50 px-4 py-3 text-black shadow-sm backdrop-blur-sm sm:hidden',
      )}
    >
      <div className="flex w-full items-center justify-between">
        <div className="text-2xl font-bold">
          <Link to="/">Pull Up!</Link>
        </div>
        <Link
          key={loginItem.path}
          to={loginItem.path}
          className={cn(
            {
              'border-b-[3px] border-primary-500 text-primary-500':
                location.pathname.split('/')[1] === loginItem.path.split('/')[1],
              'border-b-[3px] border-transparent text-stone-800 hover:text-stone-950':
                location.pathname.split('/')[1] !== loginItem.path.split('/')[1] && loginItem.path == '/',
            },
            'text-sm font-semibold transition-colors duration-200',
          )}
          onClick={handleAuthClick}
        >
          {loginItem.label}
        </Link>
      </div>

      <nav className="flex w-full justify-between">
        {headerItems.map((item) => (
          <Link
            key={item.path}
            to={item.path}
            className={cn(
              {
                'border-b-[3px] border-primary-500 text-primary-500': location.pathname === item.path,
                'border-b-[3px] border-transparent text-stone-800 hover:text-stone-950':
                  location.pathname !== item.path,
              },
              'text-lg font-semibold transition-colors duration-200',
            )}
          >
            {item.label}
          </Link>
        ))}
      </nav>
    </header>
  );
};

export default MobileHeader;
