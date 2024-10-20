package org.example.controller;

import org.example.dto.ExerciseDto;
import org.example.dto.response.ExerciseSummaryResponse;
import org.example.dto.response.FormattedExerciseSummaryResponse;
import org.example.service.ExerciseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/polar")
public class ExerciseController {

    @Autowired
    private ExerciseService exerciseService;

    @GetMapping("/exercises/save-exercises")
    public void saveExercises() {
        exerciseService.fetchAndSaveExercises();
    }

    @GetMapping("/exercises/{id}")
    public ExerciseDto getExerciseById(@PathVariable("id") Long id) {
        return exerciseService.findById(id);
    }

    @GetMapping("/exercises/exercise-summaries")
    public List<ExerciseSummaryResponse> getExerciseSummaries() {
        return exerciseService.getExerciseSummaries();
    }

    @GetMapping("/exercises/formatted-exercise-summaries")
    public List<FormattedExerciseSummaryResponse> getFormattedExerciseSummaries() {
        return exerciseService.getFormattedExerciseSummaries();
    }

    @GetMapping("/exercises/longest-distance")
    public List<ExerciseSummaryResponse> getLongestDistanceExercise() {
        return exerciseService.getLongestDistanceExercise();
    }

    @GetMapping("/exercises/highest-average-heart-rate")
    public List<ExerciseSummaryResponse> getHighestAverageHeartRateExercise() {
        return exerciseService.getHighestAverageHeartRateExercise();
    }

    @GetMapping("/exercises/total-duration")
    public String getTotalDuration() {
        return exerciseService.getTotalDuration();
    }

    @GetMapping("/exercises/total-duration/sport/{sport}")
    public String getTotalDurationBySport(@PathVariable("sport") String sport) {
        return exerciseService.getTotalDurationBySport(sport);
    }

    @GetMapping("/exercises/total-duration/grouped")
    public Map<String, String> getTotalDurationGroupedBySport() {
        return exerciseService.getTotalDurationGroupedBySport();
    }

    @GetMapping("/exercises/average-duration/grouped")
    public Map<String, String> getAverageDurationBySport() {
        return exerciseService.avgDurationGroupedBySport();
    }

    @GetMapping("/exercises/grouped-by-year-month")
    public Map<Integer, Map<String, List<ExerciseSummaryResponse>>> getExercisesGroupedByYearAndMonth() {
        return exerciseService.getExercisesGroupedByYearAndMonth();
    }

    @GetMapping("/exercises/formatted-grouped-by-year-month")
    public Map<Integer, Map<String, List<FormattedExerciseSummaryResponse>>> getFormattedExercisesGroupedByYearAndMonth() {
        return exerciseService.getFormattedExercisesGroupedByYearAndMonth();
    }

    @GetMapping("/exercises/total-distance-by-sport-year-month")
    public Map<Integer, Map<String, Map<String, Double>>> getTotalDistanceGroupedBySportYearAndMonth() {
        return exerciseService.getTotalDistanceGroupedBySportYearAndMonth();
    }

    @GetMapping("/exercises/formatted-total-distance-by-sport-year-month")
    public Map<Integer, Map<String, Map<String, String>>> getFormattedTotalDistanceGroupedBySportYearAndMonth() {
        return exerciseService.getFormattedTotalDistanceGroupedBySportYearAndMonth();
    }
}