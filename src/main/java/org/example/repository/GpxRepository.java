package org.example.repository;

import org.example.domain.Gpx;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GpxRepository extends JpaRepository<Gpx, Long> {

    Optional<Gpx> findByExerciseId(@Param("exerciseId") Long exerciseId);

}