package com.kildeen.tollcalc;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;

import static com.kildeen.tollcalc.FeePrice.*;
import static org.assertj.core.api.Assertions.assertThat;

class TimeOfDayFeeThresholdTypeTest {

    @Test
    void ZERO_FIVE_FIVE_NINE() {
        assertTimeAndFee(TimeOfDayFeeThresholdType.ZERO_FIVE_FIVE_NINE, NONE, "05:59");
    }

    @Test
    void ZERO_SIX_ZERO_ZERO() {
        assertTimeAndFee(TimeOfDayFeeThresholdType.ZERO_SIX_ZERO_ZERO, LOW, "06:00");
    }

    @Test
    void ZERO_SIX_THREE_ZERO() {
        assertTimeAndFee(TimeOfDayFeeThresholdType.ZERO_SIX_THREE_ZERO, MEDIUM, "06:30");
    }

    @Test
    void ZERO_SEVEN_ZERO_ZERO() {
        assertTimeAndFee(TimeOfDayFeeThresholdType.ZERO_SEVEN_ZERO_ZERO, MAX, "07:00");
    }

    @Test
    void ZERO_EIGHT_ZERO_ZERO() {
        assertTimeAndFee(TimeOfDayFeeThresholdType.ZERO_EIGHT_ZERO_ZERO, MEDIUM, "08:00");
    }

    @Test
    void ZERO_EIGHT_THREE_ZERO() {
        assertTimeAndFee(TimeOfDayFeeThresholdType.ZERO_EIGHT_THREE_ZERO, LOW, "08:30");
    }

    @Test
    void ONE_FIVE_ZERO_ZERO() {
        assertTimeAndFee(TimeOfDayFeeThresholdType.ONE_FIVE_ZERO_ZERO, MEDIUM, "15:00");
    }

    @Test
    void ONE_FIVE_THREE_ZERO() {
        assertTimeAndFee(TimeOfDayFeeThresholdType.ONE_FIVE_THREE_ZERO, MAX, "15:30");
    }

    @Test
    void ONE_SEVEN_ZERO_ZERO() {
        assertTimeAndFee(TimeOfDayFeeThresholdType.ONE_SEVEN_ZERO_ZERO, MEDIUM, "17:00");
    }

    @Test
    void ONE_EIGHT_ZERO_ZERO() {
        assertTimeAndFee(TimeOfDayFeeThresholdType.ONE_EIGHT_ZERO_ZERO, LOW, "18:00");
    }

    @Test
    void ONE_EIGHT_THREE_ZERO() {
        assertTimeAndFee(TimeOfDayFeeThresholdType.ONE_EIGHT_THREE_ZERO, NONE, "18:30");
    }


    @Test
    void getFee_00_00() {
        assertGetFee(00, 00, NONE);
    }


    @Test
    void getFee_02_03() {
        assertGetFee(02, 03, NONE);
    }

    @Test
    void getFee_06_30() {
        assertGetFee(6, 30, MEDIUM);
    }

    @Test
    void getFee_07_30() {
        assertGetFee(7, 30, MAX);
    }

    @Test
    void getFee_09_30() {
        assertGetFee(9, 30, LOW);
    }

    @Test
    void getFee_15_41() {
        assertGetFee(15, 41, MAX);
    }

    @Test
    void getFee_17_03() {
        assertGetFee(17, 03, MEDIUM);
    }

    @Test
    void getFee_18_31() {
        assertGetFee(18, 31, NONE);
    }

    @Test
    void getFee_23_39() {
        assertGetFee(23, 39, NONE);
    }


    private void assertGetFee(int hour, int minute, int expectedFee) {
        int fee = TimeOfDayFeeThresholdType.getFee(LocalTime.of(hour, minute));
        assertThat(fee).isEqualTo(expectedFee);
    }


    private void assertTimeAndFee(TimeOfDayFeeThresholdType type, int expectedFee, String expectedReadableTime) {
        Assertions.assertThat(type.getFee()).isEqualTo(expectedFee);
        Assertions.assertThat(type.getThreshold().toString()).isEqualTo(expectedReadableTime);
        Assertions.assertThat(TimeOfDayFeeThresholdType.getFee(type.getThreshold())).isEqualTo(expectedFee);
    }
}