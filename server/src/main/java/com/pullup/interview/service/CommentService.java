package com.pullup.interview.service;

import com.pullup.common.exception.ErrorMessage;
import com.pullup.common.exception.NotFoundException;
import com.pullup.interview.domain.Comment;
import com.pullup.interview.domain.InterviewAnswer;
import com.pullup.interview.dto.request.PostCommentRequest;
import com.pullup.interview.dto.response.PostCommentResponse;
import com.pullup.interview.repository.CommentRepository;
import com.pullup.interview.repository.InterviewAnswerRepository;
import com.pullup.member.domain.Member;
import com.pullup.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {

    private final MemberService memberService;
    private final InterviewAnswerRepository interviewAnswerRepository;
    private final CommentRepository commentRepository;

    @Transactional
    public PostCommentResponse postComment(
            Long memberId,
            Long interviewAnswerId,
            PostCommentRequest postCommentRequest
    ) {
        Member member = memberService.findMemberById(memberId);
        InterviewAnswer interviewAnswer = findInterviewAnswerById(interviewAnswerId);

        Long commentId = commentRepository.save(
                Comment.createComment(
                        postCommentRequest.content(),
                        member,
                        interviewAnswer)
        ).getId();

        return PostCommentResponse.of(commentId);
    }

    public Integer getCommentsCount(Long interviewAnswerId) {
        return commentRepository.countByInterviewAnswerId(interviewAnswerId);
    }

    private InterviewAnswer findInterviewAnswerById(Long interviewAnswerId) {
        return interviewAnswerRepository.findById(interviewAnswerId)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.ERR_INTERVIEW_ANSWER_NOT_FOUND));
    }

    @Transactional
    public void modifyComment(Long commentId, PostCommentRequest postCommentRequest) {
        Comment comment = findCommentById(commentId);
        comment.modifyContent(postCommentRequest.content());
    }

    private Comment findCommentById(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.ERR_COMMENT_NOT_FOUND));
    }

    @Transactional
    public void deleteComment(Long commentId) {
        commentRepository.deleteById(commentId);
    }
}
