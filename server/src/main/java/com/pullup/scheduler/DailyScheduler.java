package com.pullup.scheduler;

import com.pullup.external.fcm.FcmService;
import com.pullup.interview.domain.DailyQuiz;
import com.pullup.interview.domain.Interview;
import com.pullup.interview.repository.DailyQuizRepository;
import com.pullup.interview.service.InterviewService;
import com.pullup.member.domain.Member;
import com.pullup.member.service.MemberService;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DailyScheduler {

    private final MemberService memberService;
    private final InterviewService interviewService;
    private final FcmService fcmService;
    private final DailyQuizRepository dailyQuizRepository;

    @Scheduled(cron = "0 0 0 * * *")
    public void dailyScheduler() {
        List<Member> members = memberService.findAllMembers();

        dailyQuizRepository.deleteAllInBatch();

        List<DailyQuiz> dailyQuizzes = new ArrayList<>();
        members.forEach(member -> {
            Interview chosenInterview = interviewService.getRandomInterview(member.getId());
            memberService.updateSolvedDaysWithLeftShift(member);
            dailyQuizzes.add(
                    DailyQuiz.createDailyQuiz(chosenInterview.getQuestion(), member.getId(), chosenInterview.getId()));
        });
        interviewService.saveAllDailyQuiz(dailyQuizzes);
    }

    @Scheduled(cron = "0 5 13 * * *")
    public void sendMessage(){
        fcmService.sendNotifications();
    }
}
