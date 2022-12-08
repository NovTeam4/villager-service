package com.example.villagerservice.member.valid;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class DigitLengthValidator implements ConstraintValidator<DigitLength, Integer> {

    private int min;
    private int max;

    @Override
    public void initialize(DigitLength constraintAnnotation) {
        min = constraintAnnotation.min();
        max = constraintAnnotation.max();
    }

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        return min <= value && value <= max;
    }
}
