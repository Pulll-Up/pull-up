import { Comment } from '../comment';

// 댓글 작성 요청 타입
export type CommentCreateRequest = Pick<Comment, 'content'> & { interviewAnswerId: string };

// 댓글 수정 요청 타입
export type CommentUpdateRequest = Pick<Comment, 'commentId' | 'content'>;
