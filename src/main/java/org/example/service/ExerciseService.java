package org.example.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.example.domain.sample.AltitudeSample;
import org.example.domain.sample.DistanceSample;
import org.example.domain.Exercise;
import org.example.domain.sample.HeartRateSample;
import org.example.domain.HeartRateZone;
import org.example.domain.RoutePoint;
import org.example.domain.sample.SpeedSample;
import org.example.domain.sample.StepCountSample;
import org.example.dto.ExerciseDto;
import org.example.dto.HeartRateDto;
import org.example.dto.HeartRateZoneDto;
import org.example.dto.RoutePointDto;
import org.example.dto.response.ExerciseSummaryResponse;
import org.example.dto.response.FormattedExerciseSummaryResponse;
import org.example.dto.sample.AltitudeSampleDto;
import org.example.dto.sample.DistanceSampleDto;
import org.example.dto.sample.HeartRateSampleDto;
import org.example.dto.sample.SampleDto;
import org.example.dto.sample.SpeedSampleDto;
import org.example.dto.sample.StepCountSampleDto;
import org.example.repository.ExerciseRepository;
import org.example.util.formatter.DistanceFormatter;
import org.example.util.converter.DurationConverter;
import org.example.util.formatter.ExerciseSummaryFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.stream.Collectors;

import static org.example.constant.SampleTypeConstants.ALTITUDE_SAMPLE;
import static org.example.constant.SampleTypeConstants.DISTANCE_SAMPLE;
import static org.example.constant.SampleTypeConstants.HEART_RATE_SAMPLE;
import static org.example.constant.SampleTypeConstants.SPEED_SAMPLE;
import static org.example.constant.SampleTypeConstants.STEP_COUNT_SAMPLE;

@Service
public class ExerciseService {

    private static final String EXERCISES_URL = "https://www.polaraccesslink.com/v3/exercises";

    private final RestTemplate restTemplate;

    private final GpxService gpxService;

    private final ExerciseRepository exerciseRepository;

    private final ExerciseSummaryFormatter formatter;

    private final DistanceFormatter distanceFormatter;

    private final DurationConverter durationConverter;

    private final ObjectMapper objectMapper;

    @Autowired
    public ExerciseService(RestTemplate restTemplate, GpxService gpxService, ExerciseRepository exerciseRepository, ExerciseSummaryFormatter formatter, DistanceFormatter distanceFormatter, DurationConverter durationConverter, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.gpxService = gpxService;
        this.exerciseRepository = exerciseRepository;
        this.formatter = formatter;
        this.distanceFormatter = distanceFormatter;
        this.durationConverter = durationConverter;
        this.objectMapper = objectMapper;
    }

