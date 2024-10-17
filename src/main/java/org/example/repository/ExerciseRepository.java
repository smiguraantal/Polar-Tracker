package org.example.repository;

import org.example.domain.Exercise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExerciseRepository extends JpaRepository <Exercise, Long>{

    @Query("SELECT e.exerciseId FROM Exercise e")
    List<String> findAllExerciseIds();

    @Query("SELECT SUM(e.duration) FROM Exercise e")
    Long sumDuration();
}