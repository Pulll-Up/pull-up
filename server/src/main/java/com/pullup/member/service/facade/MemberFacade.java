package com.pullup.member.service.facade;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pullup.interview.domain.Interview;
import com.pullup.interview.domain.InterviewAnswer;
import com.pullup.interview.dto.request.MyInterviewAnswerRequest;
import com.pullup.interview.dto.response.ChatGptResponse;
import com.pullup.interview.dto.response.MyInterviewAnswerResponse;
import com.pullup.interview.service.ChatGptService;
import com.pullup.interview.service.InterviewService;
import com.pullup.interview.util.PromptGenerator;
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

    @Transactional
    public CompletableFuture<MyInterviewAnswerResponse> submitInterviewAnswer(
            Long memberId,
            Long interviewId,
            MyInterviewAnswerRequest myInterviewAnswerRequest
    ) {
        Member member = memberService.findMemberById(memberId);
        Interview interview = interviewService.findInterviewById(interviewId);

        memberService.updateSolveStatus(member);

        String prompt = PromptGenerator.generatePrompt(interview, myInterviewAnswerRequest);

        return CompletableFuture.supplyAsync(() -> {
            ChatGptResponse gptResponse = chatGptService.analyzeAnswer(prompt);
            String responseContent = gptResponse.getGptMessageContent();
            System.out.println("GPT Response: " + responseContent);  // 응답 로그 찍기

            return responseContent;
        }, executorService).thenApply(responseContent -> {
            String strength = extractJsonField(responseContent, "strength");
            String weakness = extractJsonField(responseContent, "weakness");

            InterviewAnswer interviewAnswer = interviewService.saveInterviewAnswer(
                    member,
                    interview,
                    strength,
                    weakness,
                    myInterviewAnswerRequest.answer()
            );

            return MyInterviewAnswerResponse.of(interviewId, interviewAnswer.getId());
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

    public Long getTodayInterviewAnswerId(Long memberId) {
        return interviewService.getTodayInterviewAnswerId(memberId);
    }
}
