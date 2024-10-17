package org.example.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FormattedExerciseSummaryResponse {

    private String date;
    private String sport;
    private String duration;
    private String distance;
    private String averageHeartRate;
}