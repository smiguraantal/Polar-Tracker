package org.example.dto.sample;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class SampleDto {

    @JsonProperty("recording_rate")
    private int recordingRate;

    @JsonProperty("sample_type")
    private int sampleType;

    @JsonProperty("data")
    private String data;
}