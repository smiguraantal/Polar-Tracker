package org.example.controller;

import org.example.dto.ExerciseDto;
import org.example.dto.response.ExerciseSummaryResponse;
import org.example.dto.response.FormattedExerciseSummaryResponse;
import org.example.service.ExerciseService;
import org.example.util.ExerciseSummaryFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/polar")
public class ExerciseController {

    @Autowired
    private ExerciseService exerciseService;

    @Autowired
    private ExerciseSummaryFormatter formatter;

    @GetMapping("/exercises/{id}")
    public ExerciseDto getExerciseById(@PathVariable("id") Long id) {
        return exerciseService.findById(id);
    }

    @GetMapping("/save-exercises")
    public void saveExercises() {
        exerciseService.fetchAndSaveExercises();
    }

    @GetMapping("/exercise-summaries")
    public List<ExerciseSummaryResponse> getExerciseSummaries() {
        return exerciseService.getExerciseSummaries();
    }

    @GetMapping("/formatted-exercise-summaries")
    public List<FormattedExerciseSummaryResponse> getFormattedExerciseSummaries() {
        return formatter.formatSummaries(exerciseService.getExerciseSummaries());
    }

    @GetMapping("/longest-distance")
    public List<ExerciseSummaryResponse> getLongestDistanceExercise() {
        List<ExerciseSummaryResponse> exercises = exerciseService.getLongestDistanceExercise();

        return exercises.isEmpty() ? Collections.emptyList() : exercises;
    }

    @GetMapping("/highest-average-heart-rate")
    public List<ExerciseSummaryResponse> getHighestAverageHeartRateExercise() {
        List<ExerciseSummaryResponse> exercises = exerciseService.getHighestAverageHeartRateExercise();

        return exercises.isEmpty() ? Collections.emptyList() : exercises;
    }

    @GetMapping("/total-duration")
    public Long getTotalDuration() {
        return exerciseService.getTotalDuration();
    }
}