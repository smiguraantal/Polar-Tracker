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

@RestController
@RequestMapping("/api/polar")
public class ExerciseController {

    @Autowired
    private ExerciseService exerciseService;

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
        return exerciseService.getFormattedExerciseSummaries(); // A szolgáltatásban hívod meg a formattert
    }

    @GetMapping("/longest-distance")
    public List<ExerciseSummaryResponse> getLongestDistanceExercise() {
        return exerciseService.getLongestDistanceExercise();
    }

    @GetMapping("/highest-average-heart-rate")
    public List<ExerciseSummaryResponse> getHighestAverageHeartRateExercise() {
        return exerciseService.getHighestAverageHeartRateExercise();
    }

    @GetMapping("/total-duration")
    public String getTotalDuration() {
        return exerciseService.getTotalDuration();
    }
}