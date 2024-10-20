package org.example.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
}