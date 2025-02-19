import { useEffect, useState } from 'react';
import { useBlocker, useNavigate } from 'react-router-dom';

interface UseExamNavigateProps {
  isBlocked: boolean;
  handleProceed: () => void;
  handleCancel: () => void;
  setException: () => void;
}

const UseExamNavigate = (): UseExamNavigateProps => {
  const blocker = useBlocker(({ currentLocation, nextLocation }) => {
    return currentLocation.pathname !== nextLocation.pathname;
  });

  const navigate = useNavigate();
  const [isBlocked, setIsBlocked] = useState(false); // 모달 상태
  const [isRefresh, setIsRefresh] = useState(false); // 새로고침 여부

  // 페이지 이동 차단
  useEffect(() => {
    if (blocker.state === 'blocked') {
      setIsBlocked(true);
    }
  }, [blocker]);

  // 새로고침 감지 (브라우저 경고 없이)
  useEffect(() => {
    const handleBeforeUnload = (event: BeforeUnloadEvent) => {
      // 기본 경고 비활성화 (단, 모달 렌더링은 불가능함)
      event.preventDefault();
      setIsBlocked(true);
      setIsRefresh(true);
      return '';
    };

    window.addEventListener('beforeunload', handleBeforeUnload);

    return () => {
      window.removeEventListener('beforeunload', handleBeforeUnload);
    };
  }, []);

  // 이동 또는 새로고침 허용
  const handleProceed = () => {
    setIsBlocked(false);
    if (isRefresh) {
      navigate('/exam'); // 새로고침 시 /exam 이동
    } else {
      blocker.proceed?.(); // 일반 페이지 이동
    }
  };

  // 이동 또는 새로고침 취소
  const handleCancel = () => {
    setIsBlocked(false);
    setIsRefresh(false);
  };

  // 예외 설정 (이동 허용)
  const setException = () => {
    blocker.proceed?.();
  };

  return { isBlocked, handleProceed, handleCancel, setException };
};

export default UseExamNavigate;
