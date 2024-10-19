package org.example.util;

import org.springframework.stereotype.Component;

@Component
public class DistanceFormatter {

    public String formatDistance(double distance) {
        return String.format("%.2f km", distance / 1000);
    }
}