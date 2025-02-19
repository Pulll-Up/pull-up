import { useGetAuthInfo } from '@/api/auth';
import { useGetComments } from '@/api/comment';
import { useCreateInterviewAnswerLike, useGetInterviewAnswerDetail } from '@/api/interview';
import RouteHeader from '@/components/common/routeheader';
import CommentItem from '@/components/interview/commentItem';
import InputForm from '@/components/interview/inputForm';
import InterviewAnswerItem from '@/components/interview/interviewAnswerItem';
import { useComment } from '@/hooks/useComment';
import convertDate from '@/utils/convertDate';
import { useNavigate, useParams } from 'react-router-dom';

const InterviewAnswerDetail = () => {
  const navigate = useNavigate();
  const { interviewId, interviewAnswerId } = useParams();
  const { authInfo } = useGetAuthInfo();
  const { data: interviewAnswer } = useGetInterviewAnswerDetail(interviewAnswerId!);
  const { data: comments } = useGetComments(interviewAnswerId!);

  // 댓글 훅
  const {
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
  } = useComment({ interviewAnswerId: interviewAnswerId! });

  // 다른 사람 답변 목록으로 돌아가기
  const onBackClick = () => {
    navigate(-1);
  };

  // 좋아요 토글
  const likeMutation = useCreateInterviewAnswerLike(interviewId!);
  const handleLikeClick = () => {
    likeMutation(interviewAnswerId!);
  };

  if (!authInfo || !interviewAnswer || !comments) return null;

  return (
    <div className="min-h-full bg-Main px-6 py-10 md:px-10 xl:px-20">
      <div className="mt-[94px] flex flex-col gap-4 rounded-2xl sm:mt-16 md:gap-6 md:border md:border-primary-200 md:bg-white md:p-6">
        <RouteHeader prev="다른 사람의 답변 목록" title="답변 상세 보기" onBackClick={onBackClick} />
        <InterviewAnswerItem
          id={interviewAnswer.interviewAnswerId}
          userName={interviewAnswer.memberName}
          content={interviewAnswer.answer}
          keywords={interviewAnswer.keywords}
          date={convertDate(interviewAnswer.createdAt)}
          likeCount={interviewAnswer.likeCount}
          commentCount={interviewAnswer.commentCount}
          liked={interviewAnswer.isLiked}
          handleLikeClick={handleLikeClick}
        />
        <InputForm
          id="answerComment"
          placeholder="댓글을 입력하세요"
          value={inputValue}
          onChange={onChange}
          onSubmit={onSubmit}
          onKeyDown={onKeyDown}
        />
        <div>
          {comments && comments.length > 0 ? (
            comments.map((comment, index) => (
              <div key={index}>
                <CommentItem
                  userEmail={authInfo.email}
                  comment={comment}
                  handleDelete={handleCommentDelete}
                  handleUpdate={handleCommentUpdate}
                  onCancelClick={onCancelClick}
                  onChange={onCommentChange}
                  onConfirmClick={onConfirmClick}
                  value={updatedComment?.id === comment.commentId ? updatedComment?.content : comment.content}
                  updated={updatedComment?.id === comment.commentId}
                />
              </div>
            ))
          ) : (
            <div>댓글 목록이 없습니다.</div>
          )}
        </div>
      </div>
    </div>
  );
};
export default InterviewAnswerDetail;