    @Transactional
    public void fetchAndSaveExercises() {
        try {
            String responseBody = restTemplate.getForObject(EXERCISES_URL + "?zones=true&route=true&samples=true", String.class);
            if (responseBody == null) {
                throw new RuntimeException("API response is null");
            }

            List<ExerciseDto> exerciseDtos = objectMapper.readValue(responseBody, new TypeReference<>() {});

            List<String> existingExerciseIds = exerciseRepository.findAllExerciseIds();

            for (ExerciseDto exerciseDto : exerciseDtos) {
                if (existingExerciseIds.contains(exerciseDto.getExerciseId())) {
                    continue;
                }

                String url = "https://www.polaraccesslink.com/v3/exercises/" + exerciseDto.getExerciseId() + "?zones=true&route=true&samples=true";
                String exerciseDetailsJson = restTemplate.getForObject(url, String.class);

                if (exerciseDetailsJson == null) {
                    throw new RuntimeException("API response for detailed exercise data is null");
                }

                ExerciseDto detailedExerciseDto = objectMapper.readValue(exerciseDetailsJson, ExerciseDto.class);
                Exercise exercise = convertToExercise(detailedExerciseDto);

                List<HeartRateZone> heartRateZones = convertToHeartRateZones(detailedExerciseDto.getHeartRateZones(), exercise);
                List<RoutePoint> routePoints = convertToRoutePoints(detailedExerciseDto.getRoute(), exercise);

                List<SampleDto> sampleDtos = detailedExerciseDto.getSamples();

                List<HeartRateSample> heartRateSamples = convertToHeartRateSamples(sampleDtos, exercise);
                List<SpeedSample> speedSamples = convertToSpeedSamples(sampleDtos, exercise);
                List<StepCountSample> stepCountSamples = convertToStepCountSamples(sampleDtos, exercise);
                List<AltitudeSample> altitudeSamples = convertToAltitudeSamples(sampleDtos, exercise);
                List<DistanceSample> distanceSamples = convertToDistanceSamples(sampleDtos, exercise);

                exercise.setHeartRateZones(heartRateZones);
                exercise.setRoutePoints(routePoints);
                exercise.setHeartRateSamples(heartRateSamples);
                exercise.setSpeedSamples(speedSamples);
                exercise.setStepCountSamples(stepCountSamples);
                exercise.setAltitudeSamples(altitudeSamples);
                exercise.setDistanceSamples(distanceSamples);

                exerciseRepository.save(exercise);

                gpxService.fetchAndSaveGpxData(exercise);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to fetch and save exercises", e);
        }
    }

    public ExerciseDto findById(Long id) {
        Exercise exercise = exerciseRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Exercise not found"));
        return convertToExerciseDto(exercise);
    }

    @Transactional
    public void deleteExercise(Long id) {
        Exercise exercise = exerciseRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Exercise not found"));
        exerciseRepository.delete(exercise);
    }


    public List<ExerciseSummaryResponse> getExerciseSummaries() {
        List<Exercise> exercises = exerciseRepository.findAll();
        List<ExerciseSummaryResponse> summaries = new ArrayList<>();

        for (Exercise exercise : exercises) {
            ExerciseSummaryResponse summary = convertToExerciseSummaryResponse(exercise);
            summaries.add(summary);
        }

        return summaries;
    }

    public List<FormattedExerciseSummaryResponse> getFormattedExerciseSummaries() {
        List<ExerciseSummaryResponse> exerciseSummaries = getExerciseSummaries();
        return formatter.formatSummaries(exerciseSummaries);
    }

    public List<ExerciseSummaryResponse> getLongestDistanceExercise() {
        OptionalDouble longDistance = getExerciseSummaries().stream()
                .mapToDouble(ExerciseSummaryResponse::getDistance)
                .max();

        return longDistance.isPresent() ? getExerciseSummaries().stream()
                .filter(exercise -> exercise.getDistance() == longDistance.getAsDouble())
                .collect(Collectors.toList()) : Collections.emptyList();
    }

    public List<ExerciseSummaryResponse> getHighestAverageHeartRateExercise() {
        OptionalInt maxHeartRate = getExerciseSummaries().stream()
                .mapToInt(ExerciseSummaryResponse::getAverageHeartRate)
                .max();

        return maxHeartRate.isPresent() ? getExerciseSummaries().stream()
                .filter(exercise -> exercise.getAverageHeartRate() == maxHeartRate.getAsInt())
                .collect(Collectors.toList()) : Collections.emptyList();
    }

    public String getTotalDuration() {
        return durationConverter.formatDuration(exerciseRepository.sumDuration());
    }

    public String getTotalDurationBySport(String sport) {
        return durationConverter.formatDuration(exerciseRepository.sumDurationBySport(sport));
    }

    public Map<String, String> getTotalDurationGroupedBySport() {
        List<Object[]> results = exerciseRepository.sumDurationGroupedBySport();
        Map<String, String> durationsBySport = new HashMap<>();

        for (Object[] result : results) {
            String sport = (String) result[0];
            long durationMillis = (long) result[1];
            String formattedDuration = durationConverter.formatDuration(durationMillis);
            durationsBySport.put(sport, formattedDuration);
        }

        return durationsBySport;
    }

    public Map<String, String> avgDurationGroupedBySport() {
        List<Object[]> results = exerciseRepository.avgDurationGroupedBySport();
        Map<String, String> avgDurations = new HashMap<>();

        for (Object[] result : results) {
            String sport = (String) result[0];
            long avgDurationMillis = ((Number) result[1]).longValue();
            String formattedDuration = durationConverter.formatDuration(avgDurationMillis);
            avgDurations.put(sport, formattedDuration);
        }

        return avgDurations;
    }

    public Map<Integer, Map<String, List<ExerciseSummaryResponse>>> getExercisesGroupedByYearAndMonth() {
        List<Exercise> exercises = exerciseRepository.findAllExercisesOrderedByDate();

        List<ExerciseSummaryResponse> exerciseSummaries = exercises.stream()
                .map(this::convertToExerciseSummaryResponse)
                .toList();

        return exerciseSummaries.stream()
                .collect(Collectors.groupingBy(
                        exercise -> exercise.getDate().getYear(),
                        Collectors.groupingBy(
                                exercise -> exercise.getDate().getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH),
                                LinkedHashMap::new,
                                Collectors.toList()
                        )
                ));
    }

