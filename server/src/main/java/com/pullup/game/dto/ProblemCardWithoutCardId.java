package com.pullup.game.dto;

import lombok.Builder;

@Builder
public record ProblemCardWithoutCardId(
        CardType cardType,
        Boolean disabled,
        String content
) {
    public static ProblemCardWithoutCardId from(ProblemCard problemCard) {
        return ProblemCardWithoutCardId.builder()
                .cardType(problemCard.getCardType())
                .disabled(problemCard.getDisabled())
                .content(problemCard.getContent())
                .build();
    }
}
