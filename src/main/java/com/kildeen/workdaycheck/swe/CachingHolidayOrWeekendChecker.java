package com.kildeen.workdaycheck.swe;

import java.time.LocalDate;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CachingHolidayOrWeekendChecker {

    @VisibleForTesting
    Map<Integer, Boolean> holidayCache;
    private HolidayOrWeekendChecker checker;

    public CachingHolidayOrWeekendChecker(HolidayOrWeekendChecker checker) {
        this.checker = checker;
        holidayCache = new ConcurrentHashMap<>();
    }

    public boolean isHolidayOrWeekend(LocalDate date) {
        return holidayCache.computeIfAbsent(date.getDayOfYear(), s -> checker.isHolidayOrWeekend(date));
    }

}