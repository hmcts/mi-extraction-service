package uk.gov.hmcts.reform.mi.miextractionservice.domain;

import lombok.Data;

@Data
public class SourceProperties {

    boolean enabled;
    String dateField;
    String timezone;
}
