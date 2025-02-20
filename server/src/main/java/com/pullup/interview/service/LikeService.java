package com.pullup.interview.service;

import com.pullup.common.exception.ErrorMessage;
import com.pullup.common.exception.NotFoundException;
import com.pullup.interview.domain.InterviewAnswer;
import com.pullup.interview.domain.Like;
import com.pullup.interview.repository.InterviewAnswerRepository;
import com.pullup.interview.repository.LikeRepository;
import com.pullup.member.domain.Member;
import com.pullup.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LikeService {

    private final MemberService memberService;
    private final InterviewAnswerRepository interviewAnswerRepository;
    private final LikeRepository likeRepository;

    public Boolean isLikedInterviewAnswerByMember(Long memberId, Long interviewAnswerId) {
        return likeRepository.findByMemberIdAndInterviewAnswerId(memberId, interviewAnswerId)
                .map(Like::getIsLiked)
                .orElse(false);
    }

    public Long getLikesCount(Long interviewAnswerId) {
        return likeRepository.findByInterviewAnswerId(interviewAnswerId)
                .stream()
                .filter(Like::isLiked)
                .count();
    }

    @Transactional
    public void toggleLike(Long memberId, Long interviewAnswerId) {
        Member member = memberService.findMemberById(memberId);
        InterviewAnswer interviewAnswer = interviewAnswerRepository.findById(interviewAnswerId)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.ERR_INTERVIEW_ANSWER_NOT_FOUND));

        likeRepository.findByMemberIdAndInterviewAnswerId(memberId, interviewAnswerId)
                .ifPresentOrElse(
                        Like::toggleLike,
                        () -> likeRepository.save(Like.createLike(member, interviewAnswer))
                );
    }
}