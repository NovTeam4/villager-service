package com.example.villagerservice.member.valid;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class GendersValidator implements ConstraintValidator<Genders, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }

        value = value.toUpperCase();
        return value.equals("MAN") || value.equals("WOMAN");
    }
}
