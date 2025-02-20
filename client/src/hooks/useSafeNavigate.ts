import { useNavigate } from 'react-router-dom';
import { useNavigationStore } from '@/stores/navigation';

export const useSafeNavigate = () => {
  const navigate = useNavigate();
  const { setAllowedNavigation } = useNavigationStore();

  const safeNavigate = (path: string) => {
    setAllowedNavigation(true);
    navigate(path);
  };

  return { safeNavigate };
};
