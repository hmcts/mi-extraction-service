package uk.gov.hmcts.reform.mi.miextractionservice.v2.component.filter;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Component;

import uk.gov.hmcts.reform.mi.miextractionservice.v2.domain.SourceProperties;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static uk.gov.hmcts.reform.mi.miextractionservice.v2.util.DateUtils.parseDateString;

@Component
public class DateFilterComponentImpl implements DateFilterComponent {

    @Override
    public List<JsonNode> filterByDate(List<JsonNode> data, SourceProperties sourceProperties, LocalDate fromDate, LocalDate toDate) {
        return data.stream()
            .filter(jsonNode -> isDataBetweenDates(jsonNode, sourceProperties, fromDate, toDate))
            .collect(Collectors.toList());
    }

    private boolean isDataBetweenDates(JsonNode jsonNode, SourceProperties sourceProperties, LocalDate fromDate, LocalDate toDate) {
        String dateString = jsonNode.get(sourceProperties.getDateField()).asText();
        ZoneOffset zone = Optional.ofNullable(sourceProperties.getTimezone())
            .map(timezone -> ZoneOffset.of(sourceProperties.getTimezone()))
            .orElse(ZoneOffset.UTC);

        LocalDate date = parseDateString(dateString, zone);

        return date.isAfter(fromDate.minusDays(1L))
            && date.isBefore(toDate.plusDays(1L));
    }
}