    public Map<Integer, Map<String, List<FormattedExerciseSummaryResponse>>> getFormattedExercisesGroupedByYearAndMonth() {

        Map<Integer, Map<String, List<ExerciseSummaryResponse>>> exercisesGroupedByYearAndMonth = getExercisesGroupedByYearAndMonth();

        return exercisesGroupedByYearAndMonth.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        yearEntry -> yearEntry.getValue().entrySet().stream()
                                .collect(Collectors.toMap(
                                        Map.Entry::getKey,
                                        monthEntry -> formatter.formatSummaries(monthEntry.getValue()),
                                        (oldValue, newValue) -> oldValue,
                                        LinkedHashMap::new
                                )),
                        (oldValue, newValue) -> oldValue,
                        LinkedHashMap::new
                ));
    }

    public Map<Integer, Map<String, Map<String, Double>>> getTotalDistanceGroupedBySportYearAndMonth() {
        List<Exercise> exercises = exerciseRepository.findAllExercisesOrderedByDate();

        return exercises.stream()
                .collect(Collectors.groupingBy(
                        exercise -> exercise.getStartTime().getYear(),
                        Collectors.groupingBy(
                                exercise -> exercise.getStartTime().getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH),
                                Collectors.groupingBy(
                                        Exercise::getSport,
                                        Collectors.summingDouble(Exercise::getDistance)
                                )
                        )
                ));
    }

    public Map<Integer, Map<String, Map<String, String>>> getFormattedTotalDistanceGroupedBySportYearAndMonth() {
        Map<Integer, Map<String, Map<String, Double>>> rawDistanceData = getTotalDistanceGroupedBySportYearAndMonth();

        return rawDistanceData.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        yearEntry -> yearEntry.getValue().entrySet().stream()
                                .collect(Collectors.toMap(
                                        Map.Entry::getKey,
                                        monthEntry -> monthEntry.getValue().entrySet().stream()
                                                .collect(Collectors.toMap(
                                                        Map.Entry::getKey,
                                                        sportEntry -> distanceFormatter.formatDistance(sportEntry.getValue())
                                                ))
                                ))
                ));
    }





    private Exercise convertToExercise(ExerciseDto dto) {
        return Exercise.builder()
                .exerciseId(dto.getExerciseId())
                .uploadTime(dto.getUploadTime())
                .polarUser(dto.getPolarUser())
                .device(dto.getDevice())
                .deviceId(dto.getDeviceId())
                .startTime(dto.getStartTime())
                .startTimeUtcOffset(dto.getStartTimeUtcOffset())
                .duration(durationConverter.isoToMillis(dto.getDuration()))
                .distance(dto.getDistance())
                .averageHeartRate(dto.getHeartRate().getAverage())
                .maxHeartRate(dto.getHeartRate().getMaximum())
                .trainingLoad(dto.getTrainingLoad())
                .sport(dto.getSport())
                .hasRoute(dto.getHasRoute())
                .detailedSportInfo(dto.getDetailedSportInfo())
                .calories(dto.getCalories())
                .runningIndex(dto.getRunningIndex())
                .build();
    }

    private ExerciseDto convertToExerciseDto(Exercise exercise) {
        return ExerciseDto.builder()
                .exerciseId(exercise.getExerciseId())
                .uploadTime(exercise.getUploadTime())
                .polarUser(exercise.getPolarUser())
                .device(exercise.getDevice())
                .deviceId(exercise.getDeviceId())
                .startTime(exercise.getStartTime())
                .startTimeUtcOffset(exercise.getStartTimeUtcOffset())
                .duration(durationConverter.millisToIso(exercise.getDuration()))
                .distance(exercise.getDistance())
                .heartRate(convertToHeartRateDto(exercise.getAverageHeartRate(), exercise.getMaxHeartRate()))
                .trainingLoad(exercise.getTrainingLoad())
                .sport(exercise.getSport())
                .hasRoute(exercise.getHasRoute())
                .detailedSportInfo(exercise.getDetailedSportInfo())
                .calories(exercise.getCalories())
                .runningIndex(exercise.getRunningIndex())
                .heartRateZones(convertToHeartRateZoneDtos(exercise.getHeartRateZones()))
                .route(convertToRoutePointDtos(exercise.getRoutePoints()))
                .heartRateSamples(convertToHeartRateSampleDtos(exercise.getHeartRateSamples()))
                .speedSamples(convertToSpeedSampleDtos(exercise.getSpeedSamples()))
                .stepCountSamples(convertToStepCountSampleDtos(exercise.getStepCountSamples()))
                .altitudeSamples(convertToAltitudeSampleDtos(exercise.getAltitudeSamples()))
                .distanceSamples(convertToDistanceSampleDtos(exercise.getDistanceSamples()))
                .build();
    }

    private ExerciseSummaryResponse convertToExerciseSummaryResponse(Exercise exercise) {
        return ExerciseSummaryResponse.builder()
                .date(exercise.getStartTime())
                .sport(exercise.getSport())
                .duration(durationConverter.millisToIso(exercise.getDuration()))
                .distance(exercise.getDistance())
                .averageHeartRate(exercise.getAverageHeartRate())
                .build();
    }


    private List<HeartRateZone> convertToHeartRateZones(List<HeartRateZoneDto> dtos, Exercise exercise) {
        return dtos.stream()
                .map(dto -> HeartRateZone.builder()
                        .index(dto.getIndex())
                        .lowerLimit(dto.getLowerLimit())
                        .upperLimit(dto.getUpperLimit())
                        .inZone(durationConverter.isoToMillis(dto.getInZone()))
                        .exercise(exercise)
                        .build())
                .collect(Collectors.toList());
    }

    private List<HeartRateZoneDto> convertToHeartRateZoneDtos(List<HeartRateZone> heartRateZones) {
        return heartRateZones.stream()
                .map(zone -> HeartRateZoneDto.builder()
                        .index(zone.getIndex())
                        .lowerLimit(zone.getLowerLimit())
                        .upperLimit(zone.getUpperLimit())
                        .inZone(durationConverter.millisToIso(zone.getInZone()))
                        .build())
                .collect(Collectors.toList());
    }


    private List<RoutePoint> convertToRoutePoints(List<RoutePointDto> dtoList, Exercise exercise) {
        return dtoList.stream()
                .map(dto -> RoutePoint.builder()
                        .exercise(exercise)
                        .latitude(dto.getLatitude())
                        .longitude(dto.getLongitude())
                        .time(durationConverter.isoToMillis(dto.getTime()))
                        .satellites(dto.getSatellites())
                        .fix(dto.getFix())
                        .build())
                .collect(Collectors.toList());
    }

    private List<RoutePointDto> convertToRoutePointDtos(List<RoutePoint> routePoints) {
        return routePoints.stream()
                .map(point -> RoutePointDto.builder()
                        .latitude(point.getLatitude())
                        .longitude(point.getLongitude())
                        .time(durationConverter.millisToIso(point.getTime()))
                        .satellites(point.getSatellites())
                        .fix(point.getFix())
                        .build())
                .collect(Collectors.toList());
    }

    private HeartRateDto convertToHeartRateDto(int average, int maximum) {
        HeartRateDto heartRateDto = new HeartRateDto();
        heartRateDto.setAverage(average);
        heartRateDto.setMaximum(maximum);
        return heartRateDto;
    }




    private List<HeartRateSample> convertToHeartRateSamples(List<SampleDto> sampleDtos, Exercise exercise) {
        return sampleDtos.stream()
                .filter(sampleDto -> sampleDto.getSampleType() == HEART_RATE_SAMPLE)
                .flatMap(sampleDto -> Arrays.stream(sampleDto.getData().split(","))
                        .map(heartRate -> HeartRateSample.builder()
                                .exercise(exercise)
                                .heartRateValue(Integer.parseInt(heartRate))
                                .recordingRate(sampleDto.getRecordingRate())
                                .build()))
                .collect(Collectors.toList());
    }

    private List<SpeedSample> convertToSpeedSamples(List<SampleDto> sampleDtos, Exercise exercise) {
        return sampleDtos.stream()
                .filter(sampleDto -> sampleDto.getSampleType() == SPEED_SAMPLE)
                .flatMap(sampleDto -> Arrays.stream(sampleDto.getData().split(","))
                        .map(speed -> SpeedSample.builder()
                                .exercise(exercise)
                                .speedValue(Double.parseDouble(speed))
                                .recordingRate(sampleDto.getRecordingRate())
                                .build()))
                .collect(Collectors.toList());
    }

    private List<StepCountSample> convertToStepCountSamples(List<SampleDto> sampleDtos, Exercise exercise) {
        return sampleDtos.stream()
                .filter(sampleDto -> sampleDto.getSampleType() == STEP_COUNT_SAMPLE)
                .flatMap(sampleDto -> Arrays.stream(sampleDto.getData().split(","))
                        .map(stepCount -> StepCountSample.builder()
                                .exercise(exercise)
                                .stepCount(Integer.parseInt(stepCount))
                                .recordingRate(sampleDto.getRecordingRate())
                                .build()))
                .collect(Collectors.toList());
    }

    private List<AltitudeSample> convertToAltitudeSamples(List<SampleDto> sampleDtos, Exercise exercise) {
        return sampleDtos.stream()
                .filter(sampleDto -> sampleDto.getSampleType() == ALTITUDE_SAMPLE)
                .flatMap(sampleDto -> Arrays.stream(sampleDto.getData().split(","))
                        .map(altitude -> AltitudeSample.builder()
                                .exercise(exercise)
                                .altitudeValue(Double.parseDouble(altitude))
                                .recordingRate(sampleDto.getRecordingRate())
                                .build()))
                .collect(Collectors.toList());
    }

    private List<DistanceSample> convertToDistanceSamples(List<SampleDto> sampleDtos, Exercise exercise) {
        return sampleDtos.stream()
                .filter(sampleDto -> sampleDto.getSampleType() == DISTANCE_SAMPLE)
                .flatMap(sampleDto -> Arrays.stream(sampleDto.getData().split(","))
                        .map(distance -> DistanceSample.builder()
                                .exercise(exercise)
                                .distanceValue(Double.parseDouble(distance))
                                .recordingRate(sampleDto.getRecordingRate())
                                .build()))
                .collect(Collectors.toList());
    }







    private List<HeartRateSampleDto> convertToHeartRateSampleDtos(List<HeartRateSample> samples) {
        return samples.stream()
                .map(sample -> HeartRateSampleDto.builder()
                        .recordingRate(sample.getRecordingRate())
                        .heartRateValue(sample.getHeartRateValue())
                        .build())
                .collect(Collectors.toList());
    }

    private List<SpeedSampleDto> convertToSpeedSampleDtos(List<SpeedSample> speedSamples) {
        return speedSamples.stream()
                .map(sample -> SpeedSampleDto.builder()
                        .recordingRate(sample.getRecordingRate())
                        .speedValue(sample.getSpeedValue())
                        .build())
                .collect(Collectors.toList());
    }

    private List<StepCountSampleDto> convertToStepCountSampleDtos(List<StepCountSample> stepCountSamples) {
        return stepCountSamples.stream()
                .map(sample -> StepCountSampleDto.builder()
                        .recordingRate(sample.getRecordingRate())
                        .stepCount(sample.getStepCount())
                        .build())
                .collect(Collectors.toList());
    }

    private List<AltitudeSampleDto> convertToAltitudeSampleDtos(List<AltitudeSample> altitudeSamples) {
        return altitudeSamples.stream()
                .map(sample -> AltitudeSampleDto.builder()
                        .recordingRate(sample.getRecordingRate())
                        .altitudeValue(sample.getAltitudeValue())
                        .build())
                .collect(Collectors.toList());
    }

    private List<DistanceSampleDto> convertToDistanceSampleDtos(List<DistanceSample> distanceSamples) {
        return distanceSamples.stream()
                .map(sample -> DistanceSampleDto.builder()
                        .recordingRate(sample.getRecordingRate())
                        .distanceValue(sample.getDistanceValue())
                        .build())
                .collect(Collectors.toList());
    }

}