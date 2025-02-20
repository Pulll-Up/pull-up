import { useMutation, useQuery } from '@tanstack/react-query';
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
import api from './instance';
import { ApiError } from './error';

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
  const data = await api
    .post('game/room/join', {
      json: { roomId },
    })
    .json<PostJoinGameResponse>();

  return data;
};

export const usePostJoinGame = () => {
  const { mutateAsync } = useMutation({
    mutationFn: (roomId: string) => postJoinGame(roomId),
    onError: (error: ApiError) => {
      if (error.response.status === 400 || error.response.status === 404)
        toast.error(error.message, {
          position: 'bottom-center',
          toastId: 'code-error',
        });
      else
        toast.error('알 수 없는 오류가 발생했습니다.', {
          position: 'bottom-center',
          toastId: 'code-error',
        });
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
