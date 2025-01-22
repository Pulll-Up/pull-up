package com.pullup.common.vaildator;

import com.pullup.exam.domain.DifficultyLevel;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class DifficultyLevelValidator implements ConstraintValidator<ValidDifficultyLevel, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }
        try {
            DifficultyLevel.valueOf(value.toUpperCase());
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
