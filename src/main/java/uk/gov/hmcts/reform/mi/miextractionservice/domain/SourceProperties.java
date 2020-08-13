package uk.gov.hmcts.reform.mi.miextractionservice.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Data
public class SourceProperties {

    boolean enabled;
    String prefix;
    String dateField;
    String timezone;
    SourceTypeEnum type;
}
