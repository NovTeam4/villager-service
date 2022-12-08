package com.example.villagerservice.member.valid;

import javax.validation.Constraint;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = DigitLengthValidator.class)
public @interface DigitLength {
    String message() default "성별은 MAN 또는 WOMAN만 가능합니다.";
    Class[] groups() default {};
    Class[] payload() default {};
    int min() default 0;
    int max() default 0;
}
