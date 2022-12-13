package com.example.villagerservice.member.valid;

import javax.validation.Constraint;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = BirthValidator.class)
public @interface Birth {
    String message() default "생년월일을 확인해주세요.";
    Class[] groups() default {};
    Class[] payload() default {};
}
