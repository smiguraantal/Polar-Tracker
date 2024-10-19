package org.example.util;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class DateFormatter {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public String formatDate(String date) {
        LocalDateTime dateTime = LocalDateTime.parse(date);
        return dateTime.format(DATE_FORMATTER);
    }
}