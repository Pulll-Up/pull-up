import { queryClient } from '@/main';
import { useMutation, useQuery } from '@tanstack/react-query';
import api from './instance';
import { CreateResponse } from '@/types/common';
import { now } from 'lodash';
import { memberStore } from '@/stores/memberStore';
import { CommentCreateRequest, CommentUpdateRequest } from '@/types/request/comment';
import { InterviewAnswer } from '@/types/interview';
import { Comment } from '@/types/comment';
import { toast } from 'react-toastify';

// 댓글 전체 조회
const getComments = async (interviewAnswerId: string) => {
  const response = await api.get(`interview/${interviewAnswerId}/comment/all`).json<{ comments: Comment[] }>();
  return response.comments;
};

export const useGetComments = (interviewAnswerId: string) => {
  return useQuery({
    queryKey: ['comments', interviewAnswerId],
    queryFn: () => getComments(interviewAnswerId),
  });
};

// 댓글 작성
const createComment = async (comment: CommentCreateRequest): Promise<CreateResponse> => {
  const data = await api
    .post(`interview/${comment.interviewAnswerId}/comment`, { json: { content: comment.content } })
    .json<CreateResponse>();
  return data;
};

export const useCreateComment = (interviewAnswerId: string) => {
  const { mutate } = useMutation({
    mutationFn: (comment: CommentCreateRequest) => {
      return createComment(comment);
    },

    onMutate: async (comment: CommentCreateRequest) => {
      await queryClient.cancelQueries({ queryKey: ['interviewAnswerDetail', interviewAnswerId] });
      await queryClient.cancelQueries({ queryKey: ['commnets', interviewAnswerId] });

      const previousAnswers = queryClient.getQueryData(['interviewAnswerDetail', interviewAnswerId]);
      const previousComments = queryClient.getQueryData(['comments', interviewAnswerId]);

      queryClient.setQueryData(['interviewAnswerDetail', interviewAnswerId], (old: InterviewAnswer) => ({
        ...old,
        commentCount: old.commentCount + 1,
      }));
      queryClient.setQueryData(['comments', interviewAnswerId], (old: Comment[]) => [
        ...old,
        {
          commentId: 0,
          writer: '',
          email: memberStore.getState().member?.email,
          content: comment.content,
          createdAt: now(),
        },
      ]);

      return { previousAnswers, previousComments };
    },
    onError: (err, _, context) => {
      toast.error('댓글이 작성되지 않았습니다.', { position: 'bottom-center', toastId: 'comment-create' });
      if (context?.previousAnswers || context?.previousComments) {
        queryClient.setQueryData(['interviewAnswerDetail', interviewAnswerId], context.previousAnswers);
        queryClient.setQueryData(['comments', interviewAnswerId], context.previousComments);
      }
    },
    onSettled: () => {
      queryClient.invalidateQueries({
        queryKey: ['interviewAnswerDetail', interviewAnswerId],
      });
      queryClient.invalidateQueries({
        queryKey: ['comments', interviewAnswerId],
      });
    },
  });

  return mutate;
};

// 댓글 수정
const updateComment = async (comment: CommentUpdateRequest): Promise<void> => {
  return await api
    .patch(`interview/interview-answer/comment/${comment.commentId}`, { json: { content: comment.content } })
    .json();
};

export const useUpdateComment = (interviewAnswerId: string) => {
  const { mutate } = useMutation({
    mutationFn: (comment: CommentUpdateRequest) => updateComment(comment),
    onMutate: async (comment: CommentUpdateRequest) => {
      await queryClient.cancelQueries({ queryKey: ['comments', interviewAnswerId] });

      const previousData = queryClient.getQueryData(['comments', interviewAnswerId]);

      queryClient.setQueryData(['comments', interviewAnswerId], (old: Comment[]) =>
        old.map((c: Comment) => (c.commentId === comment.commentId ? { ...c, content: comment.content } : c)),
      );

      return { previousData };
    },
    onError: (err, _, context) => {
      toast.error('댓글이 수정되지 않았습니다.', { position: 'bottom-center', toastId: 'comment-update' });
      if (context?.previousData) {
        queryClient.setQueryData(['comments', interviewAnswerId], context.previousData);
      }
    },
    onSettled: () => {
      queryClient.invalidateQueries({
        queryKey: ['comments', interviewAnswerId],
      });
    },
  });

  return mutate;
};

// 댓글 삭제
const deleteComment = async (commentId: number) => {
  return await api.delete(`interview/interview-answer/comment/${commentId}`);
};

export const useDeleteComment = (interviewAnswerId: string) => {
  const { mutate } = useMutation({
    mutationFn: (commentId: number) => deleteComment(commentId),
    onMutate: async (commentId: number) => {
      await queryClient.cancelQueries({ queryKey: ['interviewAnswerDetail', interviewAnswerId] });
      await queryClient.cancelQueries({ queryKey: ['comments', interviewAnswerId] });

      const previousAnswer = queryClient.getQueryData(['interviewAnswerDetail', interviewAnswerId]);
      const previousComment = queryClient.getQueryData(['comments', interviewAnswerId]);

      queryClient.setQueryData(['interviewAnswerDetail', interviewAnswerId], (old: InterviewAnswer) => ({
        ...old,
        commentCount: old.commentCount - 1,
      }));
      queryClient.setQueryData(['comments', interviewAnswerId], (old: Comment[]) =>
        old.filter((c: Comment) => c.commentId !== commentId),
      );

      return { previousAnswer, previousComment };
    },
    onError: (err, _, context) => {
      toast.error('댓글이 삭제되지 않았습니다.', { position: 'bottom-center', toastId: 'comment-delete' });
      if (context?.previousAnswer || context?.previousComment) {
        queryClient.setQueryData(['interviewAnswerDetail', interviewAnswerId], context.previousAnswer);
        queryClient.setQueryData(['comments', interviewAnswerId], context.previousComment);
      }
    },
    onSettled: () => {
      queryClient.invalidateQueries({
        queryKey: ['interviewAnswerDetail', interviewAnswerId],
      });
      queryClient.invalidateQueries({
        queryKey: ['comments', interviewAnswerId],
      });
    },
  });

  return mutate;
};
