package com.example.villagerservice.common.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;

public abstract class DateCalculateConverter {

    public static String dateCalculate(LocalDateTime createdDate) {
        Period diff = Period.between(createdDate.toLocalDate(), LocalDate.now());
        if (diff.getYears() > 0) {
            return String.format("%s년 이상 전", diff.getYears());
        } else if (diff.getMonths() > 0) {
            return String.format("약 %s개월 전", diff.getMonths());
        } else {
            return StringConverter.localDateTimeToLocalDateString(createdDate);
        }
    }
}
