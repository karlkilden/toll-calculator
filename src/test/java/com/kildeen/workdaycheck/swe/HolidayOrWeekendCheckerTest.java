package com.kildeen.workdaycheck.swe;

import com.aldaviva.easter4j.Easter4J;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.Month;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

class HolidayOrWeekendCheckerTest {
    HolidayOrWeekendChecker hc;
    HolidayOrWeekendCheckerSpy scSpy;

    @BeforeEach
    void setUp() {
        hc = new HolidayOrWeekendChecker();
        scSpy = new HolidayOrWeekendCheckerSpy();
    }

    @Test
    void saturday() {
        assertThat(hc.isHolidayOrWeekend(LocalDate.of(2020, 06, 27))).isTrue();
    }

    @Test
    void sunday() {
        assertThat(hc.isHolidayOrWeekend(LocalDate.of(2020, 06, 28))).isTrue();
    }

    @Test
    void skips_feb() {
        assertMonthSkipped(Month.FEBRUARY, 10);
    }


    @Test
    void skips_all_holidayFreeMonths() {
        for (Month m : HolidayOrWeekendChecker.HOLIDAY_FREE_MONTHS) {
            assertMonthSkipped(m, 10);
        }
    }

    @Test
    void newYearsDay() {
        assertIsHoliday(LocalDate.of(2020, Month.JANUARY, 01));
    }

    @Test
    void epiphany() {
        assertIsHoliday(LocalDate.of(2020, Month.JANUARY, 06));
    }

    @Test
    void internationalWorkersDay() {
        assertIsHoliday(LocalDate.of(2020, Month.MAY, 01));
    }

    @Test
    void nationalDay() {
        assertIsHoliday(LocalDate.of(2022, Month.JUNE, 6));
    }

    @Test
    void christmasEve() {
        assertIsHoliday(LocalDate.of(2020, Month.DECEMBER, 24));
    }

    @Test
    void christmasDay() {
        assertIsHoliday(LocalDate.of(2020, Month.DECEMBER, 25));
    }

    @Test
    void secondDayOfChristmas() {
        assertIsHoliday(LocalDate.of(2022, Month.DECEMBER, 26));
    }

    @Test
    void newYearsEve() {
        assertIsHoliday(LocalDate.of(2020, Month.DECEMBER, 31));
    }


    @Test
    void midsummerEve_2020() {
        assertIsHoliday(LocalDate.of(2020, Month.JUNE, 19));
    }

    @Test
    void midsummerEve_2021() {
        LocalDate.of(2021, Month.JUNE, 25);
        assertIsHoliday(LocalDate.of(2021, Month.JUNE, 25));
    }

    @Test
    void midsummerEve_2022() {

        assertIsHoliday(LocalDate.of(2022, Month.JUNE, 24));
    }


    @Test
    void midsummerEve_2023() {
        assertIsHoliday(LocalDate.of(2023, Month.JUNE, 23));
    }

    @Test
    void midsummerEve_2024() {
        assertIsHoliday(LocalDate.of(2024, Month.JUNE, 21));
    }

    @Test
    void midsummerEve_2025() {
        assertIsHoliday(LocalDate.of(2025, Month.JUNE, 20));
    }

    @Test
    void midsummerEve_2026() {
        assertIsHoliday(LocalDate.of(2026, Month.JUNE, 19));
    }

    @Test
    void midsummerEve_2027() {
        assertIsHoliday(LocalDate.of(2027, Month.JUNE, 25));
    }

    @Test
    void midsummerEve_2028() {
        assertIsHoliday(LocalDate.of(2028, Month.JUNE, 23));
    }

    @Test
    void midsummerEve_2029() {
        assertIsHoliday(LocalDate.of(2029, Month.JUNE, 22));
    }

    @Test
    void midsummerEve_2030() {
        assertIsHoliday(LocalDate.of(2030, Month.JUNE, 21));
    }

    @Test
    void goodFriday_2020() {
        assertIsHoliday(LocalDate.of(2020, Month.APRIL, 10));
    }

    @Test
    void easterMonday_2020() {
        assertIsHoliday(LocalDate.of(2020, Month.APRIL, 13));
    }

    @Test
    void ascension_2020() {
        assertIsHoliday(LocalDate.of(2020, Month.MAY, 21));
    }

    @Test
    void easterChecks_2021_to_2031() {
        for (int i = 0; i < 10; i++) {
            LocalDate easterDate = Easter4J.getEaster(2021 + i);
            LocalDate goodFriday = easterDate.minusDays(2);
            LocalDate ascension = easterDate.plusDays(39);
            LocalDate easterMonday = easterDate.plusDays(1);

            assertIsHoliday(goodFriday);
            assertIsHoliday(ascension);
            assertIsHoliday(easterMonday);

        }
    }

    @Test
    void not_weekendOrHoliday_sanity_test() {

        assertThat(hc.isHolidayOrWeekend(LocalDate.of(2020, 06, 30))).isFalse();
        assertThat(hc.isHolidayOrWeekend(LocalDate.of(2020, 04, 16))).isFalse();
        assertThat(hc.isHolidayOrWeekend(LocalDate.of(2020, 12, 17))).isFalse();
    }

    /**
     * A crude test to indicate performance. Disabled can be removed while doing perf-sanity-checks.
     * E.g. At least some proof that skipping months with no Holidays is worthwhile.
     * (70% more computing time on my machine if months with no holidays are not skipped.)
     */
    @Test
    @Disabled
    void perf() {
        //CachingHolidayOrWeekendChecker chc = new CachingHolidayOrWeekendChecker(hc);
        long start = System.currentTimeMillis();
        for (int i = 0; i < 10; i++) {

            for (int j = 0; j < 10000000; j++) {
                LocalDate date = LocalDate.of(2020, 01, 01);
                date = date.plusDays(j);
                hc.isHolidayOrWeekend(date);
                //  chc.isHolidayOrWeekend(date);

            }
        }
        System.out.println(TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - start));

    }

    private void assertIsHoliday(LocalDate date) {
        assertThat(hc.isHolidayOrWeekend(date)).isTrue();
    }

    private void assertMonthSkipped(Month month, int day) {
        scSpy.isHolidayOrWeekend(LocalDate.of(2020, month, day));
        assertThat(scSpy.getHolidayFreeMonthCalled()).isTrue();
    }
}