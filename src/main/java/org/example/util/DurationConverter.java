package org.example.util;

import java.time.Duration;

public class DurationConverter {

    public static long isoToMillis(String isoDuration) {
        Duration duration = Duration.parse(isoDuration);
        return duration.toMillis();
    }

    public static String millisToIso(long durationMillis) {
        Duration duration = Duration.ofMillis(durationMillis);
        return duration.toString();
    }

    public static String millisToFormatted(String isoDuration) {
        Duration duration = Duration.parse(isoDuration);

        long hours = duration.toHours();
        long minutes = duration.toMinutes() % 60;
        long seconds = duration.getSeconds() % 60;

        return String.format("%d hours %d minutes %d seconds", hours, minutes, seconds);
    }
}