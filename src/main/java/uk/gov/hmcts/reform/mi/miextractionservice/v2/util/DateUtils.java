package uk.gov.hmcts.reform.mi.miextractionservice.v2.util;

import com.google.common.collect.ImmutableList;

import uk.gov.hmcts.reform.mi.miextractionservice.exception.ExportException;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static java.time.format.DateTimeFormatter.ISO_DATE;
import static java.time.format.DateTimeFormatter.ISO_DATE_TIME;
import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE;
import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME;

public final class DateUtils {

    private static final List<DateTimeFormatter> expectedFormats = ImmutableList.of(
        ISO_DATE_TIME,
        ISO_LOCAL_DATE_TIME,
        ISO_DATE,
        ISO_LOCAL_DATE
    );

    public static final LocalDate parseDateString(String dateString, ZoneOffset zone) {
        ZonedDateTime parsedDate = expectedFormats.stream()
            .map(dateTimeFormatter -> ZonedDateTime.parse(dateString, dateTimeFormatter).withZoneSameInstant(zone))
            .findFirst()
            .orElseThrow(() -> new ExportException("Unable to parse date"));

        return parsedDate.toLocalDate();
    }

    private DateUtils() {
        // Private Constructor
    }
}
