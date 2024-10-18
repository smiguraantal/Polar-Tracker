package org.example.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HeartRateZoneDto {

    @JsonProperty("index")
    private Integer index;

    @JsonProperty("lower_limit")
    private Integer lowerLimit;

    @JsonProperty("upper_limit")
    private Integer upperLimit;

    @JsonProperty("in_zone")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private String inZone;
}