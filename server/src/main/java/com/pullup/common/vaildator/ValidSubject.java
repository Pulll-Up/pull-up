package com.pullup.common.vaildator;

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
    String message() default "Invalid subject";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}