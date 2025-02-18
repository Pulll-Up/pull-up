import { Button } from '@/components/ui/button';
import { useCallback } from 'react';
import { toast } from 'react-toastify';
import { throttle } from 'lodash';
import { useRoomStore } from '@/stores/roomStore';

const WaitingAfterCreating = () => {
  const { roomId } = useRoomStore();

  const handleClipboard = useCallback(
    throttle(
      async () => {
        await navigator.clipboard.writeText(roomId);
        toast.success('코드가 복사되었습니다!', { position: 'bottom-center', toastId: 'clipboard' });
      },
      3000,
      { leading: true, trailing: false },
    ),
    [roomId],
  );

  return (
    <div className="flex w-full flex-col items-center gap-4">
      <span className="font-bold">코드를 복사해 친구에게 공유해주세요!</span>

      <div className="flex items-center justify-center gap-3">
        <div className="rounded-xl bg-primary-50 px-4 py-2">{roomId}</div>
        <Button onClick={handleClipboard}>복사</Button>
      </div>
      <div className="flex flex-col items-center">
        <span className="space-y-0 text-xs font-semibold text-primary-500">창을 닫으면 매칭이 취소됩니다. </span>
        <span className="text-xs font-semibold text-primary-500">매칭이 되기 전까지 창을 닫지 마세요!</span>
      </div>
    </div>
  );
};

export default WaitingAfterCreating;
