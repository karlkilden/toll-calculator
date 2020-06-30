package com.kildeen.tollcalc;

import com.kildeen.workdaycheck.swe.HolidayOrWeekendChecker;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class TollFeeCalculator {

    private HolidayOrWeekendChecker checker;

    public TollFeeCalculator(HolidayOrWeekendChecker checker) {
        this.checker = checker;
    }

    public int calculateFee(LocalDateTime... tollPassages) {
        if (tollPassages == null) {
            return 0;
        }
        Map<Integer, Map<LocalDateTime, Integer>> dayAndHourMapped = new HashMap<>();

        LocalDateTime lastAddedHourMapped = null;
        for (LocalDateTime time : tollPassages) {
            if (checker.isHolidayOrWeekend(time.toLocalDate())) {
                continue;
            }
            Map<LocalDateTime, Integer> hourMapped = dayAndHourMapped.computeIfAbsent(time.getDayOfYear(), k -> new HashMap<>());
            Integer currentFee = TimeOfDayFeeThresholdType.getFee(time.toLocalTime());
            if (lastAddedHourMapped == null || isNewHour(lastAddedHourMapped, time)) {
                hourMapped.put(time, currentFee);
                lastAddedHourMapped = time;
            } else {
                hourMapped.put(lastAddedHourMapped, Math.max(hourMapped.get(lastAddedHourMapped), TimeOfDayFeeThresholdType.getFee(time.toLocalTime())));
            }
        }
        int fee = 0;
        for (Integer dayOfYear : dayAndHourMapped.keySet()) {
            Map<LocalDateTime, Integer> currentHourMapped = dayAndHourMapped.get(dayOfYear);
            fee += Math.min(FeePrice.MAX_DAILY_FEE, currentHourMapped.values().stream().collect(Collectors.summarizingInt(v -> v.intValue())).getSum());
        }

        Map<LocalDateTime, Integer> sum = dayAndHourMapped.entrySet().stream()
                .flatMap(m -> m.getValue().entrySet().stream())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, Integer::sum));
        sum.entrySet();
        return fee;
    }

    private boolean isNewHour(LocalDateTime currentHour, LocalDateTime passage) {

        return Duration.between(currentHour, passage).toMinutes() > TimeOfDayFeeThresholdType.MINUTES_PER_HOUR;
    }
}
