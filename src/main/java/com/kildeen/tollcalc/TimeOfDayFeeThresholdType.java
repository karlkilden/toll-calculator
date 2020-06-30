package com.kildeen.tollcalc;

import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

public enum TimeOfDayFeeThresholdType {

    ZERO_FIVE_FIVE_NINE(FeePrice.NONE, LocalTime.of(5, 59)),
    ZERO_SIX_ZERO_ZERO(FeePrice.LOW, LocalTime.of(6, 0)),
    ZERO_SIX_THREE_ZERO(FeePrice.MEDIUM, LocalTime.of(6, 30)),
    ZERO_SEVEN_ZERO_ZERO(FeePrice.MAX, LocalTime.of(7, 0)),
    ZERO_EIGHT_ZERO_ZERO(FeePrice.MEDIUM, LocalTime.of(8, 0)),
    ZERO_EIGHT_THREE_ZERO(FeePrice.LOW, LocalTime.of(8, 30)),
    ONE_FIVE_ZERO_ZERO(FeePrice.MEDIUM, LocalTime.of(15, 0)),
    ONE_FIVE_THREE_ZERO(FeePrice.MAX, LocalTime.of(15, 30)),
    ONE_SEVEN_ZERO_ZERO(FeePrice.MEDIUM, LocalTime.of(17, 0)),
    ONE_EIGHT_ZERO_ZERO(FeePrice.LOW, LocalTime.of(18, 0)),
    ONE_EIGHT_THREE_ZERO(FeePrice.NONE, LocalTime.of(18, 30));


    public static final int MINUTES_PER_HOUR = 60;
    private static final int HOURS_PER_DAY = 24;
    private static List<TimeOfDayFeeThresholdType> valuesAsList = Arrays.asList(values());
    private static List<LocalTime> thresholds = getValuesAsList().stream().map(v -> v.getThreshold()).collect(Collectors.toList());

    private static Map<LocalTime, Integer> thresholdToFee =
            getValuesAsList().stream().collect(Collectors.toMap(TimeOfDayFeeThresholdType::getThreshold, TimeOfDayFeeThresholdType::getFee));
    private static Map<LocalTime, Integer> minuteOfDayToFee = new HashMap<>();

    static {

        for (int i = 0; i < HOURS_PER_DAY; i++) {
            for (int j = 0; j < MINUTES_PER_HOUR; j++) {
                LocalTime currentTime = LocalTime.of(i, j);
                minuteOfDayToFee.put(currentTime, calcFeeForTime(currentTime));

            }

        }
    }

    private int fee;
    private LocalTime threshold;

    TimeOfDayFeeThresholdType(int fee, LocalTime threshold) {
        this.fee = fee;
        this.threshold = threshold;
    }

    public static List<TimeOfDayFeeThresholdType> getValuesAsList() {
        return valuesAsList;
    }

    public static Map<LocalTime, Integer> getThresholdToFee() {
        return thresholdToFee;
    }

    private static Integer calcFeeForTime(LocalTime currentTime) {
        return thresholdToFee.get(findFee(currentTime));
    }

    private static LocalTime findFee(LocalTime currentTime) {

        int index = Collections.binarySearch(thresholds, currentTime);
        if (index < 0) {
            index = (-index - 2);
        }
        index = Math.max(0, index);
        return thresholds.get(index);
    }

    public static Integer getFee(LocalTime time) {
        return minuteOfDayToFee.get(time);

    }

    public int getFee() {
        return fee;
    }

    public LocalTime getThreshold() {
        return threshold;
    }

}