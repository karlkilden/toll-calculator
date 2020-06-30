package com.kildeen.workdaycheck.swe;

import java.time.LocalDate;

public class HolidayOrWeekendCheckerSpy extends HolidayOrWeekendChecker {

    private Boolean isHolidayFreeMonthCalled;
    private Boolean isEasterOptimizationUsed;

    @Override
    boolean isHolidayFreeMonth(LocalDate date) {
        return isHolidayFreeMonthCalled = super.isHolidayFreeMonth(date);
    }

    public Boolean getHolidayFreeMonthCalled() {
        return isHolidayFreeMonthCalled;
    }
}
