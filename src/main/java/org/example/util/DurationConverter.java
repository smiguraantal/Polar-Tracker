package org.example.util;

import java.time.Duration;

import org.springframework.stereotype.Component;

@Component
public class DurationConverter {

    public long isoToMillis(String isoDuration) {
        Duration duration = Duration.parse(isoDuration);
        return duration.toMillis();
    }

    public String millisToIso(long durationMillis) {
        Duration duration = Duration.ofMillis(durationMillis);
        return duration.toString();
    }

    public String millisToFormatted(String isoDuration) {
        Duration duration = Duration.parse(isoDuration);

        long hours = duration.toHours();
        long minutes = duration.toMinutes() % 60;
        long seconds = duration.getSeconds() % 60;

        return formatTimeComponents(hours, minutes, seconds);
    }

    public String millisToFormatted(long durationMillis) {
        long hours = durationMillis / 1000 / 3600;
        long minutes = (durationMillis / 1000 / 60) % 60;
        long seconds = (durationMillis / 1000) % 60;

        return formatTimeComponents(hours, minutes, seconds);
    }

    public String formatDuration(long durationMillis) {
        long hours = durationMillis / 1000 / 3600;
        long minutes = (durationMillis / 1000 / 60) % 60;
        long seconds = (durationMillis / 1000) % 60;

        return formatTimeComponents(hours, minutes, seconds);
    }

    private String formatTimeComponents(long hours, long minutes, long seconds) {
        StringBuilder formattedDuration = new StringBuilder();

        if (hours > 0) {
            formattedDuration.append(hours).append(hours == 1 ? " hour " : " hours ");
        }
        if (minutes > 0) {
            formattedDuration.append(minutes).append(minutes == 1 ? " minute " : " minutes ");
        }
        if (seconds > 0 || formattedDuration.isEmpty()) {
            formattedDuration.append(seconds).append(seconds == 1 ? " second" : " seconds");
        }

        return formattedDuration.toString().trim();
    }
}