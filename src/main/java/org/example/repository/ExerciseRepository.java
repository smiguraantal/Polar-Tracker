package org.example.repository;

import org.example.domain.Exercise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.OptionalDouble;

@Repository
public interface ExerciseRepository extends JpaRepository <Exercise, Long>{

    @Query("SELECT e.exerciseId FROM Exercise e")
    List<String> findAllExerciseIds();

    @Query("SELECT SUM(e.duration) FROM Exercise e")
    Long sumDuration();

    @Query("SELECT SUM(e.duration) FROM Exercise e WHERE e.sport = :sport")
    long sumDurationBySport(@Param("sport") String sport);

    @Query("SELECT e.sport, SUM(e.duration) FROM Exercise e GROUP BY e.sport")
    List<Object[]> sumDurationGroupedBySport();

    @Query("SELECT e.sport, AVG(e.duration) FROM Exercise e GROUP BY e.sport")
    List<Object[]> avgDurationGroupedBySport();

    @Query("SELECT e FROM Exercise e ORDER BY YEAR(e.startTime), MONTH(e.startTime), DAY(e.startTime)")
    List<Exercise> findAllExercisesOrderedByDate();

}