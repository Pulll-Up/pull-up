package com.pullup.game.dto;

import lombok.Builder;

@Builder
public record ProblemCard(
        Long cardId,
        CardType cardType,
        Boolean disabled,
        String content
) {
    public static ProblemCard createNewProblemCard(Long cardId, CardType cardType, String content) {
        return ProblemCard.builder()
                .cardId(cardId)
                .cardType(cardType)
                .disabled(false)
                .content(content)
                .build();
    }

}