package com.kildeen.tollcalc;

import com.kildeen.workdaycheck.swe.HolidayOrWeekendCheckerMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.kildeen.tollcalc.FeePrice.*;
import static org.assertj.core.api.Assertions.assertThat;

class TollFeeCalculatorTest {
    TollFeeCalculator tc;
    HolidayOrWeekendCheckerMock checker;

    @BeforeEach
    void setUp() {

        checker = new HolidayOrWeekendCheckerMock();
        checker.fixedResponse(false);
        tc = new TollFeeCalculator(checker);
    }

    @Test
    void tollChargeCalc_can_calc_one_passage_7_45() {
        assertFee(MAX, createDate(7, 45));
    }

    @Test
    void tollChargeCalc_can_calc_passage_7_45_and_15_00() {

        assertFee(MAX + MEDIUM, createDate(7, 45), createDate(15, 00));
    }

    @Test
    void tollChargeCalc_can_calc_passage_7_45_and_two_15_00() {

        assertFee(MAX + MEDIUM, createDate(7, 45), createDate(15, 00), createDate(15, 00));
    }

    @Test
    void tollChargeCalc_can_calc_passage_00_45_and_05_59() {

        assertFee(NONE, createDate(00, 45), createDate(05, 59));
    }

    @Test
    void tollChargeCalc_can_calc_passage_05_59_and_07_00_and_18_00() {

        assertFee(NONE + MAX + LOW, createDate(05, 59), createDate(07, 00), createDate(18, 00));
    }

    @Test
    void tollChargeCalc_can_calc_passage_ten_in_a_row_at_07_00_to_07_10_to_just_one_fee() {
        List<LocalDateTime> passages = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            passages.add(createDate(07, i));
        }
        assertFee(MAX, passages.toArray(new LocalDateTime[10]));
    }

    @Test
    void tollChargeCalc_can_calc_passage_two_same_day() {

        assertFee(MAX + MAX, createDate(07, 01), createDate(16, 01));
    }

    @Test
    void tollChargeCalc_can_calc_passage_one_every_hour() {
        List<LocalDateTime> passages = new ArrayList<>();
        for (int i = 5; i < 19; i++) {
            passages.add(createDate(i, 1));
        }
        assertFee(MAX_DAILY_FEE, passages.toArray(new LocalDateTime[passages.size()]));
    }

    @Test
    void tollChargeCalc_can_handle_null() {
        assertFee(NONE);
    }

    @Test
    void tollChargeCalc_can_handle_empty() {
        assertFee(NONE, new LocalDateTime[0]);
    }

    @Test
    void tollFeeCalc_different_days() {
        assertFee((MAX + MAX) * 2, createDate(07, 01), createDate(16, 01),
                LocalDateTime.of(2020, 01, 11, 07, 01),
                LocalDateTime.of(2020, 01, 11, 16, 01));
    }

    @Test
    void tollFeeCalc_uses_HolidayOrWeekendChecker() {

        checker.fixedResponse(true);
        assertFee(NONE + NONE, createDate(07, 01), createDate(16, 01));
    }

    @Test
    void tollFeeCalc_uses_HolidayOrWeekendChecker_2() {

        checker.fixedResponse(null);
        checker.record(true).record(true).record(false);
        LocalDateTime date = createDate(07, 01);
        assertFee(NONE + NONE + MAX, date, date, date);
    }

    private void assertFee(int expectedFee, LocalDateTime... passages) {
        assertThat(tc.calculateFee(passages)).isEqualTo(expectedFee);
    }

    private LocalDateTime createDate(int hour, int minute) {
        return LocalDateTime.of(2020, 01, 10, hour, minute);
    }
}