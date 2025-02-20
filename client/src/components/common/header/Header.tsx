import { logout } from '@/api/auth';
import { cn } from '@/lib/utils';
import { memberStore } from '@/stores/memberStore';
import { AuthStore } from '@/utils/authService';
import { Link, useLocation } from 'react-router-dom';

interface HeaderItem {
  label: string;
  path: string;
}

const Header = () => {
  const location = useLocation();
  const isExamInProgress = /^\/exam\/[^/]+$/.test(location.pathname);
  const isGameInProgress = /^\/game\/(?!$).+$/.test(location.pathname);

  const { isLoggedIn, logoutMember, isSolvedToday, interviewAnswerId } = memberStore();

  const headerItems: HeaderItem[] = [
    { label: '오늘의 문제', path: !isSolvedToday ? '/interview' : `/interview/result/${interviewAnswerId}` },
    { label: '시험모드', path: '/exam' },
    { label: '게임모드', path: '/game' },
    { label: '대시보드', path: '/dashboard' },
    { label: isLoggedIn ? '로그아웃' : '로그인', path: isLoggedIn ? '/' : '/signin' },
  ];

  const handleAuthClick = async () => {
    console.log('로그아웃 클릭');
    if (isLoggedIn) {
      if (isExamInProgress || isGameInProgress) {
        console.log('시험 / 게임 페이지 로그아웃 차단.');
        return;
      }
      console.log('로그아웃 시도');
      await logout();
      AuthStore.clearAccessToken();
      logoutMember();
      window.location.reload();
    }
  };

  return (
    <header
      className={cn(
        'fixed top-0 z-10 hidden w-full items-center justify-between bg-white/50 p-4 px-10 text-black shadow-sm backdrop-blur-sm sm:flex',
      )}
    >
      <div className="text-3xl font-bold">
        <Link to="/">Pull Up!</Link>
      </div>

      <nav className="flex space-x-6">
        {headerItems.map((item) => (
          <Link
            key={item.path}
            to={item.path}
            className={`text-xl font-semibold transition-colors duration-200 ${
              location.pathname.split('/')[1] === item.path.split('/')[1] && item.label !== '로그아웃'
                ? 'border-b-[3px] border-primary-500 text-primary-500'
                : 'border-b-[3px] border-transparent text-stone-800 hover:text-stone-950'
            }`}
            onClick={item.label === '로그아웃' || item.label === '로그인' ? handleAuthClick : undefined}
          >
            {item.label}
          </Link>
        ))}
      </nav>
    </header>
  );
};

export default Header;
