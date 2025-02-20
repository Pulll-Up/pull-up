package com.pullup.game.dto;

import lombok.Builder;
import lombok.Getter;


@Getter
public class ProblemCard {
    private Long cardId;
    private CardType cardType;
    private Boolean disabled;
    private String content;

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


    public void disableCard() {
        this.disabled = true;
    }


}