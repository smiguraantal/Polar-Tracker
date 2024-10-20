package org.example.service;

import org.example.domain.Gpx;
import org.example.repository.GpxRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;


@Service
public class GpxService {

    private final RestTemplate restTemplate;

    private final GpxRepository gpxRepository;

    public GpxService(RestTemplate restTemplate, GpxRepository gpxRepository) {
        this.restTemplate = restTemplate;
        this.gpxRepository = gpxRepository;
    }

    @Transactional
    public void fetchAndSaveGpxData(String exerciseId) {
        String url = "https://www.polaraccesslink.com/v3/exercises/" + exerciseId + "/gpx";
        String gpxData = restTemplate.getForObject(url, String.class);

        if (gpxData == null || gpxData.isEmpty()) {
            throw new RuntimeException("Failed to retrieve GPX data for exercise: " + exerciseId);
        }

        Gpx gpx = Gpx.builder()
                .exerciseId(exerciseId)
                .gpxData(gpxData)
                .build();

        gpxRepository.save(gpx);
    }
}