import { useDeleteRoom, usePostCreateGame, usePostCreateRoomRandom, usePostJoinGame } from '@/api/game';
import Modal from '../common/modal';
import CreateRoom from './gameModalComponent/CreateRoom';
import JoinGame from './gameModalComponent/JoinGame';
import Waiting from './gameModalComponent/waiting/Waiting';
import { useEffect, useRef, useState } from 'react';
import WaitingAfterCreating from './gameModalComponent/waiting/WaitingAfterCreating';
import { toast } from 'react-toastify';
import { useRoomStore } from '@/stores/roomStore';
import { GetRandomTypeResponse } from '@/types/response/game';
import { useWebSocketStore } from '@/stores/useWebSocketStore';
import { FormFormEvent } from '@/types/event';
import { SubjectSelect } from '@/types/game';
import { useSafeNavigate } from '@/hooks/useSafeNavigate';

const INITIAL_SELECT = {
  algorithm: false,
  computerArchitecture: false,
  database: false,
  dataStructure: false,
  network: false,
  operatingSystem: false,
};

const GameModals = () => {
  const { safeNavigate } = useSafeNavigate();
  const createRoomTimeoutRef = useRef<NodeJS.Timeout | null>(null);

  const { roomId, setRoomId } = useRoomStore();
  const { roomStatus, sendMessage, connectWebSocket } = useWebSocketStore();

  const postCreateGame = usePostCreateGame();
  const postJoinGame = usePostJoinGame();
  const deleteRoom = useDeleteRoom();
  const postCreateGameRandom = usePostCreateRoomRandom();

  const [isPlayerReady, setIsPlayerReady] = useState(false);
  const [isCreateMode, setIsCreateMode] = useState(false);

  const [codeForJoinning, setCodeForJoinning] = useState('');
  const [selectedSubjects, setSelectedSubjects] = useState<SubjectSelect>(INITIAL_SELECT);

  const createRoomTimeout = (roomId: string) => {
    createRoomTimeoutRef.current = setTimeout(() => {
      if (roomStatus !== 'PLAYING') {
        deleteRoom(roomId);

        setIsCreateMode(false);
        setIsPlayerReady(false);

        toast.error('방을 다시 만들어주세요!', {
          position: 'bottom-center',
          toastId: 'retry',
        });
      }
    }, 1000 * 60);
  };

  const handleCloseModal = (isOpen: boolean) => {
    if (!isOpen) {
      if (roomId && isCreateMode) {
        deleteRoom(roomId);
        setIsCreateMode(false);
      }

      if (createRoomTimeoutRef.current) {
        clearTimeout(createRoomTimeoutRef.current);
      }

      setSelectedSubjects(INITIAL_SELECT);
      setCodeForJoinning('');
      setIsPlayerReady(false);
    }
  };

  const handleCodeChange = (newCode: string) => {
    setCodeForJoinning(newCode);
  };

  const handleCreateRoom = async (e: FormFormEvent) => {
    e.preventDefault();

    setIsPlayerReady(true);
    setIsCreateMode(true);

    const { roomId } = await postCreateGame(selectedSubjects);
    setRoomId(roomId);

    createRoomTimeout(roomId);
  };

  const handleJoinRoom = async (event: FormFormEvent) => {
    event.preventDefault();

    // 웹소켓 연결 체크
    connectWebSocket();

    setIsPlayerReady(true);

    try {
      const { isReady } = await postJoinGame(codeForJoinning);
      if (isReady) setRoomId(codeForJoinning);
    } catch {
      setIsPlayerReady(false);
    } finally {
      setCodeForJoinning('');
    }
  };

  const handleRandomRoom = async ({ randomMatchType, roomId: randomRoomId }: GetRandomTypeResponse) => {
    setIsPlayerReady(true);

    // 웹소켓 연결 체크
    connectWebSocket();

    if (randomMatchType === 'CREATE') {
      setIsCreateMode(true);
      const { roomId: createdRandomId } = await postCreateGameRandom();
      setRoomId(createdRandomId);

      return;
    }

    if (randomMatchType === 'JOIN') {
      try {
        const { isReady } = await postJoinGame(randomRoomId);
        if (isReady) setRoomId(randomRoomId);
      } catch {
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
        autoClose: 2000,
        position: 'bottom-center',
        toastId: 'match-success',
      });

      setTimeout(() => {
        safeNavigate(`/game/${roomId}`);
      }, 3000);
    }
  }, [roomStatus]);

  return (
    <div className="flex gap-4 sm:gap-6">
      {/* <Modal
        triggerName="랜덤 매칭"
        triggerColor="primary"
        onOpenChange={(isOpen: boolean) => handleCloseModal(isOpen)}
        isOutsideClickable={true}
      >
        <WaitingRamdom handleGameState={handleRandomRoom} />
      </Modal> */}
      <Modal
        triggerName="방 생성"
        triggerColor="primary"
        onOpenChange={(isOpen: boolean) => handleCloseModal(isOpen)}
        isOutsideClickable={false}
      >
        {isPlayerReady ? (
          <WaitingAfterCreating />
        ) : (
          <CreateRoom
            handleGameState={handleCreateRoom}
            selectedSubjects={selectedSubjects}
            setSelectedSubjects={setSelectedSubjects}
          />
        )}
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
