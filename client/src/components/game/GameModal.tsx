import { useDeleteRoom, usePostCreateGame, usePostCreateRoomRandom, usePostJoinGame } from '@/api/game';
import Modal from '../common/modal';
import CreateRoom from './gameModalComponent/CreateRoom';
import JoinGame from './gameModalComponent/JoinGame';
import Waiting from './gameModalComponent/waiting/Waiting';
import { useEffect, useRef, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import WaitingAfterCreating from './gameModalComponent/waiting/WaitingAfterCreating';
import { toast } from 'react-toastify';
import { useRoomStore } from '@/stores/roomStore';
import { GetRandomTypeResponse } from '@/types/response/game';
import WaitingRamdom from './gameModalComponent/waiting/WaitingRandom';
import { useWebSocketStore } from '@/stores/useWebSocketStore';

const GameModals = () => {
  const navigate = useNavigate();
  const createRoomTimeoutRef = useRef<NodeJS.Timeout | null>(null);

  const { roomId, setRoomId } = useRoomStore();
  const { roomStatus, sendMessage } = useWebSocketStore();

  const postCreateGame = usePostCreateGame();
  const postJoinGame = usePostJoinGame();
  const deleteRoom = useDeleteRoom();

  const postCreateGameRandom = usePostCreateRoomRandom();

  const [isPlayerReady, setIsPlayerReady] = useState(false);

  const [codeForJoinning, setCodeForJoinning] = useState('');
  const [isCreateMode, setIsCreateMode] = useState(false);

  const createRoomTimeout = () => {
    createRoomTimeoutRef.current = setTimeout(() => {
      if (roomStatus !== 'PLAYING') {
        deleteRoom(roomId);

        toast.error('방을 다시 만들어주세요!', {
          position: 'bottom-center',
        });

        setIsCreateMode(false);
        setRoomId('');
        setIsPlayerReady(false);
      }
    }, 1000 * 60);
  };

  const handleCloseModal = (isOpen: boolean) => {
    if (!isOpen) {
      if (roomId && isCreateMode) {
        deleteRoom(roomId);
        setRoomId('');
        setIsCreateMode(false);
      }

      if (createRoomTimeoutRef.current) {
        clearTimeout(createRoomTimeoutRef.current);
      }

      setIsPlayerReady(false);
    }
  };

  const handleCodeChange = (newCode: string) => {
    setCodeForJoinning(newCode);
  };

  const handleCreateRoom = async () => {
    setIsPlayerReady(true);
    setIsCreateMode(true);

    const selects = {
      algorithm: true,
      computerArchitecture: true,
      database: true,
      dataStructure: false,
      network: true,
      operatingSystem: true,
    }; // 더미

    const { roomId } = await postCreateGame(selects);
    setRoomId(roomId);

    createRoomTimeout();
  };

  const handleJoinRoom = async () => {
    setIsPlayerReady(true);

    const { isReady } = await postJoinGame(codeForJoinning);

    if (!isReady) {
      toast.error('코드와 일치하는 방이 없습니다.', {
        position: 'bottom-center',
      });

      setIsPlayerReady(false);
      setCodeForJoinning('');
      return;
    }

    setRoomId(codeForJoinning);
    setCodeForJoinning('');
  };

  const handleRandomRoom = async ({ randomMatchType, roomId: randomRoomId }: GetRandomTypeResponse) => {
    setIsPlayerReady(true);

    if (randomMatchType === 'CREATE') {
      setIsCreateMode(true);
      const { roomId: createdRondomId } = await postCreateGameRandom();
      setRoomId(createdRondomId);

      return;
    }

    if (randomMatchType === 'JOIN') {
      setRoomId(randomRoomId);
      const { isReady } = await postJoinGame(randomRoomId);
      if (!isReady) {
        toast.error('다시 시도해주세요.', {
          position: 'bottom-center',
        });

        setIsPlayerReady(false);
      }
    }
  };

  useEffect(() => {
    if (!roomId) return;

    setTimeout(() => {
      sendMessage(`/app/game/${roomId}/status`, {});
    }, 3000);
  }, [roomId]);

  useEffect(() => {
    if (roomId && roomStatus === 'PLAYING') {
      if (createRoomTimeoutRef.current) {
        clearTimeout(createRoomTimeoutRef.current);
      }

      toast.success('매칭 성공! 3초 뒤 이동합니다.', {
        position: 'bottom-center',
      });

      setTimeout(() => {
        navigate(`/game/${roomId}`);
      }, 3000);
    }
  }, [roomStatus]);

  return (
    <div className="flex gap-4 sm:gap-6">
      <Modal
        triggerName="랜덤 매칭"
        triggerColor="primary"
        onOpenChange={(isOpen: boolean) => handleCloseModal(isOpen)}
        isOutsideClickable={true}
      >
        <WaitingRamdom handleGameState={handleRandomRoom} />
      </Modal>
      <Modal
        triggerName="방 생성"
        triggerColor="primary"
        onOpenChange={(isOpen: boolean) => handleCloseModal(isOpen)}
        isOutsideClickable={false}
      >
        {isPlayerReady ? <WaitingAfterCreating /> : <CreateRoom handleGameState={handleCreateRoom} />}
      </Modal>
      <Modal
        triggerName="코드 입력"
        triggerColor="primary"
        onOpenChange={(isOpen: boolean) => handleCloseModal(isOpen)}
        isOutsideClickable={true}
      >
        {isPlayerReady ? (
          <Waiting text="입장 중..." />
        ) : (
          <JoinGame code={codeForJoinning} onCodeChange={handleCodeChange} handleGameState={handleJoinRoom} />
        )}
      </Modal>
    </div>
  );
};

export default GameModals;
