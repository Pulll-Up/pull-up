package com.pullup.common.annotation;

import com.pullup.common.vaildator.SubjectValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.TYPE_USE})  // TYPE_USE 추가
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = SubjectValidator.class)
public @interface ValidSubject {
    String message() default "과목은 COMPUTER_ARCHITECTURE, "
            + "OPERATING_SYSTEM, "
            + "NETWORK, "
            + "DATABASE, "
            + "ALGORITHM, "
            + "DATA_STRUCTURE "
            + "중에서 골라야 합니다";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}