package org.example.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.domain.sample.AltitudeSample;
import org.example.domain.sample.DistanceSample;
import org.example.domain.sample.HeartRateSample;
import org.example.domain.sample.SpeedSample;
import org.example.domain.sample.StepCountSample;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "exercise")
public class Exercise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "exercise_id", nullable = false, unique = true)
    private String exerciseId;

    @Column(name = "upload_time", nullable = false)
    private LocalDateTime uploadTime;

    @Column(name = "polar_user", nullable = false)
    private String polarUser;

    @Column(name = "device", nullable = false)
    private String device;

    @Column(name = "device_id", nullable = false)
    private String deviceId;

    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;

    @Column(name = "start_time_utc_offset", nullable = false)
    private Integer startTimeUtcOffset;

    @Column(name = "duration", nullable = false)
    private Long duration;

    @Column(name = "distance", nullable = false)
    private Double distance;

    @Column(name = "average_heart_rate", nullable = false)
    private Integer averageHeartRate;

    @Column(name = "max_heart_rate", nullable = false)
    private Integer maxHeartRate;

    @Column(name = "training_load", nullable = false)
    private Double trainingLoad;

    @Column(name = "sport", nullable = false)
    private String sport;

    @Column(name = "has_route", nullable = false)
    private Boolean hasRoute;

    @Column(name = "detailed_sport_info", nullable = false)
    private String detailedSportInfo;

    @Column(name = "calories", nullable = false)
    private Integer calories;

    @Column(name = "running_index")
    private Integer runningIndex;

    @OneToMany(mappedBy = "exercise", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<HeartRateZone> heartRateZones;

    @OneToMany(mappedBy = "exercise", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RoutePoint> routePoints;

    @OneToMany(mappedBy = "exercise", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<HeartRateSample> heartRateSamples;

    @OneToMany(mappedBy = "exercise", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SpeedSample> speedSamples;

    @OneToMany(mappedBy = "exercise", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StepCountSample> stepCountSamples;

    @OneToMany(mappedBy = "exercise", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AltitudeSample> altitudeSamples;

    @OneToMany(mappedBy = "exercise", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DistanceSample> distanceSamples;

    @OneToMany(mappedBy = "exercise", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Gpx> gpxData;
}