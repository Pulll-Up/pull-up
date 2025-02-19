import { useGetPlayerType } from '@/api/game';
import NavigationDialog from '@/components/common/navigationDialog';
import ProgressBar from '@/components/common/progressBar';
import GameBoard from '@/components/game/gameStage/GameBoard';
import GameScoreBoard from '@/components/game/gameStage/GameScoreBoard';
import usePrompt from '@/hooks/useNavigationBlocker';
import { useSafeNavigate } from '@/hooks/useSafeNavigate';
import useSendTimerManager from '@/hooks/useSendTimerManager';
import { useRoomStore } from '@/stores/roomStore';
import { useWebSocketStore } from '@/stores/useWebSocketStore';
import { useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { toast } from 'react-toastify';

const GameStage = () => {
  const navigate = useNavigate();
  const { safeNavigate } = useSafeNavigate();
  const { roomId } = useRoomStore();
  const { updateSubscription, roomInfo, connectWebSocket, disconnectWebSocket } = useWebSocketStore();
  const { isBlocked, handleProceed, handleCancel, setException } = usePrompt();
  const { startSendTimer, clearSendTimer } = useSendTimerManager();

  const { data: playerTypeData, isPending, isError: getPlayerTypeError } = useGetPlayerType(roomId);

  const handleGameProceed = () => {
    disconnectWebSocket();
    connectWebSocket();
    navigate('/game');
    handleProceed();
  };

  useEffect(() => {
    if (isPending || getPlayerTypeError) return;

    if (roomId) {
      updateSubscription(roomId, 'game');

      startSendTimer({
        key: 'init',
        durationMs: 800,
        destination: '/app/card/check',
        payload: {
          checkType: 'INIT',
          roomId,
          playerType: playerTypeData.playerType,
        },
      });

      startSendTimer({
        key: 'timeover',
        durationMs: 60000,
        destination: '/app/card/check',
        payload: {
          checkType: 'TIME_OVER',
          roomId,
        },
      });
    }
  }, [roomId, playerTypeData, updateSubscription]);

  useEffect(() => {
    if (roomInfo.gameRoomStatus === 'FINISHED') {
      clearSendTimer('timeover');
      setException();
      safeNavigate('/game/result');
    }
  }, [roomInfo]);

  useEffect(() => {
    if (getPlayerTypeError) {
      setException();
      navigate('/game');

      toast('게임이 종료되어 대기 화면으로 이동합니다.');
    }
  }, [getPlayerTypeError, navigate]);

  if (!playerTypeData || isPending)
    return (
      <div className="flex h-full w-full flex-col items-center justify-center bg-Main">잠시만 기다려주세요...</div>
    );
  return (
    <div className="flex h-full w-full flex-col gap-3 bg-Main p-4 sm:pt-[85px] md:p-8 md:pt-[84px]">
      <div className="flex items-center gap-3 md:gap-6">
        <ProgressBar initialTime={60} />
      </div>
      <div className="grid h-full w-full grid-rows-[3fr_auto] gap-3 md:grid-cols-[2.5fr_1fr] md:grid-rows-1 md:gap-7">
        <GameBoard playerType={playerTypeData.playerType} problems={roomInfo.problemCardWithoutCardIds} />
        <div className="flex flex-col">
          <div className="grid grow grid-cols-2 gap-3 md:grid-cols-1 md:grid-rows-2 md:gap-7">
            {playerTypeData.playerType === 'player1P' ? (
              <>
                <GameScoreBoard player={roomInfo.player2P} />
                <GameScoreBoard player={roomInfo.player1P} />
              </>
            ) : (
              <>
                <GameScoreBoard player={roomInfo.player1P} />
                <GameScoreBoard player={roomInfo.player2P} />
              </>
            )}
          </div>
        </div>
      </div>
      <NavigationDialog
        isOpen={isBlocked}
        onProceed={handleGameProceed}
        onCancel={handleCancel}
        title="게임을 중단하시겠습니까?"
        description="페이지를 이동할 경우 게임은 기권 처리 됩니다."
      />
    </div>
  );
};
export default GameStage;
