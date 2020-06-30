package com.kildeen.workdaycheck.swe;

import com.aldaviva.easter4j.Easter4J;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public class HolidayOrWeekendChecker {
    static final EnumSet<Month> HOLIDAY_FREE_MONTHS = EnumSet.of(Month.FEBRUARY, Month.JULY, Month.AUGUST, Month.SEPTEMBER, Month.OCTOBER, Month.NOVEMBER);
    List<HolidayMatch> holidays = new ArrayList<>();

    public HolidayOrWeekendChecker() {
        holidays.add(this::isNewYearsDay);
        holidays.add(this::epiphany);
        holidays.add(this::internationalWorkersDay);
        holidays.add(this::nationalDay);
        holidays.add(this::christmasEve);
        holidays.add(this::christmasDay);
        holidays.add(this::secondDayOfChristmas);
        holidays.add(this::newYearsEve);
    }

    public boolean isHolidayOrWeekend(LocalDate date) {

        if (date.getDayOfWeek() == DayOfWeek.SATURDAY || date.getDayOfWeek() == DayOfWeek.SUNDAY) {
            return true;
        }

        if (isHolidayFreeMonth(date)) {
            return false;
        }
        boolean isFixedHoliday = holidays.stream().map(holidayMatch -> holidayMatch.isHolidayDate(date)).filter(v -> v).findAny().orElse(false);
        if (isFixedHoliday) {
            return true;
        }

        if (isMidsummerEve(date)) {
            return true;
        }
        LocalDate easterDate = Easter4J.getEaster(date.getYear());

        LocalDate goodFriday = easterDate.minusDays(2);
        LocalDate ascension = easterDate.plusDays(39);
        LocalDate easterMonday = easterDate.plusDays(1);

        if (date.equals(goodFriday) || date.equals(ascension) || date.equals(easterMonday)) {
            return true;
        }
        return false;
    }

    private boolean isMidsummerEve(LocalDate date) {
        return date.getMonth() == Month.JUNE && date.getDayOfMonth() >= 19 && date.getDayOfMonth() <= 25 && date.getDayOfWeek() == DayOfWeek.FRIDAY;
    }

    boolean isHolidayFreeMonth(LocalDate date) {
        return HOLIDAY_FREE_MONTHS.contains(date.getMonth());
    }

    private boolean isNewYearsDay(LocalDate date) {
        return date.getMonth() == Month.JANUARY && date.getDayOfMonth() == 1;
    }

    private boolean epiphany(LocalDate date) {
        return date.getMonth() == Month.JANUARY && date.getDayOfMonth() == 6;
    }

    private boolean internationalWorkersDay(LocalDate date) {
        return date.getMonth() == Month.MAY && date.getDayOfMonth() == 1;
    }

    private boolean nationalDay(LocalDate date) {
        return date.getMonth() == Month.JUNE && date.getDayOfMonth() == 6;
    }

    private boolean christmasEve(LocalDate date) {
        return date.getMonth() == Month.DECEMBER && date.getDayOfMonth() == 24;
    }

    private boolean christmasDay(LocalDate date) {
        return date.getMonth() == Month.DECEMBER && date.getDayOfMonth() == 25;
    }

    private boolean secondDayOfChristmas(LocalDate date) {
        return date.getMonth() == Month.DECEMBER && date.getDayOfMonth() == 26;
    }

    private boolean newYearsEve(LocalDate date) {
        return date.getMonth() == Month.DECEMBER && date.getDayOfMonth() == 31;
    }
}