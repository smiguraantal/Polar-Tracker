package org.example.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.dto.sample.AltitudeSampleDto;
import org.example.dto.sample.DistanceSampleDto;
import org.example.dto.sample.HeartRateSampleDto;
import org.example.dto.sample.SampleDto;
import org.example.dto.sample.SpeedSampleDto;
import org.example.dto.sample.StepCountSampleDto;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExerciseDto {

    @JsonProperty("id")
    private String exerciseId;

    @JsonProperty("upload_time")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    private LocalDateTime uploadTime;

    @JsonProperty("polar_user")
    private String polarUser;

    @JsonProperty("device")
    private String device;

    @JsonProperty("device_id")
    private String deviceId;

    @JsonProperty("start_time")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime startTime;

    @JsonProperty("start_time_utc_offset")
    private Integer startTimeUtcOffset;

    @JsonProperty("duration")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private String duration;

    @JsonProperty("distance")
    private Double distance;

    @JsonProperty("heart_rate")
    private HeartRateDto heartRate;

    @JsonProperty("training_load")
    private Double trainingLoad;

    @JsonProperty("sport")
    private String sport;

    @JsonProperty("has_route")
    private Boolean hasRoute;

    @JsonProperty("detailed_sport_info")
    private String detailedSportInfo;

    @JsonProperty("calories")
    private Integer calories;

    @JsonProperty("running_index")
    private Integer runningIndex;

    @JsonProperty("heart_rate_zones")
    private List<HeartRateZoneDto> heartRateZones;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("route")
    private List<RoutePointDto> route;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("samples")
    private List<SampleDto> samples;

    @JsonProperty("heart_rate_samples")
    private List<HeartRateSampleDto> heartRateSamples;

    @JsonProperty("speed_samples")
    private List<SpeedSampleDto> speedSamples;

    @JsonProperty("step_count_samples")
    private List<StepCountSampleDto> stepCountSamples;

    @JsonProperty("altitude_samples")
    private List<AltitudeSampleDto> altitudeSamples;

    @JsonProperty("distance_samples")
    private List<DistanceSampleDto> distanceSamples;
}