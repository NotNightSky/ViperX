package com.Nightsky.viperX.utils;

import java.util.regex.*;
import java.util.HashMap;
import java.util.Map;

public class durationParser {
    //Don't ask me to explain me this code, I did not write this
    //some unused stuff will be used with litebans so do not remove them
    private static final Map<String, Long> timeUnits = new HashMap<>();

    static {
        timeUnits.put("y", 365L * 24 * 60 * 60 * 1000); // Approximate year
        timeUnits.put("w", 7L * 24 * 60 * 60 * 1000);   // Fixed week
        timeUnits.put("d", 24L * 60 * 60 * 1000);
        timeUnits.put("h", 60L * 60 * 1000);
        timeUnits.put("m", 60L * 1000);
        timeUnits.put("s", 1000L);
    }

    public static long parseToMillis(String input) {
        long totalMillis = 0;

        // Normalize input
        String normalized = input
                .toLowerCase()
                .replaceAll("days?|day\\(s\\)", "d")
                .replaceAll("weeks?|wks?", "w")
                .replaceAll("hours?|hrs?", "h")
                .replaceAll("minutes?|mins?|min", "m")
                .replaceAll("seconds?|secs?|sec", "s")
                .replaceAll("and", "")
                .replaceAll("\\s+", " ")
                .trim();

        Pattern pattern = Pattern.compile("(\\d+)\\s*(y|w|d|h|m|s)");
        Matcher matcher = pattern.matcher(normalized);

        while (matcher.find()) {
            long value = Long.parseLong(matcher.group(1));
            String unit = matcher.group(2);
            Long multiplier = timeUnits.get(unit);
            if (multiplier != null) {
                totalMillis += value * multiplier;
            }
        }

        return totalMillis;
    }
}
