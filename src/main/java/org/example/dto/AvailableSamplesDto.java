package org.example.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class AvailableSamplesDto {

    @JsonProperty("samples")
    private List<String> samples;
}