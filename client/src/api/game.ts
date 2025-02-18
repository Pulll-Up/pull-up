import { useMutation, useQuery } from '@tanstack/react-query';
import api from './instance';
import { WinningRate } from '@/types/chart';
import { SubjectSelect } from '@/types/game';
import {
  GetGameResultResponse,
  GetPlayerTypeResponse,
  GetRandomTypeResponse,
  PostCreateGameResponse,
  PostJoinGameResponse,
} from '@/types/response/game';
import { useRoomStore } from '@/stores/roomStore';
import { toast } from 'react-toastify';

const getWinningRate = async () => {
  const response = await api.get<WinningRate>('game/me/winning-rate');
  const data = await response.json();

  return data;
};

export const useGetWinningRate = () =>
  useQuery({
    queryKey: ['winningRate'],
    queryFn: () => getWinningRate(),
  });

const postCreateGame = async (selects: SubjectSelect) => {
  const response = await api.post<PostCreateGameResponse>('game/room', { json: selects });
  const data = await response.json();
  return data;
};

export const usePostCreateGame = () => {
  const { mutateAsync } = useMutation({
    mutationFn: (selects: SubjectSelect) => postCreateGame(selects),
    onSuccess: () => {},
  });

  return mutateAsync;
};

const postJoinGame = async (roomId: string) => {
  const response = await api.post<PostJoinGameResponse>('game/room/join', { json: { roomId } });
  const data = await response.json();
  return data;
};

export const usePostJoinGame = () => {
  const { mutateAsync } = useMutation({
    mutationFn: (roomId: string) => postJoinGame(roomId),
    onError: (error) => {
      toast.error(error.message || '다시 시도해주세요!', {
        position: 'bottom-center',
      });
      throw new Error();
    },
  });

  return mutateAsync;
};

const deleteRoom = (roomId: string) => api.delete(`game/room/${roomId}`);

export const useDeleteRoom = () => {
  const { setRoomId } = useRoomStore();

  const { mutate } = useMutation({
    mutationFn: (roomId: string) => deleteRoom(roomId),
    onSuccess: () => {
      setRoomId('');
    },
  });

  return mutate;
};

const getPlayerType = async (roomId: string) => {
  const response = await api.get<GetPlayerTypeResponse>(`game/room/${roomId}/player`);
  const data = await response.json();
  return data;
};

export const useGetPlayerType = (roomId: string) =>
  useQuery({
    queryKey: ['myPlayerType'],
    queryFn: () => getPlayerType(roomId),
  });

const getRandomType = async () => {
  const response = await api.get<GetRandomTypeResponse>('game/random/type');
  const data = await response.json();

  return data;
};

export const useGetRandomType = () =>
  useQuery({
    queryKey: ['randomType'],
    queryFn: () => getRandomType(),
    staleTime: 0,
    gcTime: 0,
  });

const postCreateRoomRandom = async () => {
  const response = await api.post<PostCreateGameResponse>('game/room/random');
  const data = await response.json();

  return data;
};

export const usePostCreateRoomRandom = () => {
  const { mutateAsync } = useMutation({
    mutationFn: () => postCreateRoomRandom(),
  });
  return mutateAsync;
};

const getGameResult = async (roomId: string) => {
  const response = await api.get<GetGameResultResponse>(`game/room/${roomId}/result`);
  const data = await response.json();

  return data;
};

export const useGetGameResult = () => {
  const { roomId } = useRoomStore();

  return useQuery({
    queryKey: ['gameResult'],
    queryFn: () => getGameResult(roomId),
    staleTime: 0,
    gcTime: 0,
  });
};
