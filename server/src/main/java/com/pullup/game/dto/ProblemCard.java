package com.pullup.game.dto;

import java.util.concurrent.atomic.AtomicBoolean;
import lombok.Builder;
import lombok.Getter;


@Getter
public class ProblemCard {
    private Long cardId;
    private CardType cardType;
    private Boolean disabled;
    private String content;
    private final AtomicBoolean isProcessing = new AtomicBoolean(false);

    @Builder
    private ProblemCard(Long cardId, CardType cardType, Boolean disabled, String content) {
        this.cardId = cardId;
        this.cardType = cardType;
        this.disabled = disabled;
        this.content = content;
    }

    public static ProblemCard createNewProblemCard(Long cardId, CardType cardType, String content) {
        return ProblemCard.builder()
                .cardId(cardId)
                .cardType(cardType)
                .disabled(false) // 기본값 false
                .content(content)
                .build();
    }

    public boolean tryProcessing() {
        if (disabled) {
            return false;
        }
        return isProcessing.compareAndSet(false, true);
    }


    public void disableCard() {
        this.disabled = true;
    }


}