package org.example.util.formatter;

import org.springframework.stereotype.Component;

@Component
public class HeartRateFormatter {

    public String formatHeartRate(int heartRate) {
        return heartRate + " bpm";
    }
}