import { useQuery, useSuspenseQuery } from '@tanstack/react-query';
import api from './instance';
import { Exam, ExamCreateRequest, ExamDetailsResponse, ExamResultRequest, ExamResultResponse } from '@/types/exam';
import { GetCorrectRateResponse, GetExamAllResponse, GetScoreResponse } from '@/types/response/exam';

const getScore = async () => {
  const response = await api.get<GetScoreResponse>('exam/me/score');
  const data = await response.json();
  return data;
};

export const useGetScore = () =>
  useQuery({
    queryKey: ['scores'],
    queryFn: () => getScore(),
  });

const getCorrectRate = async () => {
  const response = await api.get<GetCorrectRateResponse>('exam/me/correct-rate');
  const data = await response.json();
  return data;
};

export const useGetCorrectRate = () =>
  useQuery({
    queryKey: ['correctRates'],
    queryFn: () => getCorrectRate(),
  });

// 최근에 푼 모의고사 전체 조회
const getExamAll = async () => {
  const response = await api.get<GetExamAllResponse>('exam/me/all');
  const responseData = await response.json();
  return responseData;
};

export const useGetExamAll = () =>
  useQuery({
    queryKey: ['examAll'],
    queryFn: () => getExamAll(),
  });

// 최근에 푼 모의고사 단건 조회
const getRecentExam = async () => {
  const response = await api.get<Exam>('exam/me/recent');
  const responseData = await response.json();
  return responseData;
};

export const useGetRecentExam = () =>
  useQuery({
    queryKey: ['recentExam'],
    queryFn: () => getRecentExam(),
  });

// 모의고사 생성
export const postExam = async (data: ExamCreateRequest) => {
  const response = await api.post('exam/me', { json: data });
  const responseData = await response.json<{ examId: string }>();
  return responseData;
};

// 모의고사 조회
const getExamDetails = (examId: string) => {
  return api.get(`exam/${examId}`).json<{ examDetailsDtos: ExamDetailsResponse }>();
};

export const useGetExamDetails = (examId: string) => {
  return useSuspenseQuery({
    queryKey: ['examDetails', examId],
    queryFn: async () => {
      const response = await getExamDetails(examId);
      return response.examDetailsDtos;
    },
    refetchOnMount: false,
    refetchOnWindowFocus: false,
    retry: false,
  });
};

// 모의고사 답안 제출
export const postExamAnswer = async (examId: string, data: ExamResultRequest) => {
  return await api.post(`exam/${examId}`, {
    json: data,
  });
};

// 모의고사 채점 결과 조회
export const getExamResult = (examId: string) => {
  return api.get(`exam/${examId}/result`).json<ExamResultResponse>();
};

export const useGetExamResult = (examId: string) => {
  return useSuspenseQuery({
    queryKey: ['examResult', examId],
    queryFn: () => getExamResult(examId),
  });
};
