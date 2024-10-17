package org.example.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RoutePointDto {

    @JsonProperty("latitude")
    private Double latitude;

    @JsonProperty("longitude")
    private Double longitude;

    @JsonProperty("time")
    private String time;

    @JsonProperty("satellites")
    private Integer satellites;

    @JsonProperty("fix")
    private Integer fix;
}