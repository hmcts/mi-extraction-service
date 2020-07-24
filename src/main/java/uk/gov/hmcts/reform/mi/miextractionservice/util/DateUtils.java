package uk.gov.hmcts.reform.mi.miextractionservice.util;

import com.google.common.collect.ImmutableList;

import uk.gov.hmcts.reform.mi.miextractionservice.exception.ExportException;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.time.format.DateTimeFormatter.ISO_DATE;
import static java.time.format.DateTimeFormatter.ISO_DATE_TIME;
import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE;
import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME;
import static uk.gov.hmcts.reform.mi.miextractionservice.domain.Constants.YEAR_MONTH_FORMAT;

public final class DateUtils {

    private static List<DateTimeFormatter> dateTimeFormats = ImmutableList.of(
        ISO_DATE_TIME,
        ISO_LOCAL_DATE_TIME
    );

    private static List<DateTimeFormatter> dateFormats = ImmutableList.of(
        ISO_DATE,
        ISO_LOCAL_DATE
    );

    public static LocalDate parseDateString(String dateString) {
        return parseDateString(dateString, null);
    }

    public static LocalDate parseDateString(String dateString, ZoneOffset zone) {
        ZoneOffset timezone = Optional.ofNullable(zone).orElse(ZoneOffset.UTC);

        return getZonedDateTimeOptional(dateString, timezone)
            .map(ZonedDateTime::toLocalDate)
            .orElse(
                getLocalDateOptional(dateString).orElseThrow(() -> new ExportException("Unable to parse date string."))
            );
    }

    public static LocalDate getRetrievalDate(String retrieveDate) {
        return Optional.ofNullable(retrieveDate)
            .map(fromDate -> parseDateString(retrieveDate))
            .orElse(LocalDate.now());
    }

    public static List<String> getListOfYearsAndMonthsBetweenDates(LocalDate fromDate, LocalDate toDate) {
        List<String> dateList = new ArrayList<>();

        LocalDate currentDate = fromDate.withDayOfMonth(1);
        LocalDate finalDate = toDate.withDayOfMonth(28);

        do {
            dateList.add(currentDate.format(DateTimeFormatter.ofPattern(YEAR_MONTH_FORMAT)));
            currentDate = currentDate.plusMonths(1L);
        } while (currentDate.isBefore(finalDate));

        return dateList;
    }

    private static Optional<ZonedDateTime> getZonedDateTimeOptional(String dateTimeString, ZoneOffset zone) {
        return dateTimeFormats.stream()
            .map(dateTimeFormatter -> ZonedDateTime.parse(dateTimeString, dateTimeFormatter).withZoneSameInstant(zone))
            .findFirst();
    }

    private static Optional<LocalDate> getLocalDateOptional(String dateString) {
        return dateFormats.stream()
            .map(dateTimeFormatter -> LocalDate.parse(dateString, dateTimeFormatter))
            .findFirst();
    }

    private DateUtils() {
        // Private Constructor
    }
}
