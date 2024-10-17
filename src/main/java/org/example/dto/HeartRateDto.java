package org.example.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HeartRateDto {

    @JsonProperty("average")
    private int average;

    @JsonProperty("maximum")
    private int maximum;
}