package org.example.util;

import org.example.dto.response.ExerciseSummaryResponse;
import org.example.dto.response.FormattedExerciseSummaryResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ExerciseSummaryFormatter {

    private final DateFormatter dateFormatter;
    private final DistanceFormatter distanceFormatter;
    private final HeartRateFormatter heartRateFormatter;
    private final DurationConverter durationConverter;

    @Autowired
    public ExerciseSummaryFormatter(DateFormatter dateFormatter, DistanceFormatter distanceFormatter, HeartRateFormatter heartRateFormatter, DurationConverter durationConverter) {
        this.dateFormatter = dateFormatter;
        this.distanceFormatter = distanceFormatter;
        this.heartRateFormatter = heartRateFormatter;
        this.durationConverter = durationConverter;
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
                .duration(durationConverter.millisToFormatted(response.getDuration()))
                .distance(distanceFormatter.formatDistance(response.getDistance()))
                .averageHeartRate(heartRateFormatter.formatHeartRate(response.getAverageHeartRate()))
                .build();
    }
}