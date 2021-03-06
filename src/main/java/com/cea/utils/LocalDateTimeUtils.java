package com.cea.utils;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

@Data
@Component
public class LocalDateTimeUtils {
    private String ZONE_TIME = "America/Sao_Paulo";

    public LocalDateTime dateNow() {
        LocalDateTime dateNow = LocalDateTime.now(ZoneId.of(this.ZONE_TIME));
        return dateNow;
    }

    public LocalDateTime addMinutes(LocalDateTime date, long minutes) {
        LocalDateTime newDateTime = LocalDateTime
                .from(date.plus(Duration.of(minutes, ChronoUnit.MINUTES)).atZone(ZoneId.of(this.ZONE_TIME)));

        return newDateTime;
    }

    public boolean validateDateTime(LocalDateTime date, LocalDateTime dateNow) {
        return dateNow.isBefore(date);
    }

    public LocalDateTime convertStringToDate(String dateStr) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDateTime dateTime = LocalDate.parse(dateStr, dateTimeFormatter).atStartOfDay();

        return dateTime;
    }

    public long diffDateInMinutes(LocalDateTime dateFirst, LocalDateTime dateLast) {
        Duration diffTime = Duration.between(dateFirst, dateLast);

        return diffTime.toMinutes();
    }
}
