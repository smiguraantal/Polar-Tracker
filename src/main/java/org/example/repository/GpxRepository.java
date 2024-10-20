package org.example.repository;

import org.example.domain.Gpx;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GpxRepository extends JpaRepository<Gpx, Long> {

    Optional<Gpx> findByExerciseId(String exerciseId);
}
