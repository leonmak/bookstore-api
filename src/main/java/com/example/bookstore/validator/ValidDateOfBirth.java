package com.example.bookstore.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = DateOfBirthValidator.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface ValidDateOfBirth {
    String message() default "should be in format yyyy-MM-dd and be before or on today's date";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

}
