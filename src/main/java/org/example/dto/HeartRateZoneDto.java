package org.example.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.time.Duration;

@Data
@Builder
public class HeartRateZoneDto {

    @JsonProperty("index")
    private Integer index;

    @JsonProperty("lower_limit")
    private Integer lowerLimit;

    @JsonProperty("upper_limit")
    private Integer upperLimit;

    @JsonProperty("in_zone")
    private Duration inZone;
}