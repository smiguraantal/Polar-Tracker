package org.example.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class PolarService {

    private final String polarAccessToken;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Autowired
    public PolarService(String polarAccessToken, RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.polarAccessToken = polarAccessToken;
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }
}