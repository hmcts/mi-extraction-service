package uk.gov.hmcts.reform.mi.miextractionservice.v2.domain;

import lombok.Data;

@Data
public class SourceProperties {

    boolean enabled;
    String dateField;
    String timezone;
}
