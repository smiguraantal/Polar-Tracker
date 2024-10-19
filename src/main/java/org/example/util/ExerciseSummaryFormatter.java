package org.example.util;

import org.example.dto.response.ExerciseSummaryResponse;
import org.example.dto.response.FormattedExerciseSummaryResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ExerciseSummaryFormatter {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final DateFormatter dateFormatter;
    private final DistanceFormatter distanceFormatter;
    private final HeartRateFormatter heartRateFormatter;

    @Autowired
    public ExerciseSummaryFormatter(DateFormatter dateFormatter, DistanceFormatter distanceFormatter, HeartRateFormatter heartRateFormatter) {
        this.dateFormatter = dateFormatter;
        this.distanceFormatter = distanceFormatter;
        this.heartRateFormatter = heartRateFormatter;
    }

    public List<FormattedExerciseSummaryResponse> formatSummaries(List<ExerciseSummaryResponse> responses) {
        return responses.stream()
                .map(this::formatSummary)
                .collect(Collectors.toList());
    }

    public FormattedExerciseSummaryResponse formatSummary(ExerciseSummaryResponse response) {
        return FormattedExerciseSummaryResponse.builder()
                .date(dateFormatter.formatDate(response.getDate().toString()))
                .sport(response.getSport())
                .duration(DurationConverter.millisToFormatted(response.getDuration()))
                .distance(distanceFormatter.formatDistance(response.getDistance()))
                .averageHeartRate(heartRateFormatter.formatHeartRate(response.getAverageHeartRate()))
                .build();
    }
}