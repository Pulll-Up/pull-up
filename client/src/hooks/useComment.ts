import { useCreateComment, useDeleteComment, useUpdateComment } from '@/api/comment';
import { useState } from 'react';
import { toast } from 'react-toastify';

interface useCommentProps {
  interviewAnswerId: number;
}

export const useComment = ({ interviewAnswerId }: useCommentProps) => {
  // 댓글 작성
  const [inputValue, setInputValue] = useState('');
  const createComment = useCreateComment(interviewAnswerId);

  const onChange = (e: React.ChangeEvent<HTMLTextAreaElement>) => {
    setInputValue(e.target.value);
  };

  const onSubmit = async () => {
    if (!inputValue.trim()) return;
    createComment({ interviewAnswerId, content: inputValue });
    setInputValue('');
  };

  const onKeyDown = (e: React.KeyboardEvent<HTMLTextAreaElement>) => {
    if (e.key === 'Enter' && !e.shiftKey) {
      e.preventDefault();
      onSubmit();
    }
  };

  // 댓글 수정
  const [updatedComment, setUpdatedComment] = useState<{
    id: number;
    content: string;
  }>();
  const updateComment = useUpdateComment(interviewAnswerId);

  // 댓글칸 활성화
  const handleCommentUpdate = (comment: string, commentId: number) => {
    setUpdatedComment({ id: commentId, content: comment });
  };

  // 댓글 수정 중
  const onCommentChange = (e: React.ChangeEvent<HTMLTextAreaElement>, commentId: number) => {
    setUpdatedComment((prev) => ({
      ...prev,
      id: commentId,
      content: e.target.value,
    }));
  };

  // 수정 취소
  const onCancelClick = () => {
    setUpdatedComment({ id: 0, content: '' });
  };

  // 수정 완료
  const onConfirmClick = async () => {
    if (!updatedComment) {
      return;
    }

    if (updatedComment.content === '') {
      toast.error('댓글을 입력하세요.', { position: 'bottom-center' });
      return;
    }

    updateComment({
      commentId: Number(updatedComment.id),
      content: updatedComment.content,
    });
    setUpdatedComment({ id: 0, content: '' });
  };

  // 댓글 삭제
  const deleteComment = useDeleteComment(interviewAnswerId);
  const handleCommentDelete = async (commentId: number) => {
    deleteComment(commentId);
  };

  return {
    inputValue,
    updatedComment,
    onChange,
    onSubmit,
    onKeyDown,
    handleCommentUpdate,
    onCommentChange,
    onCancelClick,
    onConfirmClick,
    handleCommentDelete,
  };
};
