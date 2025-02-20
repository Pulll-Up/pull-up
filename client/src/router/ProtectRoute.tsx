import { useNavigationStore } from '@/stores/navigation';
import { useEffect } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import { toast } from 'react-toastify';

interface ProtectedRouteProps {
  blockedPath: string;
  redirectPath: string;
  blockedText?: string;
  children: React.ReactNode;
}

export const ProtectedRoute = ({ blockedPath, redirectPath, blockedText, children }: ProtectedRouteProps) => {
  const location = useLocation();
  const navigate = useNavigate();
  const { allowedNavigation } = useNavigationStore();

  useEffect(() => {
    if (location.pathname.startsWith(blockedPath) && !allowedNavigation) {
      navigate(redirectPath, { replace: true });
      toast(blockedText ? blockedText : '이 페이지는 직접 접근으로부터 보호되어 있습니다.', {
        toastId: 'blocked-path',
      });
    }
  }, [location.pathname]);

  return children;
};
