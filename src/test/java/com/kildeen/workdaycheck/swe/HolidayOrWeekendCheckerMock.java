package com.kildeen.workdaycheck.swe;

import java.time.LocalDate;
import java.util.ArrayDeque;
import java.util.Deque;

public class HolidayOrWeekendCheckerMock extends HolidayOrWeekendChecker {

    private Deque<Boolean> recorded = new ArrayDeque<>();

    private Boolean fixedResponse;

    @Override
    public boolean isHolidayOrWeekend(LocalDate date) {
        if (fixedResponse == null) {
            return recorded.pop();
        } else {
            return fixedResponse;
        }
    }

    public HolidayOrWeekendCheckerMock record(boolean value) {
        recorded.add(value);
        return this;
    }

    public void fixedResponse(Boolean fixedResponse) {
        this.fixedResponse = fixedResponse;
    }
}
