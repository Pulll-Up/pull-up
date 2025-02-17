package com.pullup.common.annotation;

import com.pullup.common.vaildator.DifficultyLevelValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = DifficultyLevelValidator.class)
public @interface ValidDifficultyLevel {
    String message() default "난이도는 HARD, MEDIUM, EASY 중 한가지를 골라야 합니다";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}