package org.example.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExerciseSummaryResponse {
    private LocalDateTime date;
    private String sport;
    private String duration;
    private Double distance;
    private Integer averageHeartRate;

//    public long convertISOToMillis(String isoDuration) {
//        Duration duration = Duration.parse(isoDuration);
//        return duration.toMillis();
//    }
}