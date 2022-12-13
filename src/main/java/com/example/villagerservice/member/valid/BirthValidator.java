package com.example.villagerservice.member.valid;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BirthValidator implements ConstraintValidator<Birth, String> {
    private static final java.text.SimpleDateFormat sdf =
            new java.text.SimpleDateFormat("yyyy-MM-dd");

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        if (value != null) {
            try {
                java.util.Date ret = sdf.parse(value.trim());
                if (sdf.format(ret).equals(value.trim())) {
                    return true;
                }
            } catch (ParseException e) {
                return false;
            }
        }
        return false;
    }
}
