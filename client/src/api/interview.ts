import { useMutation, useQuery } from '@tanstack/react-query';
import api from './instance';
import { InterviewAnswer } from '@/types/interview';
import { LikeResponse } from '@/types/common';
import { queryClient } from '@/main';
import {
  AnswerResponse,
  GetStreakResponse,
  InterviewListResponse,
  InterviewResponse,
  InterviewResultResponse,
} from '@/types/response/interview';
import { toast } from 'react-toastify';
import { AnswerRequest } from '@/types/request/interview';

const getStreak = async () => {
  const response = await api.get<GetStreakResponse>('member/me/streak');
  const data = await response.json();

  return data;
};

export const useGetStreak = () =>
  useQuery({
    queryKey: ['streak'],
    queryFn: () => getStreak(),
  });

// 오늘의 문제 조회
const getInterview = (): Promise<InterviewResponse> => {
  const data = api.get('interview').json<InterviewResponse>();
  return data;
};

export const useGetInterview = () => {
  return useQuery({
    queryKey: ['interview'],
    queryFn: getInterview,
    staleTime: 1000 * 60 * 10, // 10분
    gcTime: 1000 * 60 * 60, // 1시간
  });
};

// 답안 제출
export const createAnswer = async (request: AnswerRequest) => {
  const response = await api
    .post(`interview/${request.interviewId}/submit`, { json: { answer: request.answer } })
    .json<AnswerResponse>();

  return response;
};

export const useCreateAnswer = (request: AnswerRequest) => {
  const { mutate } = useMutation({
    mutationFn: () => createAnswer(request),
    onError: (error) => {
      toast.error('답변 작성을 실패했습니다.', { position: 'bottom-center', toastId: 'answer-create' });
      throw error;
    },
  });

  return mutate;
};

// 결과 조회
const getInterviewResult = (interviewAnswerId: number): Promise<InterviewResultResponse> => {
  const data = api.get(`interview/${interviewAnswerId}/result`).json<InterviewResultResponse>();
  return data;
};

export const useGetInterviewResult = (interviewAnswerId: number) => {
  return useQuery({
    queryKey: ['result', interviewAnswerId],
    queryFn: () => getInterviewResult(interviewAnswerId),
    staleTime: 1000 * 60 * 10, // 10분
    gcTime: 1000 * 60 * 60, // 1시간
  });
};

// 지난 오늘의 문제 전체 조회
const getInterviewList = async (): Promise<InterviewListResponse[]> => {
  const response = await api.get('interview/me/all').json<{ myInterviewAnswers: InterviewListResponse[] }>();
  return response.myInterviewAnswers;
};

export const useGetInterviewList = () => {
  return useQuery({
    queryKey: ['interviewList'],
    queryFn: getInterviewList,
  });
};

// 지난 오늘의 문제 검색 조회
export const getInterviewListByKeyword = async (keyword: string): Promise<InterviewListResponse[]> => {
  const response = await api
    .get(`interview/search?keyword=${keyword}`)
    .json<{ searchedInterviewQuestions: InterviewListResponse[] }>();

  return response.searchedInterviewQuestions;
};

// 다른 사람 답변 전체 조회
const getInterviewAnswers = async (interviewId: number): Promise<InterviewAnswer[]> => {
  const response = await api.get(`interview/${interviewId}/all`).json<{ interviewAnswers: InterviewAnswer[] }>();
  return response.interviewAnswers;
};

export const useGetInterviewAnswers = (interviewId: number) => {
  return useQuery({
    queryKey: ['interviewAnswers', interviewId],
    queryFn: () => getInterviewAnswers(interviewId),
  });
};

// 다른 사람 답변 단건 조회
const getInterviewAnswerDetail = async (interviewAnswerId: number) => {
  const response = await api.get(`interview/${interviewAnswerId}`).json<{ interviewAnswer: InterviewAnswer }>();
  return response.interviewAnswer;
};

export const useGetInterviewAnswerDetail = (interviewAnswerId: number) => {
  return useQuery({
    queryKey: ['interviewAnswerDetail', interviewAnswerId],
    queryFn: () => getInterviewAnswerDetail(interviewAnswerId),
  });
};

// 다른 사람 답변 좋아요
const createInterviewAnswerLike = async (interviewAnswerId: number): Promise<LikeResponse> => {
  const data = await api.post(`interview/${interviewAnswerId}/like`).json<LikeResponse>();
  return data;
};

export const useCreateInterviewAnswerLike = (interviewId: number) => {
  const { mutate } = useMutation({
    mutationFn: (interviewAnswerId: number) => createInterviewAnswerLike(interviewAnswerId),
    onMutate: async (interviewAnswerId) => {
      await Promise.all([
        queryClient.cancelQueries({ queryKey: ['interviewAnswers', interviewId] }),
        queryClient.cancelQueries({ queryKey: ['interviewAnswerDetail', interviewAnswerId] }),
      ]);

      const previousListData = queryClient.getQueryData(['interviewAnswers', interviewId]);
      const previousDetailData = queryClient.getQueryData(['interviewAnswerDetail', interviewAnswerId]);

      if (previousListData) {
        queryClient.setQueryData(['interviewAnswers', interviewId], (old: InterviewAnswer[]) =>
          old?.map((answer) =>
            answer.interviewAnswerId === interviewAnswerId
              ? {
                  ...answer,
                  isLiked: !answer.isLiked,
                  likeCount: answer.isLiked ? answer.likeCount - 1 : answer.likeCount + 1,
                }
              : answer,
          ),
        );
      }

      if (previousDetailData) {
        queryClient.setQueryData(['interviewAnswerDetail', interviewAnswerId], (old: InterviewAnswer) => ({
          ...old,
          isLiked: !old.isLiked,
          likeCount: old.isLiked ? old.likeCount - 1 : old.likeCount + 1,
        }));
      }

      return { previousListData, previousDetailData };
    },
    onError: (err, interviewAnswerId, context) => {
      toast.error('좋아요를 실패했습니다. 다시 시도해주세요!', {
        position: 'bottom-center',
        toastId: 'like-request',
      });
      if (context?.previousListData) {
        queryClient.setQueryData(['interviewAnswers', interviewId], context.previousListData);
      }
      if (context?.previousDetailData) {
        queryClient.setQueryData(['interviewAnswerDetail', interviewAnswerId], context.previousDetailData);
      }
    },
    onSettled: (_, __, interviewAnswerId) => {
      queryClient.invalidateQueries({ queryKey: ['interviewAnswers', interviewId] });
      queryClient.invalidateQueries({ queryKey: ['interviewAnswerDetail', interviewAnswerId] });
    },
  });

  return mutate;
};
