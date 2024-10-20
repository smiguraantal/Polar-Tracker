package org.example.dto.sample;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StepCountSampleDto {

    @JsonProperty("step_count")
    private Integer stepCount;

    @JsonProperty("recording_rate")
    private Integer recordingRate;
}