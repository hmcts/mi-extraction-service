package uk.gov.hmcts.reform.mi.miextractionservice.component.impl.corecasedata;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import uk.gov.hmcts.reform.mi.micore.model.CoreCaseData;
import uk.gov.hmcts.reform.mi.miextractionservice.component.CoreCaseDataFormatterComponent;
import uk.gov.hmcts.reform.mi.miextractionservice.domain.OutputCoreCaseData;
import uk.gov.hmcts.reform.mi.miextractionservice.exception.ParserException;
import uk.gov.hmcts.reform.mi.miextractionservice.util.DateTimeUtil;

import java.util.Map;
import java.util.Optional;

@Component
public class CoreCaseDataOutputFormatterComponentImpl implements CoreCaseDataFormatterComponent<OutputCoreCaseData> {

    @Autowired
    private DateTimeUtil dateTimeUtil;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public OutputCoreCaseData formatData(CoreCaseData coreCaseData) {
        try {
            return OutputCoreCaseData
                .builder()
                .extraction_date(coreCaseData.getExtractionDate())
                .ce_id(getStringFromLong(coreCaseData.getCeId()))
                .ce_case_data_id(getStringFromLong(coreCaseData.getCeCaseDataId()))
                .ce_created_date(getStringFromDate(coreCaseData.getCeCreatedDate()))
                .ce_case_type_id(coreCaseData.getCeCaseTypeId())
                .ce_case_type_version(getStringFromLong(coreCaseData.getCeCaseTypeVersion()))
                .ce_state_id(coreCaseData.getCeStateId())
                .ce_state_name(coreCaseData.getCeStateName())
                .ce_summary(coreCaseData.getCeSummary())
                .ce_description(coreCaseData.getCeDescription())
                .ce_event_id(coreCaseData.getCeEventId())
                .ce_event_name(coreCaseData.getCeEventName())
                .ce_user_id(coreCaseData.getCeUserId())
                .ce_user_first_name(coreCaseData.getCeUserFirstName())
                .ce_user_last_name(coreCaseData.getCeUserLastName())
                .ce_data(getStringFromObject(coreCaseData.getCeData()))
                .cd_case_data_id(getStringFromLong(coreCaseData.getCdCaseDataId()))
                .cd_created_date(getStringFromLong(coreCaseData.getCdCreatedDate()))
                .cd_last_modified(getStringFromLong(coreCaseData.getCdLastModified()))
                .cd_jurisdiction(coreCaseData.getCdJurisdiction())
                .cd_latest_state(coreCaseData.getCdLatestState())
                .cd_reference(getStringFromLong(coreCaseData.getCdReference()))
                .cd_security_classification(coreCaseData.getCdSecurityClassification())
                .cd_version(getStringFromLong(coreCaseData.getCdVersion()))
                .cd_last_state_modified_date(getStringFromLong(coreCaseData.getCdLastModified()))
                .ce_data_classification(getStringFromObject(coreCaseData.getCeDataClassification()))
                .ce_security_classification(coreCaseData.getCeSecurityClassification())
                .build();
        } catch (JsonProcessingException e) {
            throw new ParserException("Unable to format given CoreCaseData to output format", e);
        }
    }

    private String getStringFromLong(Long input) {
        return Optional.ofNullable(input)
            .map(String::valueOf)
            .orElse(null);
    }

    private String getStringFromObject(Map<String, Object> input) throws JsonProcessingException {
        return input == null ? null : objectMapper.writeValueAsString(input);
    }

    private String getStringFromDate(Long input) {
        return Optional.ofNullable(input)
            .map(dateTimeUtil::getTimestampFormatFromLong)
            .orElse(null);
    }
}
