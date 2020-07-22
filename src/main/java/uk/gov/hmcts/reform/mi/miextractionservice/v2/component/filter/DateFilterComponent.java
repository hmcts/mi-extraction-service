package uk.gov.hmcts.reform.mi.miextractionservice.v2.component.filter;

import com.fasterxml.jackson.databind.JsonNode;

import uk.gov.hmcts.reform.mi.miextractionservice.v2.domain.SourceProperties;

import java.time.LocalDate;
import java.util.List;

public interface DateFilterComponent {

    List<JsonNode> filterByDate(List<JsonNode> data, SourceProperties sourceProperties, LocalDate fromDate, LocalDate toDate);
}
