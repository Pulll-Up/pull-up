import { ProblemDetail } from '@/types/problem';
import api from './instance';
import { useQuery, useSuspenseQuery } from '@tanstack/react-query';
import {
  GetArchivedProblemAllResponse,
  GetRecentWrongProblem,
  GetWrongProblemAllResponse,
} from '@/types/response/problems';

// 내가 틀린 문제 최근 전체 조회
const getWrongProblemAll = async () => {
  const response = await api.get<GetWrongProblemAllResponse>('problem/me/all');
  const responseData = await response.json();
  return responseData;
};

export const useGetWrongProblemAll = () =>
  useSuspenseQuery({
    queryKey: ['wrongProblems'],
    queryFn: () => getWrongProblemAll(),
  });

// 내가 틀린 문제 최근 10건 조회
const getRecentWrongProblem = async () => {
  const response = await api.get<GetRecentWrongProblem>('problem/wrong/recent');
  const responseData = await response.json();
  return responseData;
};

export const useGetRecentWrongProblem = () => {
  return useQuery({
    queryKey: ['recentWrongProblems'],
    queryFn: () => getRecentWrongProblem(),
  });
};

// 내가 틀린 문제 검색 조회
const getWrongProblemsByTitle = async (searchValue: string) => {
  const response = await api.get<GetWrongProblemAllResponse>(`problem/wrong?title=${searchValue}`);
  const responseData = await response.json();
  return responseData;
};

export const useGetWrongProblemsByTitle = (searchValue: string) => {
  return useSuspenseQuery({
    queryKey: ['getWrongProblemsByTitle', searchValue],
    queryFn: () => getWrongProblemsByTitle(searchValue),
  });
};

// 문제 단건 조회
const getProblemDetail = (problemId: string) => api.get<ProblemDetail>(`problem/${problemId}`).json();

export const useGetProblemDetail = (problemId: string) =>
  useSuspenseQuery({
    queryKey: ['problemDetail', problemId],
    queryFn: () => getProblemDetail(problemId),
  });

// 북마크 문제 전체 조회
const getArchivedProblemAll = () => api.get<GetArchivedProblemAllResponse>('problem/archive/all').json();

export const useGetArchivedProblemAll = () =>
  useQuery({
    queryKey: ['archivedProblems'],
    queryFn: () => getArchivedProblemAll(),
  });

// 문제 북마크
export const toggleProblemBookmark = async (problemId: string) => {
  await api.post(`problem/${problemId}`);
};

// 북마크 문제 검색 조회
const getArchivedProblemsByTitle = async (searchValue: string) => {
  const response = await api.get<GetArchivedProblemAllResponse>(`problem/bookmark?title=${searchValue}`);
  const responseData = await response.json();

  return responseData;
};

export const useGetArchivedProblemsByTitle = (searchValue: string) => {
  return useSuspenseQuery({
    queryKey: ['getArchivedProblemsByTitle', searchValue],
    queryFn: () => getArchivedProblemsByTitle(searchValue),
  });
};
