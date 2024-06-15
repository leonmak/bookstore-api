package com.example.bookstore.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateOfBirthValidator implements ConstraintValidator<ValidDateOfBirth, String> {
    @Override
    public void initialize(ValidDateOfBirth constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String dobString, ConstraintValidatorContext constraintValidatorContext) {
        if (dobString == null) {
            return true;
        }
        try {
            var temp = DateTimeFormatter.ISO_LOCAL_DATE.parse(dobString);
            LocalDate dob = LocalDate.from(temp);
            return dob.isBefore(LocalDate.now().plusDays(1));
        } catch (Exception e) {
            return false;
        }
    }
}
