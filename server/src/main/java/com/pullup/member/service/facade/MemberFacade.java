package com.pullup.member.service.facade;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pullup.common.exception.BadRequestException;
import com.pullup.common.exception.ErrorMessage;
import com.pullup.common.util.IdEncryptionUtil;
import com.pullup.external.gpt.dto.response.ChatGptResponse;
import com.pullup.external.gpt.service.ChatGptService;
import com.pullup.external.gpt.util.PromptGenerator;
import com.pullup.interview.domain.Interview;
import com.pullup.interview.domain.InterviewAnswer;
import com.pullup.interview.dto.request.MyInterviewAnswerRequest;
import com.pullup.interview.dto.response.MyInterviewAnswerResponse;
import com.pullup.interview.dto.response.MyInterviewAnswerResultResponse;
import com.pullup.interview.service.InterviewService;
import com.pullup.member.domain.Member;
import com.pullup.member.service.MemberService;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberFacade {
    private final MemberService memberService;
    private final InterviewService interviewService;
    private final ChatGptService chatGptService;
    private final ExecutorService executorService = Executors.newFixedThreadPool(10);
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final IdEncryptionUtil idEncryptionUtil;

    @Transactional
    public MyInterviewAnswerResponse submitInterviewAnswer(
            Long memberId,
            Long interviewId,
            MyInterviewAnswerRequest myInterviewAnswerRequest
    ) {
        Member member = memberService.findMemberById(memberId);
        Interview interview = interviewService.findInterviewById(interviewId);

        if (interviewService.isAnswered(member, interview)) {
            throw new BadRequestException(ErrorMessage.ERR_INTERVIEW_ALREADY_ANSWERED);
        }

        InterviewAnswer interviewAnswer = interviewService.saveInterviewAnswer(
                member,
                interview,
                myInterviewAnswerRequest.answer()
        );

        memberService.updateSolveStatus(member);

        return MyInterviewAnswerResponse.of(idEncryptionUtil.encrypt(interviewId),
                idEncryptionUtil.encrypt(interviewAnswer.getId()));
    }

    @Transactional
    public CompletableFuture<MyInterviewAnswerResultResponse> getOrGenerateMyInterviewAnswerResult(
            Long interviewAnswerId) {
        InterviewAnswer interviewAnswer = interviewService.findByIdWithInterview(interviewAnswerId);

        if (interviewAnswer.getStrength() != null && interviewAnswer.getWeakness() != null) {
            MyInterviewAnswerResultResponse myInterviewAnswerResultResponse = interviewService.buildMyInterviewAnswerResultResponse(
                    interviewAnswer, interviewAnswerId);
            return CompletableFuture.completedFuture(myInterviewAnswerResultResponse);
        }

        String prompt = PromptGenerator.generatePrompt(interviewAnswer.getInterview(), interviewAnswer.getAnswer());

        return CompletableFuture.supplyAsync(() -> {
            ChatGptResponse gptResponse = chatGptService.analyzeAnswer(prompt);
            return gptResponse.getGptMessageContent();
        }, executorService).thenApply(responseContent -> {
            String strength = extractJsonField(responseContent, "strength");
            String weakness = extractJsonField(responseContent, "weakness");

            interviewAnswer.updateAnswer(strength, weakness);

            return interviewService.buildMyInterviewAnswerResultResponse(interviewAnswer, interviewAnswerId);
        });
    }

    private String extractJsonField(String jsonResponse, String fieldName) {
        try {
            JsonNode jsonNode = objectMapper.readTree(jsonResponse);
            return jsonNode.has(fieldName) ? jsonNode.get(fieldName).asText() : fieldName + " 분석 실패";
        } catch (Exception e) {
            return fieldName + " 분석 실패";
        }
    }

    public String getEncryptedTodayInterviewAnswerId(Long memberId) {
        Long todayInterviewAnswerId = interviewService.getTodayInterviewAnswerId(memberId);
        return idEncryptionUtil.encrypt(todayInterviewAnswerId);
    }
}
