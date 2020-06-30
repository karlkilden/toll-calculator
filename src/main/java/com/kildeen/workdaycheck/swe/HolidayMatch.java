package com.kildeen.workdaycheck.swe;

import java.time.LocalDate;

@FunctionalInterface
public interface HolidayMatch {

    boolean isHolidayDate(LocalDate date);


}
