package com.example.authSecurity.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = AgeLimitValidator.class)
public @interface DoBLimit {
    int minAge() default 12;
    String message() default "User must be at least 12 years old";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
