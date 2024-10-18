package org.example.util;

import org.example.dto.response.ExerciseSummaryResponse;
import org.example.dto.response.FormattedExerciseSummaryResponse;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ExerciseSummaryFormatter {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public List<FormattedExerciseSummaryResponse> formatSummaries(List<ExerciseSummaryResponse> responses) {
        return responses.stream()
                .map(this::formatSummary)
                .collect(Collectors.toList());
    }

    private FormattedExerciseSummaryResponse formatSummary(ExerciseSummaryResponse response) {
        return FormattedExerciseSummaryResponse.builder()
                .date(formatDate(response.getDate().toString()))
                .sport(response.getSport())
//                .duration(formatDuration(response.convertISOToMillis(response.getDuration())))  // Long duration millis
                .duration(DurationConverter.millisToFormatted(response.getDuration()))
                .distance(formatDistance(response.getDistance()))
                .averageHeartRate(formatHeartRate(response.getAverageHeartRate()))
                .build();
    }

    private String formatDate(String date) {
        LocalDateTime dateTime = LocalDateTime.parse(date);
        return dateTime.format(DATE_FORMATTER);
    }

    private String formatDuration(long durationMillis) {
        long hours = durationMillis / 1000 / 60 / 60;
        long minutes = (durationMillis / 1000 / 60) % 60;
        long seconds = (durationMillis / 1000) % 60;

        StringBuilder result = new StringBuilder();

        if (hours > 0) {
            result.append(hours).append(hours == 1 ? " hour " : " hours ");
        }

        if (minutes > 0) {
            result.append(minutes).append(minutes == 1 ? " minute " : " minutes ");
        }

        result.append(seconds).append(seconds == 1 ? " second" : " seconds");

        return result.toString();
    }

    private String formatDistance(double distance) {
        return String.format("%.2f km", distance / 1000);
    }

    private String formatHeartRate(int heartRate) {
        return heartRate + " bpm";
    }
}
