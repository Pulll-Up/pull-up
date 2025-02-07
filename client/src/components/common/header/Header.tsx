import { logout } from '@/api/auth';
import { cn } from '@/lib/utils';
import { memberStore } from '@/stores/memberStore';
import { Link, useLocation } from 'react-router-dom';

interface HeaderItem {
  label: string;
  path: string;
}

const Header = () => {
  const location = useLocation();
  const { member, isLoggedIn, logoutMember, isSolvedToday, interviewId } = memberStore();

  const headerItems: HeaderItem[] = [
    { label: '시험모드', path: '/exam' },
    { label: '게임모드', path: '/game' },
    { label: '대시보드', path: '/dashboard' },
    { label: isLoggedIn ? '로그아웃' : '로그인', path: isLoggedIn ? '/' : '/signin' },
  ];

  const handleAuthClick = async () => {
    if (isLoggedIn) {
      await logout();
      logoutMember();
    }
  };

  return (
    <header
      className={cn(
        'fixed top-0 z-10 hidden w-full items-center justify-between bg-white/50 p-4 px-10 text-black shadow-sm backdrop-blur-sm sm:flex',
      )}
    >
      <div className="text-3xl font-bold">
        {/* 로그인이면 로고->오늘의 문제, 비로그인이면 로고->메인인 */}
        <Link to={!isLoggedIn || !member ? '/' : !isSolvedToday ? '/interview' : `/interview/result/${interviewId}`}>
          Pull Up!
        </Link>
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
