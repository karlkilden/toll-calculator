package com.kildeen.workdaycheck.swe;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * {@link CachingHolidayOrWeekendChecker} is not currently not in use pending cache eviction strategy during production.
 * But using {@link HolidayOrWeekendCheckerTest#perf()} A  performance enhancement could be noted (60% on my machine).
 * Makes sense since the same date will occur many many times as many daily passages is the root cause for having a toll in the first place
 * The cache should probably be warmed-up as we know all keys will be used eventually. E.g populate all possible dayOfYear eagerly.
 */
class CachingHolidayOrWeekendCheckerTest {
    HolidayOrWeekendCheckerMock mock = new HolidayOrWeekendCheckerMock();
    CachingHolidayOrWeekendChecker chc = new CachingHolidayOrWeekendChecker(mock);

    @Test
    void delegates_to_checker_and_caches_result() {
        mock.record(false).record(true).record(true);

        assertDelegatedAndCached(LocalDate.of(2020, 12, 24), false);
        assertDelegatedAndCached(LocalDate.of(2020, 06, 29), true);
        assertDelegatedAndCached(LocalDate.of(2020, 06, 30), true);

    }

    private void assertDelegatedAndCached(LocalDate date, boolean expectedValue) {
        assertThat(chc.isHolidayOrWeekend(date)).isEqualTo(expectedValue);
        assertThat(chc.holidayCache.get(date.getDayOfYear())).isEqualTo(expectedValue);
        chc.holidayCache.put(date.getDayOfYear(), !expectedValue);
        assertThat(chc.isHolidayOrWeekend(date)).isNotEqualTo(expectedValue);

    }


}