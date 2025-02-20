import { Button } from '@/components/ui/button';
import { useEffect, useState } from 'react';
import { cn } from '@/lib/utils';
import confetti from 'canvas-confetti';
import { useNavigate } from 'react-router-dom';
import { useRoomStore } from '@/stores/roomStore';
import { OPPONENT } from '@/constants/game';
import { useWebSocketStore } from '@/stores/useWebSocketStore';
import usePrompt from '@/hooks/useNavigationBlocker';
import NavigationDialog from '@/components/common/navigationDialog';
import { useGetPlayerType } from '@/api/game';
import { toast } from 'react-toastify';

const GameResultPage = () => {
  const navigate = useNavigate();

  const { isBlocked, handleProceed, handleCancel, setException } = usePrompt();

  const { setRoomId, roomId } = useRoomStore();
  const { updateSubscription, gameResult, sendMessage, connectWebSocket, disconnectWebSocket } = useWebSocketStore();

  const { data: playerTypeData, isPending, isError } = useGetPlayerType(roomId);

  const [animatedScore, setAnimatedScore] = useState(0);
  const [animatedOpponentScore, setAnimatedOpponentScore] = useState(0);
  const [isVisibleResult, setIsVisibleResult] = useState(false);
  const [isVisibleWinner, setIsVisibleWinner] = useState(false);

  const handleGoBack = () => {
    setException();

    setRoomId('');
    disconnectWebSocket();
    connectWebSocket();
    navigate('/game');
  };

  const handleGameProceed = () => {
    handleGoBack();
    handleProceed();
  };

  useEffect(() => {
    if (isError) {
      handleGoBack();

      toast('게임이 종료되어 대기 화면으로 이동합니다.');
    }
  }, [isError]);

  useEffect(() => {
    if (roomId) {
      updateSubscription(roomId, 'result');
      setTimeout(() => {
        sendMessage(`/app/game/${roomId}/result`, {});
      }, 3000);
    }
  }, [roomId]);

  useEffect(() => {
    if (isPending || isError) return;

    const animateValue = (start: number, end: number, interval: number, setter: (value: number) => void) => {
      let currentValue = start;
      setter(currentValue);

      const step = () => {
        if (currentValue < end) {
          currentValue += 1;
          setter(currentValue);
          setTimeout(step, interval);
        } else {
          setIsVisibleWinner(true);
        }
      };

      setTimeout(step, interval);
    };

    if (gameResult.player1P.name) {
      setIsVisibleResult(true);

      animateValue(0, gameResult[playerTypeData.playerType].score, 200, setAnimatedScore);
      animateValue(0, gameResult[OPPONENT[playerTypeData.playerType]].score, 200, setAnimatedOpponentScore);
    }
  }, [gameResult]);

  useEffect(() => {
    if (isPending || isError || !gameResult.player1P.name) return;

    if (isVisibleWinner) {
      if (gameResult[playerTypeData.playerType].status === 'WIN') {
        confetti({
          particleCount: 100,
          spread: 100,
          origin: { y: 0.6 },
        });
      }
    }
  }, [isVisibleWinner, gameResult]);

  if (isPending || isError)
    return (
      <div className="flex h-full w-full flex-col items-center justify-center bg-Main">잠시만 기다려주세요...</div>
    );

  return (
    <div className="flex h-full w-full flex-col items-center justify-center bg-Main p-8">
      {!isVisibleResult ? (
        <>누가 이겼을까요?</>
      ) : (
        <div className="flex flex-col items-center gap-4">
          <h3
            className={cn(
              'transform text-8xl font-bold text-primary-500 transition-opacity duration-700 ease-out',
              isVisibleWinner ? 'translate-y-0 opacity-100' : 'translate-y-5 opacity-0',
            )}
          >
            {gameResult[playerTypeData.playerType].status}
          </h3>
          {!gameResult.isForfeit && (
            <div className="grid grid-cols-[1fr_auto_1fr] grid-rows-[auto_auto] items-center justify-center rounded-xl bg-white p-4">
              <div className="flex max-w-[120px] flex-col items-center justify-center md:max-w-[180px]">
                <span className="text-md line-clamp-1 overflow-hidden text-ellipsis font-semibold text-stone-800 md:text-lg">
                  {gameResult[playerTypeData.playerType].name}
                </span>
              </div>
              <span className="px-6 text-2xl font-bold text-primary-200">vs</span>
              <div className="flex max-w-[120px] flex-col items-center justify-center md:max-w-[180px]">
                <span className="text-md line-clamp-1 overflow-hidden text-ellipsis font-semibold text-stone-800 md:text-lg">
                  {gameResult[OPPONENT[playerTypeData.playerType]].name}
                </span>
              </div>
              <div className="flex flex-col items-center justify-center p-4">
                <span className="text-5xl font-extrabold text-primary-600">{animatedScore}</span>
              </div>
              <div></div>
              <div className="flex flex-col items-center justify-center p-4">
                <span className="text-5xl font-extrabold text-primary-600">{animatedOpponentScore}</span>
              </div>
            </div>
          )}
          {gameResult.isForfeit && (
            <span>
              {gameResult[playerTypeData.playerType].status === 'WIN'
                ? gameResult[OPPONENT[playerTypeData.playerType]].name
                : gameResult[playerTypeData.playerType].name}
              님의 기권으로 게임이 종료되었습니다.
            </span>
          )}
          <Button className="mt-4 w-[120px]" onClick={handleGoBack}>
            돌아가기
          </Button>
        </div>
      )}
      <NavigationDialog
        isOpen={isBlocked}
        onProceed={handleGameProceed}
        onCancel={handleCancel}
        title="게임 결과 페이지를 나가시겠습니까?"
        description=""
      />
    </div>
  );
};

export default GameResultPage;
