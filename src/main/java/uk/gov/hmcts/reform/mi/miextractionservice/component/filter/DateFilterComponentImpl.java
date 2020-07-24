package uk.gov.hmcts.reform.mi.miextractionservice.component.filter;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Component;

import uk.gov.hmcts.reform.mi.miextractionservice.domain.SourceProperties;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Optional;

import static uk.gov.hmcts.reform.mi.miextractionservice.util.DateUtils.parseDateString;

@Component
public class DateFilterComponentImpl implements DateFilterComponent {

    @Override
    public boolean filterByDate(JsonNode data, SourceProperties sourceProperties, LocalDate fromDate, LocalDate toDate) {
        String dateString = data.get(sourceProperties.getDateField()).asText();
        ZoneOffset zone = Optional.ofNullable(sourceProperties.getTimezone())
            .map(timezone -> ZoneOffset.of(sourceProperties.getTimezone()))
            .orElse(ZoneOffset.UTC);

        LocalDate date = parseDateString(dateString, zone);

        // Plus and minus one day to account for same day dates.
        return date.isAfter(fromDate.minusDays(1L)) && date.isBefore(toDate.plusDays(1L));
    }
}
